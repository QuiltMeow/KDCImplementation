package ew.sr.x1c.quilt.meow.kdc.simulator;

import ew.sr.x1c.quilt.meow.kdc.simulator.constant.Constant;
import ew.sr.x1c.quilt.meow.kdc.simulator.handler.PacketCreator;
import ew.sr.x1c.quilt.meow.kdc.simulator.handler.PublicKeyHandler;
import ew.sr.x1c.quilt.meow.kdc.simulator.packet.data.ByteArrayByteStream;
import ew.sr.x1c.quilt.meow.kdc.simulator.packet.data.LittleEndianAccessor;
import ew.sr.x1c.quilt.meow.kdc.simulator.util.HexUtil;
import java.util.Arrays;
import java.util.logging.Level;

public class Bob extends Endpoint {

    private static Bob instance;

    public static Bob getInstance() {
        if (instance == null) {
            instance = new Bob();
        }
        return instance;
    }

    private Bob() {
        localName = "Bob";
        localPrivateKey = KeyData.getInstance().getBobPrivateKey();
        localPrivateKeyByte = KeyData.getInstance().getBobPrivateKeyByte();
    }

    public byte[] connectResponse(byte[] data) throws InterruptedException {
        LittleEndianAccessor slea = new LittleEndianAccessor(new ByteArrayByteStream(data));

        byte[] decryptByte = PublicKeyHandler.RSADecrypt(localPrivateKey, slea.read(slea.readShort()));
        MainGUI.getInstance().getBobLogger().log(Level.INFO, "解密資料 : {0}", HexUtil.toString(decryptByte));

        LittleEndianAccessor decrypt = new LittleEndianAccessor(new ByteArrayByteStream(decryptByte));
        String responseRemoteName = decrypt.readLengthAsciiString();
        byte[] responseRemoteNonce = decrypt.read(decrypt.readShort());

        MainGUI.getInstance().getBobLogger().log(Level.INFO, "接收遠端名稱 : {0}", responseRemoteName);
        MainGUI.getInstance().getBobLogger().log(Level.INFO, "接收遠端 Nonce : {0}", HexUtil.toString(responseRemoteNonce));

        remoteName = responseRemoteName;
        setRemoteNonce(responseRemoteNonce);
        requestPublicKey(remoteName, MainGUI.getInstance().getBobLogger());

        byte[] verify = PacketCreator.verifyRequest(remotePublicKey, remoteNonce, localNonce);
        MainGUI.getInstance().getBobLogger().log(Level.INFO, "請求驗證資料 : {0}", HexUtil.toString(verify));
        Thread.sleep(Constant.SLEEP_TIME);
        return verify;
    }

    public void finalVerify(byte[] data) {
        LittleEndianAccessor slea = new LittleEndianAccessor(new ByteArrayByteStream(data));

        byte[] decryptByte = PublicKeyHandler.RSADecrypt(localPrivateKey, slea.read(slea.readShort()));
        MainGUI.getInstance().getBobLogger().log(Level.INFO, "解密驗證回應 : {0}", HexUtil.toString(decryptByte));

        LittleEndianAccessor decrypt = new LittleEndianAccessor(new ByteArrayByteStream(decryptByte));
        byte[] responseSelfNonce = decrypt.read(decrypt.readShort());
        MainGUI.getInstance().getBobLogger().log(Level.INFO, "驗證本地 Nonce : {0}", HexUtil.toString(responseSelfNonce));
        if (!Arrays.equals(responseSelfNonce, localNonce)) {
            throw new RuntimeException("本地 Nonce 驗證失敗");
        }
        MainGUI.getInstance().getBobLogger().log(Level.INFO, "安全通道建立完成");
    }

    @Override
    public void showKeyInfo() {
        MainGUI.getInstance().getBobLogger().log(Level.INFO, "公開金鑰 : {0}", HexUtil.toString(KeyData.getInstance().getBobPublicKeyByte()));
        MainGUI.getInstance().getBobLogger().log(Level.INFO, "秘密金鑰 : {0}", HexUtil.toString(localPrivateKeyByte));
        MainGUI.getInstance().getBobLogger().log(Level.INFO, "本地 Nonce : {0}", HexUtil.toString(localNonce));
    }
}
