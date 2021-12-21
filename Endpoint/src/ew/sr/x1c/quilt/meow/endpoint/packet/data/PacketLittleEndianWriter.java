package ew.sr.x1c.quilt.meow.endpoint.packet.data;

import ew.sr.x1c.quilt.meow.endpoint.constant.EndpointConstant;
import ew.sr.x1c.quilt.meow.endpoint.util.HexUtil;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;

public final class PacketLittleEndianWriter {

    private final ByteArrayOutputStream baos;
    private static final Charset ASCII = Charset.forName(EndpointConstant.DEFAULT_CHARSET);

    public static int getlength(String str) {
        byte[] b = str.getBytes(Charset.forName(EndpointConstant.DEFAULT_CHARSET));
        return b.length;
    }

    public PacketLittleEndianWriter() {
        this(32);
    }

    public PacketLittleEndianWriter(int size) {
        baos = new ByteArrayOutputStream(size);
    }

    public byte[] getPacket() {
        return baos.toByteArray();
    }

    public int getLength() {
        return baos.size();
    }

    @Override
    public String toString() {
        return HexUtil.toString(baos.toByteArray());
    }

    public void writeZeroByte(int count) {
        for (int i = 0; i < count; ++i) {
            baos.write((byte) 0);
        }
    }

    public void write(byte[] b) {
        for (int i = 0; i < b.length; ++i) {
            baos.write(b[i]);
        }
    }

    public void write(boolean bool) {
        baos.write(bool ? 1 : 0);
    }

    public void write(byte b) {
        baos.write(b);
    }

    public void write(int b) {
        baos.write((byte) b);
    }

    public void writeByte(byte b) {
        baos.write(b);
    }

    public void writeShort(int s) {
        baos.write((byte) (s & 0xFF));
        baos.write((byte) ((s >>> 8) & 0xFF));
    }

    public void writeInt(int i) {
        baos.write((byte) (i & 0xFF));
        baos.write((byte) ((i >>> 8) & 0xFF));
        baos.write((byte) ((i >>> 16) & 0xFF));
        baos.write((byte) ((i >>> 24) & 0xFF));
    }

    public void writeAsciiString(String str) {
        write(str.getBytes(ASCII));
    }

    public void writeAsciiString(String str, int max) {
        if (getlength(str) > max) {
            str = str.substring(0, max);
        }
        write(str.getBytes(ASCII));
        for (int i = getlength(str); i < max; ++i) {
            write(0);
        }
    }

    public void writeLengthAsciiString(String str) {
        writeShort((short) getlength(str));
        writeAsciiString(str);
    }

    public void writeLengthAsciiString(String str, int max) {
        if (getlength(str) > max) {
            str = str.substring(0, max);
        }
        writeShort((short) getlength(str));
        write(str.getBytes(ASCII));
        for (int i = getlength(str); i < max; ++i) {
            write(0);
        }
    }

    public void writePos(Point pos) {
        writeShort(pos.x);
        writeShort(pos.y);
    }

    public void writeRect(Rectangle rect) {
        writeInt(rect.x);
        writeInt(rect.y);
        writeInt(rect.x + rect.width);
        writeInt(rect.y + rect.height);
    }

    public void writeLong(long l) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(l);
        write(buffer.array());
    }

    public void writeReverseLong(long l) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(Long.reverseBytes(l));
        write(buffer.array());
    }

    @Deprecated
    public void writeLongLegacy(long l) {
        baos.write((byte) (l & 0xFF));
        baos.write((byte) ((l >>> 8) & 0xFF));
        baos.write((byte) ((l >>> 16) & 0xFF));
        baos.write((byte) ((l >>> 24) & 0xFF));
        baos.write((byte) ((l >>> 32) & 0xFF));
        baos.write((byte) ((l >>> 40) & 0xFF));
        baos.write((byte) ((l >>> 48) & 0xFF));
        baos.write((byte) ((l >>> 56) & 0xFF));
    }

    @Deprecated
    public void writeReverseLongLegacy(long l) {
        baos.write((byte) ((l >>> 32) & 0xFF));
        baos.write((byte) ((l >>> 40) & 0xFF));
        baos.write((byte) ((l >>> 48) & 0xFF));
        baos.write((byte) ((l >>> 56) & 0xFF));
        baos.write((byte) (l & 0xFF));
        baos.write((byte) ((l >>> 8) & 0xFF));
        baos.write((byte) ((l >>> 16) & 0xFF));
        baos.write((byte) ((l >>> 24) & 0xFF));
    }

    public byte[] subByteArray(int length) {
        byte[] array = baos.toByteArray();
        byte[] ret = new byte[length];
        for (int i = 0; i < length; ++i) {
            ret[i] = array[i];
        }
        return ret;
    }
}
