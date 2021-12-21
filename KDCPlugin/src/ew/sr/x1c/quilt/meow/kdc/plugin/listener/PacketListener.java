package ew.sr.x1c.quilt.meow.kdc.plugin.listener;

import ew.sr.x1c.quilt.meow.constant.Constant;
import ew.sr.x1c.quilt.meow.event.EventHandler;
import ew.sr.x1c.quilt.meow.event.implement.ClientMessageReceiveEvent;
import ew.sr.x1c.quilt.meow.kdc.constant.PluginConstant;
import ew.sr.x1c.quilt.meow.kdc.plugin.handler.AESCrypto;
import ew.sr.x1c.quilt.meow.kdc.plugin.handler.KDCKey;
import ew.sr.x1c.quilt.meow.kdc.plugin.handler.MemberHandler;
import ew.sr.x1c.quilt.meow.kdc.plugin.handler.PacketCreator;
import ew.sr.x1c.quilt.meow.kdc.plugin.handler.PublicKeyHandler;
import ew.sr.x1c.quilt.meow.kdc.plugin.packet.header.KDCPacketOPCode;
import ew.sr.x1c.quilt.meow.packet.data.ByteArrayByteStream;
import ew.sr.x1c.quilt.meow.packet.data.LittleEndianAccessor;
import ew.sr.x1c.quilt.meow.packet.data.PacketLittleEndianWriter;
import ew.sr.x1c.quilt.meow.plugin.Listener;
import ew.sr.x1c.quilt.meow.server.GeneralManager;
import java.security.interfaces.RSAPublicKey;
import java.util.logging.Level;
import javax.crypto.spec.IvParameterSpec;

public class PacketListener implements Listener {

    public static boolean isNullOrBlank(String input) {
        return input == null || input.trim().length() == 0;
    }

    @EventHandler
    public void onMessageReceive(ClientMessageReceiveEvent event) {
        LittleEndianAccessor slea = event.getPacket();
        KDCPacketOPCode header = KDCPacketOPCode.getOPCode(event.getHeader());
        if (header == null) {
            return;
        }
        try {
            switch (header) {
                case REGISTER: {
                    if (event.getClient().getAccount() != null) {
                        break;
                    }
                    String account = slea.readLengthAsciiString();
                    String password = new String(PublicKeyHandler.RSADecrypt(KDCKey.getInstance().getPrivateKey(), slea.read(slea.readShort())), Constant.DEFAULT_CHARSET);

                    boolean register;
                    if (isNullOrBlank(account) || isNullOrBlank(password)) {
                        register = false;
                    } else if (account.length() > PluginConstant.MAX_ACCOUNT_LENGTH || password.length() > PluginConstant.MAX_ACCOUNT_LENGTH) {
                        register = false;
                    } else {
                        register = MemberHandler.register(event.getClient(), account, password);
                    }

                    PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
                    mplew.writeShort(KDCPacketOPCode.REGISTER.getHeader());
                    mplew.write(register);
                    event.getClient().getSession().write(mplew.getPacket());
                    break;
                }
                case LOGIN: {
                    if (event.getClient().getAccount() != null) {
                        break;
                    }
                    String account = slea.readLengthAsciiString();
                    String password = new String(PublicKeyHandler.RSADecrypt(KDCKey.getInstance().getPrivateKey(), slea.read(slea.readShort())), Constant.DEFAULT_CHARSET);

                    boolean login;
                    if (isNullOrBlank(account) || isNullOrBlank(password)) {
                        login = false;
                    } else if (account.length() > PluginConstant.MAX_ACCOUNT_LENGTH || password.length() > PluginConstant.MAX_ACCOUNT_LENGTH) {
                        login = false;
                    } else {
                        login = MemberHandler.login(event.getClient(), account, password);
                    }

                    PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
                    mplew.writeShort(KDCPacketOPCode.LOGIN.getHeader());
                    mplew.write(login);
                    event.getClient().getSession().write(mplew.getPacket());
                    break;
                }
                case PUBLIC_KEY_UPDATE: {
                    String account = event.getClient().getAccount();
                    if (account == null) {
                        break;
                    }
                    byte[] AESIV = PublicKeyHandler.RSADecrypt(KDCKey.getInstance().getPrivateKey(), slea.read(slea.readShort()));
                    byte[] decrypt = AESCrypto.CBCDecrypt(new IvParameterSpec(AESIV), slea.read(slea.readShort()));
                    LittleEndianAccessor message = new LittleEndianAccessor(new ByteArrayByteStream(decrypt));
                    byte[] keyByte = message.read(message.readShort());
                    byte[] signature = message.read(message.readShort());
                    RSAPublicKey key = (RSAPublicKey) PublicKeyHandler.loadPublicKey(keyByte);

                    PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
                    mplew.writeShort(KDCPacketOPCode.PUBLIC_KEY_UPDATE.getHeader());

                    boolean update;
                    if (key.getModulus().bitLength() != PluginConstant.KEY_LENGTH) {
                        update = false;
                    } else if (PublicKeyHandler.RSASignatureVerify(key, keyByte, signature)) {
                        update = MemberHandler.updateKey(account, keyByte);
                    } else {
                        update = false;
                    }

                    mplew.write(update);
                    event.getClient().getSession().write(mplew.getPacket());
                    break;
                }
                case PUBLIC_KEY_GET: {
                    String target = slea.readLengthAsciiString();
                    String timestamp = slea.readLengthAsciiString();
                    GeneralManager.getInstance().getLogger().log(Level.INFO, "公開金鑰請求 使用者 : {0} 時間戳記 : {1}", new Object[]{
                        target, timestamp
                    });
                    event.getClient().getSession().write(PacketCreator.publicKeyResponse(target, timestamp));
                    break;
                }
                case KDC_PUBLIC_kEY: {
                    // 存在中間人攻擊 應先行 [信任] 金鑰而非從網路直接抓取
                    event.getClient().getSession().write(PacketCreator.KDCPublicKey());
                    break;
                }
            }
        } catch (Exception ex) {
            GeneralManager.getInstance().getLogger().log(Level.WARNING, "處理封包時發生例外狀況", ex);
        }
    }
}
