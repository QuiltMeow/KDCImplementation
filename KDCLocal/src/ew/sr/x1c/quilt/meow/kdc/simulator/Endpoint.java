package ew.sr.x1c.quilt.meow.kdc.simulator;

import ew.sr.x1c.quilt.meow.kdc.simulator.constant.Constant;
import ew.sr.x1c.quilt.meow.kdc.simulator.handler.PacketCreator;
import ew.sr.x1c.quilt.meow.kdc.simulator.handler.PublicKeyHandler;
import ew.sr.x1c.quilt.meow.kdc.simulator.packet.data.ByteArrayByteStream;
import ew.sr.x1c.quilt.meow.kdc.simulator.packet.data.LittleEndianAccessor;
import ew.sr.x1c.quilt.meow.kdc.simulator.packet.data.PacketLittleEndianWriter;
import ew.sr.x1c.quilt.meow.kdc.simulator.util.HexUtil;
import ew.sr.x1c.quilt.meow.kdc.simulator.util.Randomizer;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;

public abstract class Endpoint {

    @Getter
    protected byte[] localNonce;

    @Getter
    protected RSAPrivateKey localPrivateKey;

    @Getter
    protected byte[] localPrivateKeyByte;

    @Getter
    protected byte[] remoteNonce;

    @Getter
    protected String localName;

    @Getter
    protected String remoteName;

    @Getter
    protected RSAPublicKey remotePublicKey;

    @Getter
    protected byte[] remotePublicKeyByte;

    @Getter
    protected String timestamp;

    public Endpoint() {
        renewNonce();
    }

    public final void renewNonce() {
        localNonce = generateNonce();
    }

    public abstract void showKeyInfo();

    public void requestPublicKey(String request, Logger logger) throws InterruptedException {
        timestamp = String.valueOf(System.currentTimeMillis());
        logger.log(Level.INFO, "KDC 公開金鑰請求");
        logger.log(Level.INFO, "時間戳記 : {0}", timestamp);
        byte[] send = PacketCreator.KDCRequest(request, timestamp);
        logger.log(Level.INFO, "請求資料 : {0}", HexUtil.toString(send));
        Thread.sleep(Constant.SLEEP_TIME);

        byte[] response = KDC.responsePublicKey(send);
        LittleEndianAccessor slea = new LittleEndianAccessor(new ByteArrayByteStream(response));
        byte[] key = slea.read(slea.readShort());
        String target = slea.readLengthAsciiString();
        String responseTimestamp = slea.readLengthAsciiString();

        logger.log(Level.INFO, "KDC 回應公開金鑰 : {0}", HexUtil.toString(key));
        logger.log(Level.INFO, "KDC 回應目標 : {0}", target);
        logger.log(Level.INFO, "KDC 回應時間戳記 : {0}", responseTimestamp);

        PacketLittleEndianWriter craft = new PacketLittleEndianWriter();
        craft.writeShort(key.length);
        craft.write(key);
        craft.writeLengthAsciiString(target);
        craft.writeLengthAsciiString(responseTimestamp);

        byte[] signature = slea.read(slea.readShort());
        logger.log(Level.INFO, "KDC 回應簽章 : {0}", HexUtil.toString(signature));

        boolean verify = PublicKeyHandler.RSASignatureVerify(KeyData.getInstance().getKDCPublicKey(), craft.getPacket(), signature);
        if (!verify) {
            throw new RuntimeException("KDC 金鑰請求簽章驗證失敗");
        }
        if (!responseTimestamp.equals(timestamp)) {
            throw new RuntimeException("KDC 金鑰請求時間戳記不相符");
        }
        logger.log(Level.INFO, "簽章與時間戳記驗證完成");
        setRemotePublicKey(key);
    }

    public void setRemotePublicKey(byte[] key) {
        remotePublicKeyByte = key;
        remotePublicKey = (RSAPublicKey) PublicKeyHandler.loadPublicKey(key);
    }

    public void setRemoteNonce(byte[] nonce) {
        if (nonce.length != Constant.NONCE_SIZE) {
            throw new RuntimeException("Nonce 長度錯誤");
        }
        remoteNonce = nonce;
    }

    public static byte[] generateNonce() {
        byte[] ret = new byte[Constant.NONCE_SIZE];
        Randomizer.nextByte(ret);
        return ret;
    }
}
