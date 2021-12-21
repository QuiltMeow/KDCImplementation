package ew.sr.x1c.quilt.meow.endpoint.server;

import ew.sr.x1c.quilt.meow.endpoint.MainGUI;
import ew.sr.x1c.quilt.meow.endpoint.RemoteUser;
import ew.sr.x1c.quilt.meow.endpoint.Session;
import ew.sr.x1c.quilt.meow.endpoint.constant.EndpointConstant;
import ew.sr.x1c.quilt.meow.endpoint.handler.AESCrypto;
import ew.sr.x1c.quilt.meow.endpoint.handler.PublicKeyHandler;
import ew.sr.x1c.quilt.meow.endpoint.key.KeyData;
import ew.sr.x1c.quilt.meow.endpoint.packet.data.ByteArrayByteStream;
import ew.sr.x1c.quilt.meow.endpoint.packet.data.LittleEndianAccessor;
import ew.sr.x1c.quilt.meow.endpoint.packet.header.CommunicatePacketOPCode;
import ew.sr.x1c.quilt.meow.endpoint.packet.header.CryptMode;
import ew.sr.x1c.quilt.meow.endpoint.util.HexUtil;
import ew.sr.x1c.quilt.meow.endpoint.util.ImageUtil;
import ew.sr.x1c.quilt.meow.endpoint.util.PathUtil;
import ew.sr.x1c.quilt.meow.endpoint.util.Randomizer;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.logging.Level;

