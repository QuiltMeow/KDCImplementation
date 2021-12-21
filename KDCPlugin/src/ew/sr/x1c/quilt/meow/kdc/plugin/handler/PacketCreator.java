package ew.sr.x1c.quilt.meow.kdc.plugin.handler;

import ew.sr.x1c.quilt.meow.kdc.plugin.packet.header.KDCPacketOPCode;
import ew.sr.x1c.quilt.meow.packet.data.PacketLittleEndianWriter;

public class PacketCreator {

    public static byte[] publicKeyResponse(String name, String timestamp) {
        PacketLittleEndianWriter craft = new PacketLittleEndianWriter();
        byte[] publicKey = MemberHandler.queryKey(name);
        if (publicKey == null || publicKey.length == 0) {
            craft.write(0);
            craft.writeLengthAsciiString(name);
            craft.writeLengthAsciiString(timestamp);
        } else {
            craft.write(1);
            craft.writeShort(publicKey.length);
            craft.write(publicKey);
            craft.writeLengthAsciiString(name);
            craft.writeLengthAsciiString(timestamp);
        }
        byte[] message = craft.getPacket();

        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
        mplew.writeShort(KDCPacketOPCode.PUBLIC_KEY_GET.getHeader());
        mplew.write(message);
        byte[] signature = PublicKeyHandler.RSASignature(KDCKey.getInstance().getPrivateKey(), message);
        mplew.writeShort(signature.length);
        mplew.write(signature);
        return mplew.getPacket();
    }

    public static byte[] KDCPublicKey() {
        byte[] publicKey = KDCKey.getInstance().getPublicKeyByte();
        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
        mplew.writeShort(KDCPacketOPCode.KDC_PUBLIC_kEY.getHeader());
        mplew.writeShort(publicKey.length);
        mplew.write(publicKey);

        byte[] signature = PublicKeyHandler.RSASignature(KDCKey.getInstance().getPrivateKey(), publicKey);
        mplew.writeShort(signature.length);
        mplew.write(signature);
        return mplew.getPacket();
    }
}
