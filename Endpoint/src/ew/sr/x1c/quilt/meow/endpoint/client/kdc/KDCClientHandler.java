package ew.sr.x1c.quilt.meow.endpoint.client.kdc;

import ew.sr.x1c.quilt.meow.endpoint.MainGUI;
import ew.sr.x1c.quilt.meow.endpoint.RemoteUser;
import ew.sr.x1c.quilt.meow.endpoint.Session;
import ew.sr.x1c.quilt.meow.endpoint.constant.EndpointConstant;
import ew.sr.x1c.quilt.meow.endpoint.handler.PacketCreator;
import ew.sr.x1c.quilt.meow.endpoint.handler.PublicKeyHandler;
import ew.sr.x1c.quilt.meow.endpoint.key.KeyData;
import ew.sr.x1c.quilt.meow.endpoint.packet.data.ByteArrayByteStream;
import ew.sr.x1c.quilt.meow.endpoint.packet.data.LittleEndianAccessor;
import ew.sr.x1c.quilt.meow.endpoint.packet.data.PacketLittleEndianWriter;
import ew.sr.x1c.quilt.meow.endpoint.packet.header.KDCPacketOPCode;
import ew.sr.x1c.quilt.meow.endpoint.util.HexUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.logging.Level;
import javax.swing.JOptionPane;

public class KDCClientHandler extends ChannelInboundHandlerAdapter {

    private final KDCSession kdcSession;

    public KDCClientHandler(KDCSession kdcSession) {
        this.kdcSession = kdcSession;
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        kdcSession.setSession(new Session(context.channel()));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext context, Object event) {
        context.channel().close();
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) {
        byte[] data = (byte[]) message;
        LittleEndianAccessor slea = new LittleEndianAccessor(new ByteArrayByteStream(data));
        if (slea.available() < 2) {
            return;
        }

        short headerValue = slea.readShort();
        if (headerValue == EndpointConstant.PING_HEADER) {
            kdcSession.getSession().write(PacketCreator.pong());
            return;
        }

        KDCPacketOPCode header = KDCPacketOPCode.getOPCode(headerValue);
        if (header != null) {
            handlePacket(header, slea);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) {
        MainGUI.getInstance().getLogger().log(Level.SEVERE, "與 KDC 伺服器連線中斷");
        JOptionPane.showMessageDialog(MainGUI.getInstance(), "與 KDC 伺服器連線中斷");
        System.exit(EndpointConstant.EXIT_WITH_ERROR);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        context.channel().close();
    }

    private void handlePacket(KDCPacketOPCode header, LittleEndianAccessor slea) {
        try {
            switch (header) {
                case REGISTER: {
                    boolean success = slea.readByte() == 1;
                    MainGUI.getInstance().getLogger().log(success ? Level.INFO : Level.WARNING, "KDC 註冊{0}", success ? "成功" : "失敗");
                    break;
                }
                case LOGIN: {
                    boolean success = slea.readByte() == 1;
                    MainGUI.getInstance().updateLoginStatus(success);
                    MainGUI.getInstance().getLogger().log(success ? Level.INFO : Level.WARNING, "KDC 登入{0}", success ? "成功" : "失敗");
                    break;
                }
                case PUBLIC_KEY_UPDATE: {
                    boolean success = slea.readByte() == 1;
                    MainGUI.getInstance().getLogger().log(success ? Level.INFO : Level.WARNING, "KDC 公開金鑰更新{0}", success ? "成功" : "失敗");
                    break;
                }
                case PUBLIC_KEY_GET: {
                    PacketLittleEndianWriter craft = new PacketLittleEndianWriter();
                    boolean valid = slea.readByte() == 1;
                    craft.write(valid);

                    if (valid) {
                        byte[] key = slea.read(slea.readShort());
                        String target = slea.readLengthAsciiString();
                        String timestamp = slea.readLengthAsciiString();

                        craft.writeShort(key.length);
                        craft.write(key);
                        craft.writeLengthAsciiString(target);
                        craft.writeLengthAsciiString(timestamp);

                        byte[] signature = slea.read(slea.readShort());
                        boolean verify = PublicKeyHandler.RSASignatureVerify(KeyData.getInstance().getKDCPublicKey(), craft.getPacket(), signature);
                        if (!verify) {
                            MainGUI.getInstance().getLogger().log(Level.SEVERE, "KDC 金鑰請求簽章驗證失敗");
                            kdcSession.getSession().close();
                            break;
                        }
                        RemoteUser user = MainGUI.getInstance().getUserStorage().getUserByName(target);
                        if (user != null) {
                            if (!user.getTimestamp().equals(timestamp)) {
                                MainGUI.getInstance().getLogger().log(Level.SEVERE, "KDC 金鑰請求時間戳記不相符");
                                kdcSession.getSession().close();
                                break;
                            }
                            MainGUI.getInstance().getLogger().log(Level.INFO, "KDC 客戶端接收使用者 : {0} 時間戳記 : {1} 公開金鑰 : {2}", new Object[]{
                                target, timestamp, HexUtil.toString(key)
                            });
                            user.setRemotePublicKey(key);

                            if (user.isPassive()) {
                                user.getSession().write(PacketCreator.verifyRequest(user.getRemotePublicKey(), user.getRemoteNonce(), user.getLocalNonce()));
                            } else {
                                user.getSession().write(PacketCreator.connectCreateRequest(user.getRemotePublicKey(), MainGUI.getInstance().getLocalAccount(), user.getLocalNonce()));
                            }
                        }
                    } else {
                        String target = slea.readLengthAsciiString();
                        String timestamp = slea.readLengthAsciiString();

                        craft.writeLengthAsciiString(target);
                        craft.writeLengthAsciiString(timestamp);

                        byte[] signature = slea.read(slea.readShort());
                        boolean verify = PublicKeyHandler.RSASignatureVerify(KeyData.getInstance().getKDCPublicKey(), craft.getPacket(), signature);
                        if (!verify) {
                            MainGUI.getInstance().getLogger().log(Level.SEVERE, "KDC 金鑰請求簽章驗證失敗");
                            kdcSession.getSession().close();
                        }
                        RemoteUser user = MainGUI.getInstance().getUserStorage().getUserByName(target);
                        if (user != null) {
                            MainGUI.getInstance().getLogger().log(Level.WARNING, "公開金鑰請求失敗");
                            user.getSession().close();
                        }
                    }
                    break;
                }
                case KDC_PUBLIC_kEY: {
                    byte[] key = slea.read(slea.readShort());
                    byte[] signature = slea.read(slea.readShort());
                    boolean verify = PublicKeyHandler.RSASignatureVerify(PublicKeyHandler.loadPublicKey(key), key, signature);
                    if (!verify) {
                        MainGUI.getInstance().getLogger().log(Level.SEVERE, "KDC 公開金鑰簽章驗證失敗");
                        kdcSession.getSession().close();
                        break;
                    }
                    MainGUI.getInstance().getLogger().log(Level.INFO, "KDC 公開金鑰 : {0}", HexUtil.toString(key));
                    break;
                }
            }
        } catch (Exception ex) {
            MainGUI.getInstance().getLogger().log(Level.WARNING, "處理封包時發生例外狀況", ex);
        }
    }
}