@Sharable
public class CommunicateServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        RemoteUser user = (RemoteUser) context.channel().attr(RemoteUser.USER_KEY).get();
        if (user != null) {
            user.disconnect();
        }
        context.channel().close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext context, Object event) {
        RemoteUser user = (RemoteUser) context.channel().attr(RemoteUser.USER_KEY).get();
        if (user == null) {
            context.channel().close();
            return;
        }
        user.sendPing();
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) {
        byte[] data = (byte[]) message;
        LittleEndianAccessor slea = new LittleEndianAccessor(new ByteArrayByteStream(data));
        if (slea.available() < 2) {
            return;
        }

        RemoteUser user = (RemoteUser) context.channel().attr(RemoteUser.USER_KEY).get();
        if (user == null) {
            return;
        }

        short headerValue = slea.readShort();
        if (headerValue == EndpointConstant.PONG_HEADER) {
            user.pongReceive();
            return;
        }

        CommunicatePacketOPCode header = CommunicatePacketOPCode.getOPCode(headerValue);
        if (header != null) {
            handlePacket(user, header, slea);
        }
    }

    private void handlePacket(RemoteUser user, CommunicatePacketOPCode header, LittleEndianAccessor slea) {
        try {
            switch (header) {
                case CONNECTION_CREATE: {
                    if (user.getRemoteName() != null) {
                        break;
                    }
                    byte[] decryptByte = PublicKeyHandler.RSADecrypt(KeyData.getInstance().getLocalPrivateKey(), slea.read(slea.readShort()));
                    LittleEndianAccessor decrypt = new LittleEndianAccessor(new ByteArrayByteStream(decryptByte));
                    String remoteName = decrypt.readLengthAsciiString();
                    if (remoteName.length() > EndpointConstant.MAX_ACCOUNT_LENGTH) {
                        user.getSession().close();
                        break;
                    }
                    byte[] remoteNonce = decrypt.read(decrypt.readShort());

                    user.setRemoteName(remoteName);
                    MainGUI.getInstance().getUserStorage().registerUser(user);

                    MainGUI.getInstance().getLogger().log(Level.INFO, "[服務端] 本地 Nonce : {0} 遠端使用者 : {1} 遠端 Nonce : {2}", new Object[]{
                        HexUtil.toString(user.getLocalNonce()), user.getRemoteName(), HexUtil.toString(remoteNonce)
                    });
                    user.setRemoteNonce(remoteNonce);
                    user.requestPublicKey();
                    break;
                }
                case VERIFY: {
                    if (user.canCommunicate()) {
                        break;
                    }
                    byte[] decryptByte = PublicKeyHandler.RSADecrypt(KeyData.getInstance().getLocalPrivateKey(), slea.read(slea.readShort()));
                    LittleEndianAccessor decrypt = new LittleEndianAccessor(new ByteArrayByteStream(decryptByte));
                    byte[] selfNonce = decrypt.read(decrypt.readShort());
                    if (!Arrays.equals(selfNonce, user.getLocalNonce())) {
                        user.getSession().close();
                        break;
                    }
                    user.calculateIV();
                    break;
                }
                case MESSAGE: {
                    if (!user.canCommunicate()) {
                        break;
                    }
                    CryptMode mode = CryptMode.values()[slea.readByte()];
                    byte[] encrypt = slea.read(slea.readShort());
                    byte[] decrypt;
                    if (mode == CryptMode.AES_CBC) {
                        decrypt = AESCrypto.CBCDecrypt(user.getIV(), encrypt);
                    } else {
                        decrypt = PublicKeyHandler.RSADecrypt(KeyData.getInstance().getLocalPrivateKey(), encrypt);
                    }
                    String message = new String(decrypt, EndpointConstant.DEFAULT_CHARSET);
                    if (message.length() > EndpointConstant.MAX_MESSAGE_LENGTH) {
                        break;
                    }
                    MainGUI.getInstance().getMessageLogger().log(Level.INFO, "{0} : {1}", new Object[]{
                        user.getRemoteName(), message
                    });
                    if (MainGUI.getInstance().showEncryptMessage()) {
                        MainGUI.getInstance().getMessageLogger().log(Level.INFO, "原始訊息 : {0}", HexUtil.toString(encrypt));
                    }
                    break;
                }
                case FILE: {
                    if (!user.canCommunicate()) {
                        break;
                    }
                    CryptMode mode = CryptMode.values()[slea.readByte()];
                    byte[] encrypt = slea.read(slea.readInt());
                    byte[] decryptByte;
                    if (mode == CryptMode.AES_CBC) {
                        decryptByte = AESCrypto.CBCDecrypt(user.getIV(), encrypt);
                    } else {
                        decryptByte = PublicKeyHandler.RSADecrypt(KeyData.getInstance().getLocalPrivateKey(), encrypt);
                    }
                    LittleEndianAccessor decrypt = new LittleEndianAccessor(new ByteArrayByteStream(decryptByte));
                    String fileName = PathUtil.cleanPath(decrypt.readLengthAsciiString());
                    if (fileName.isEmpty()) {
                        break;
                    }

                    String downloadName = Math.abs(Randomizer.nextInt()) + "-" + Math.abs(System.nanoTime()) + "-" + fileName;
                    File download = new File(MainGUI.getInstance().getDownloadFolder(), downloadName);
                    if (download.exists()) {
                        break;
                    }

                    String path = download.getPath();
                    if (!PathUtil.isValidPath(path)) {
                        break;
                    }

                    int fileLength = decrypt.readInt();
                    if (fileLength > EndpointConstant.MAX_FILE_LENGTH) {
                        break;
                    }
                    byte[] file = decrypt.read(fileLength);
                    Files.write(download.toPath(), file);
                    MainGUI.getInstance().getMessageLogger().log(Level.INFO, "{0} : 已傳送檔案 {1}", new Object[]{
                        user.getRemoteName(), downloadName
                    });
                    if (ImageUtil.isImage(path)) {
                        MainGUI.getInstance().appendImageMessage(path);
                    }
                    break;
                }
            }
        } catch (Exception ex) {
            MainGUI.getInstance().getLogger().log(Level.WARNING, "處理封包時發生例外狀況", ex);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) {
        RemoteUser user = (RemoteUser) context.channel().attr(RemoteUser.USER_KEY).get();
        if (user != null) {
            user.disconnect();
            context.channel().attr(RemoteUser.USER_KEY).set(null);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        RemoteUser user = new RemoteUser(new Session(context.channel()), true);
        context.channel().attr(RemoteUser.USER_KEY).set(user);
    }
}
