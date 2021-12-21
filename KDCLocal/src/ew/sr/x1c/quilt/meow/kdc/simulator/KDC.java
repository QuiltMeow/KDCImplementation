package ew.sr.x1c.quilt.meow.kdc.simulator;

import ew.sr.x1c.quilt.meow.kdc.simulator.constant.Constant;
import ew.sr.x1c.quilt.meow.kdc.simulator.handler.PacketCreator;
import ew.sr.x1c.quilt.meow.kdc.simulator.packet.data.ByteArrayByteStream;
import ew.sr.x1c.quilt.meow.kdc.simulator.packet.data.LittleEndianAccessor;
import ew.sr.x1c.quilt.meow.kdc.simulator.util.HexUtil;
import java.util.logging.Level;

public class KDC {

    public static byte[] responsePublicKey(byte[] data) throws InterruptedException {
        LittleEndianAccessor slea = new LittleEndianAccessor(new ByteArrayByteStream(data));
        String user = slea.readLengthAsciiString();
        String timestamp = slea.readLengthAsciiString();

        MainGUI.getInstance().getKDCLogger().log(Level.INFO, "KED 接收請求名稱 : {0}", user);
        MainGUI.getInstance().getKDCLogger().log(Level.INFO, "KED 接收請求時間戳記 : {0}", timestamp);
        byte[] response = null;
        switch (user) {
            case "Alice": {
                response = PacketCreator.publicKeyResponse(KeyData.getInstance().getAlicePublicKeyByte(), user, timestamp);
                break;
            }
            case "Bob": {
                response = PacketCreator.publicKeyResponse(KeyData.getInstance().getBobPublicKeyByte(), user, timestamp);
                break;
            }
        }
        MainGUI.getInstance().getKDCLogger().log(Level.INFO, "KDC 回應資料 : {0}", HexUtil.toString(response));
        Thread.sleep(Constant.SLEEP_TIME);
        return response;
    }

    public static void showKeyInfo() {
        MainGUI.getInstance().getKDCLogger().log(Level.INFO, "公開金鑰 : {0}", HexUtil.toString(KeyData.getInstance().getKDCPublicKeyByte()));
        MainGUI.getInstance().getKDCLogger().log(Level.INFO, "秘密金鑰 : {0}", HexUtil.toString(KeyData.getInstance().getKDCPrivateKeyByte()));
    }
}
