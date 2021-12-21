package ew.sr.x1c.quilt.meow.endpoint.handler;

import ew.sr.x1c.quilt.meow.endpoint.RemoteUser;
import ew.sr.x1c.quilt.meow.endpoint.key.KeyData;
import ew.sr.x1c.quilt.meow.endpoint.constant.EndpointConstant;
import ew.sr.x1c.quilt.meow.endpoint.packet.data.PacketLittleEndianWriter;
import ew.sr.x1c.quilt.meow.endpoint.packet.header.CommunicatePacketOPCode;
import ew.sr.x1c.quilt.meow.endpoint.packet.header.CryptMode;
import ew.sr.x1c.quilt.meow.endpoint.packet.header.KDCPacketOPCode;
import ew.sr.x1c.quilt.meow.endpoint.util.Randomizer;
import java.nio.charset.Charset;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.spec.IvParameterSpec;

public class PacketCreator {

    public static byte[] connectCreateRequest(RSAPublicKey targetKey, String selfAccount, byte[] selfNonce) {
        PacketLittleEndianWriter craft = new PacketLittleEndianWriter();
        craft.writeLengthAsciiString(selfAccount);
        craft.writeShort(selfNonce.length);
        craft.write(selfNonce);
        byte[] request = craft.getPacket();

        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
        mplew.writeShort(CommunicatePacketOPCode.CONNECTION_CREATE.getHeader());
        byte[] encrypt = PublicKeyHandler.RSAEncrypt(targetKey, request);
        mplew.writeShort(encrypt.length);
        mplew.write(encrypt);
        return mplew.getPacket();
    }

    public static byte[] KDCRequest(String target, String timestamp) {
        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
        mplew.writeShort(KDCPacketOPCode.PUBLIC_KEY_GET.getHeader());
        mplew.writeLengthAsciiString(target);
        mplew.writeLengthAsciiString(timestamp);
        return mplew.getPacket();
    }

    public static byte[] verifyRequest(RSAPublicKey targetKey, byte[] targetNonce, byte[] selfNonce) {
        PacketLittleEndianWriter craft = new PacketLittleEndianWriter();
        craft.writeShort(targetNonce.length);
        craft.write(targetNonce);
        craft.writeShort(selfNonce.length);
        craft.write(selfNonce);
        byte[] verify = craft.getPacket();

        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
        mplew.writeShort(CommunicatePacketOPCode.VERIFY.getHeader());
        byte[] encrypt = PublicKeyHandler.RSAEncrypt(targetKey, verify);
        mplew.writeShort(encrypt.length);
        mplew.write(encrypt);
        return mplew.getPacket();
    }

    public static byte[] verifyResponse(RSAPublicKey targetKey, byte[] targetNonce) {
        PacketLittleEndianWriter craft = new PacketLittleEndianWriter();
        craft.writeShort(targetNonce.length);
        craft.write(targetNonce);
        byte[] response = craft.getPacket();

        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
        mplew.writeShort(CommunicatePacketOPCode.VERIFY.getHeader());
        byte[] encrypt = PublicKeyHandler.RSAEncrypt(targetKey, response);
        mplew.writeShort(encrypt.length);
        mplew.write(encrypt);
        return mplew.getPacket();
    }

    public static byte[] message(CryptMode mode, RemoteUser target, String text) {
        byte[] textByte = text.getBytes(Charset.forName(EndpointConstant.DEFAULT_CHARSET));
        byte[] encrypt;
        if (mode == CryptMode.AES_CBC) {
            encrypt = AESCrypto.CBCEncrypt(target.getIV(), textByte);
        } else {
            encrypt = PublicKeyHandler.RSAEncrypt(target.getRemotePublicKey(), textByte);
            if (encrypt == null) {
                throw new RuntimeException("RSA 加密失敗 請檢查資料長度");
            }
        }

        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
        mplew.writeShort(CommunicatePacketOPCode.MESSAGE.getHeader());
        mplew.write(mode.ordinal());
        mplew.writeShort(encrypt.length);
        mplew.write(encrypt);
        return mplew.getPacket();
    }

