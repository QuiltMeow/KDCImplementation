package ew.sr.x1c.quilt.meow.kdc.simulator.handler;

import ew.sr.x1c.quilt.meow.kdc.simulator.KeyData;
import ew.sr.x1c.quilt.meow.kdc.simulator.packet.data.PacketLittleEndianWriter;
import java.security.interfaces.RSAPublicKey;

public class PacketCreator {

    public static byte[] connectCreateRequest(RSAPublicKey targetKey, String selfAccount, byte[] selfNonce) {
        PacketLittleEndianWriter craft = new PacketLittleEndianWriter();
        craft.writeLengthAsciiString(selfAccount);
        craft.writeShort(selfNonce.length);
        craft.write(selfNonce);
        byte[] request = craft.getPacket();

        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
        byte[] encrypt = PublicKeyHandler.RSAEncrypt(targetKey, request);
        mplew.writeShort(encrypt.length);
        mplew.write(encrypt);
        return mplew.getPacket();
    }

    public static byte[] KDCRequest(String target, String timestamp) {
        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
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
        byte[] encrypt = PublicKeyHandler.RSAEncrypt(targetKey, response);
        mplew.writeShort(encrypt.length);
        mplew.write(encrypt);
        return mplew.getPacket();
    }

    public static byte[] publicKeyResponse(byte[] key, String name, String timestamp) {
        PacketLittleEndianWriter craft = new PacketLittleEndianWriter();
        craft.writeShort(key.length);
        craft.write(key);
        craft.writeLengthAsciiString(name);
        craft.writeLengthAsciiString(timestamp);
        byte[] message = craft.getPacket();

        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
        mplew.write(message);
        byte[] signature = PublicKeyHandler.RSASignature(KeyData.getInstance().getKDCPrivateKey(), message);
        mplew.writeShort(signature.length);
        mplew.write(signature);
        return mplew.getPacket();
    }
}
