package ew.sr.x1c.quilt.meow.kdc.simulator;

import ew.sr.x1c.quilt.meow.kdc.simulator.constant.Constant;
import ew.sr.x1c.quilt.meow.kdc.simulator.handler.PacketCreator;
import ew.sr.x1c.quilt.meow.kdc.simulator.handler.PublicKeyHandler;
import ew.sr.x1c.quilt.meow.kdc.simulator.packet.data.ByteArrayByteStream;
import ew.sr.x1c.quilt.meow.kdc.simulator.packet.data.LittleEndianAccessor;
import ew.sr.x1c.quilt.meow.kdc.simulator.util.HexUtil;
import java.util.Arrays;
import java.util.logging.Level;

public class Alice extends Endpoint {

    private static Alice instance;

    public static Alice getInstance() {
        if (instance == null) {
            instance = new Alice();
        }
        return instance;
    }

    private Alice() {
        localName = "Alice";
        localPrivateKey = KeyData.getInstance().getAlicePrivateKey();
        localPrivateKeyByte = KeyData.getInstance().getAlicePrivateKeyByte();
    }

    public void start() throws InterruptedException {
        remoteName = "Bob";
        requestPublicKey(remoteName, MainGUI.getInstance().getAliceLogger());
        byte[] verify = communicateConnect();
        verifyResponse(verify);
    }

    public byte[] communicateConnect() throws InterruptedException {
        MainGUI.getInstance().getAliceLogger().log(Level.INFO, "{0} 建立與 {1} 連線", new Object[]{
            localName, remoteName
        });
        byte[] request = PacketCreator.connectCreateRequest(remotePublicKey, localName, localNonce);
        MainGUI.getInstance().getAliceLogger().log(Level.INFO, "請求資料 : {0}", HexUtil.toString(request));
        Thread.sleep(Constant.SLEEP_TIME);
        return Bob.getInstance().connectResponse(request);
    }

    public void verifyResponse(byte[] data) throws InterruptedException {
        LittleEndianAccessor slea = new LittleEndianAccessor(new ByteArrayByteStream(data));

        byte[] decryptByte = PublicKeyHandler.RSADecrypt(localPrivateKey, slea.read(slea.readShort()));
        MainGUI.getInstance().getAliceLogger().log(Level.INFO, "解密驗證資料 : {0}", HexUtil.toString(decryptByte));

        LittleEndianAccessor decrypt = new LittleEndianAccessor(new ByteArrayByteStream(decryptByte));
        byte[] responseSelfNonce = decrypt.read(decrypt.readShort());
        byte[] responseRemoteNonce = decrypt.read(decrypt.readShort());
        MainGUI.getInstance().getAliceLogger().log(Level.INFO, "驗證本地 Nonce : {0}", HexUtil.toString(responseSelfNonce));
        MainGUI.getInstance().getAliceLogger().log(Level.INFO, "驗證遠端 Nonce : {0}", HexUtil.toString(responseRemoteNonce));

        if (!Arrays.equals(responseSelfNonce, localNonce)) {
            throw new RuntimeException("本地 Nonce 驗證失敗");
        }
        MainGUI.getInstance().getAliceLogger().log(Level.INFO, "本地 Nonce 驗證完成");
        setRemoteNonce(responseRemoteNonce);

        byte[] send = PacketCreator.verifyResponse(remotePublicKey, responseRemoteNonce);
        MainGUI.getInstance().getAliceLogger().log(Level.INFO, "回應驗證請求 : {0}", HexUtil.toString(send));
        Thread.sleep(Constant.SLEEP_TIME);
        Bob.getInstance().finalVerify(send);
        MainGUI.getInstance().getAliceLogger().log(Level.INFO, "安全通道建立完成");
    }

    @Override
    public void showKeyInfo() {
        MainGUI.getInstance().getAliceLogger().log(Level.INFO, "公開金鑰 : {0}", HexUtil.toString(KeyData.getInstance().getAlicePublicKeyByte()));
        MainGUI.getInstance().getAliceLogger().log(Level.INFO, "秘密金鑰 : {0}", HexUtil.toString(localPrivateKeyByte));
        MainGUI.getInstance().getAliceLogger().log(Level.INFO, "本地 Nonce : {0}", HexUtil.toString(localNonce));
    }
}