    public static byte[] file(CryptMode mode, RemoteUser target, String fileName, byte[] data) {
        PacketLittleEndianWriter craft = new PacketLittleEndianWriter();
        craft.writeLengthAsciiString(fileName);
        craft.writeInt(data.length);
        craft.write(data);

        byte[] plain = craft.getPacket();
        byte[] encrypt;
        if (mode == CryptMode.AES_CBC) {
            encrypt = AESCrypto.CBCEncrypt(target.getIV(), plain);
        } else {
            encrypt = PublicKeyHandler.RSAEncrypt(target.getRemotePublicKey(), plain);
            if (encrypt == null) {
                throw new RuntimeException("RSA 加密失敗 請檢查資料長度");
            }
        }

        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
        mplew.writeShort(CommunicatePacketOPCode.FILE.getHeader());
        mplew.write(mode.ordinal());
        mplew.writeInt(encrypt.length);
        mplew.write(encrypt);
        return mplew.getPacket();
    }

    public static byte[] register(String account, String password) {
        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
        mplew.writeShort(KDCPacketOPCode.REGISTER.getHeader());
        mplew.writeLengthAsciiString(account);

        byte[] passwordByte = password.getBytes(Charset.forName(EndpointConstant.DEFAULT_CHARSET));
        byte[] encrypt = PublicKeyHandler.RSAEncrypt(KeyData.getInstance().getKDCPublicKey(), passwordByte);
        mplew.writeShort(encrypt.length);
        mplew.write(encrypt);
        return mplew.getPacket();
    }

    public static byte[] login(String account, String password) {
        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
        mplew.writeShort(KDCPacketOPCode.LOGIN.getHeader());
        mplew.writeLengthAsciiString(account);

        byte[] passwordByte = password.getBytes(Charset.forName(EndpointConstant.DEFAULT_CHARSET));
        byte[] encrypt = PublicKeyHandler.RSAEncrypt(KeyData.getInstance().getKDCPublicKey(), passwordByte);
        mplew.writeShort(encrypt.length);
        mplew.write(encrypt);
        return mplew.getPacket();
    }

    public static byte[] updatePublicKey(byte[] publicKey) {
        PacketLittleEndianWriter craft = new PacketLittleEndianWriter();
        craft.writeShort(publicKey.length);
        craft.write(publicKey);

        byte[] signature = PublicKeyHandler.RSASignature(KeyData.getInstance().getLocalPrivateKey(), publicKey);
        craft.writeShort(signature.length);
        craft.write(signature);
        byte[] plain = craft.getPacket();

        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
        mplew.writeShort(KDCPacketOPCode.PUBLIC_KEY_UPDATE.getHeader());

        byte[] AESIV = new byte[16];
        Randomizer.nextByte(AESIV);
        byte[] encryptAESIV = PublicKeyHandler.RSAEncrypt(KeyData.getInstance().getKDCPublicKey(), AESIV);
        mplew.writeShort(encryptAESIV.length);
        mplew.write(encryptAESIV);

        byte[] encrypt = AESCrypto.CBCEncrypt(new IvParameterSpec(AESIV), plain);
        mplew.writeShort(encrypt.length);
        mplew.write(encrypt);
        return mplew.getPacket();
    }

    public static byte[] downloadKDCPublicKey() {
        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
        mplew.writeShort(KDCPacketOPCode.KDC_PUBLIC_kEY.getHeader());
        return mplew.getPacket();
    }

    public static byte[] ping() {
        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
        mplew.writeShort(EndpointConstant.PING_HEADER);
        return mplew.getPacket();
    }

    public static byte[] pong() {
        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
        mplew.writeShort(EndpointConstant.PONG_HEADER);
        return mplew.getPacket();
    }
}
