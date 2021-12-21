package ew.sr.x1c.quilt.meow.endpoint.packet.data;

import ew.sr.x1c.quilt.meow.endpoint.MainGUI;
import ew.sr.x1c.quilt.meow.endpoint.constant.EndpointConstant;
import java.awt.Point;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.logging.Level;

public final class LittleEndianAccessor {

    private final ByteArrayByteStream bs;

    public LittleEndianAccessor(final ByteArrayByteStream bs) {
        this.bs = bs;
    }

    public byte readByte() {
        return (byte) bs.readByte();
    }

    public int readInt() {
        int byte1 = bs.readByte();
        int byte2 = bs.readByte();
        int byte3 = bs.readByte();
        int byte4 = bs.readByte();
        return (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1;
    }

    public int getLength() {
        return bs.getLength();
    }

    public short readShort() {
        int byte1 = bs.readByte();
        int byte2 = bs.readByte();
        return (short) ((byte2 << 8) + byte1);
    }

    public int readUnsignShort() {
        int ret = readShort();
        if (ret < 0) {
            ret += 65536;
        }
        return ret;
    }

    @Deprecated
    public long readLongLegacy() {
        int byte1 = bs.readByte();
        int byte2 = bs.readByte();
        int byte3 = bs.readByte();
        int byte4 = bs.readByte();
        long byte5 = bs.readByte();
        long byte6 = bs.readByte();
        long byte7 = bs.readByte();
        long byte8 = bs.readByte();
        return (long) ((byte8 << 56) + (byte7 << 48) + (byte6 << 40) + (byte5 << 32) + (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1);
    }

    public long readLong() {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(read(Long.BYTES));
        buffer.flip();
        return buffer.getLong();
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public String readAsciiString(int length) {
        byte ret[] = new byte[length];
        for (int i = 0; i < length; ++i) {
            ret[i] = (byte) readByte();
        }
        return new String(ret, Charset.forName(EndpointConstant.DEFAULT_CHARSET));
    }

    public long getByteRead() {
        return bs.getByteRead();
    }

    public long getPosition() {
        return bs.getPosition();
    }

    public String readLengthAsciiString() {
        return readAsciiString(readShort());
    }

    public Point readPos() {
        int x = readShort();
        int y = readShort();
        return new Point(x, y);
    }

    public byte[] read(int length) {
        byte[] ret = new byte[length];
        for (int i = 0; i < length; ++i) {
            ret[i] = readByte();
        }
        return ret;
    }

    public void unReadByte() {
        bs.unReadByte();
    }

    public void unReadInt() {
        for (int byte_ = 0; byte_ < 4; ++byte_) {
            bs.unReadByte();
        }
    }

    public void unReadShort() {
        for (int byte_ = 0; byte_ < 2; ++byte_) {
            bs.unReadByte();
        }
    }

    public void unReadLong() {
        for (int byte_ = 0; byte_ < 8; ++byte_) {
            bs.unReadByte();
        }
    }

    public void unReadAsciiString(int length) {
        for (int byte_ = 0; byte_ < length; ++byte_) {
            bs.unReadByte();
        }
    }

    public void unReadPos() {
        for (int byte_ = 0; byte_ < 4; ++byte_) {
            bs.unReadByte();
        }
    }

    public void unRead(int length) {
        for (int byte_ = 0; byte_ < length; ++byte_) {
            bs.unReadByte();
        }
    }

    public byte readLastByte() {
        return (byte) bs.readLastByte();
    }

    public int readLastInt() {
        unReadInt();
        int byte1 = bs.readByte();
        int byte2 = bs.readByte();
        int byte3 = bs.readByte();
        int byte4 = bs.readByte();
        return (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1;
    }

    public short readLastShort() {
        unReadShort();
        int byte1 = bs.readByte();
        int byte2 = bs.readByte();
        return (short) ((byte2 << 8) + byte1);
    }

    public long readLastLong() {
        unReadLong();
        return readLong();
    }

    public String readLastAsciiString(int length) {
        for (int i = 0; i < length; ++i) {
            unReadByte();
        }
        byte ret[] = new byte[length];
        for (int i = 0; i < length; ++i) {
            ret[i] = (byte) readByte();
        }
        return new String(ret, Charset.forName(EndpointConstant.DEFAULT_CHARSET));
    }

    public Point readLastPos() {
        unReadInt();
        short x = readShort();
        short y = readShort();
        return new Point(x, y);
    }

    public byte[] readLastByte(int length) {
        for (int byte_ = 0; byte_ < length; ++byte_) {
            bs.unReadByte();
        }
        byte[] ret = new byte[length];
        for (int i = 0; i < length; ++i) {
            ret[i] = readByte();
        }
        return ret;
    }

    public long available() {
        return bs.available();
    }

    @Override
    public String toString() {
        return bs.toString();
    }

    public String toString(boolean b) {
        return bs.toString(b);
    }

    public void seek(long offset) {
        try {
            bs.seek(offset);
        } catch (IOException ex) {
            MainGUI.getInstance().getLogger().log(Level.WARNING, "找尋失敗", ex);
        }
    }

    public void skip(int length) {
        seek(getPosition() + length);
    }

    public byte getByte(int position) {
        return (byte) bs.getByte(position);
    }

    public short getShort(int position) {
        int byte1 = bs.getByte(position);
        int byte2 = bs.getByte(position + 1);
        return (short) ((byte2 << 8) + byte1);
    }

    public int getInt(int position) {
        int byte1 = bs.getByte(position);
        int byte2 = bs.getByte(position + 1);
        int byte3 = bs.getByte(position + 2);
        int byte4 = bs.getByte(position + 3);
        return (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1;
    }

    public long getLong(int position) {
        byte[] read = new byte[Long.BYTES];
        for (int i = 0; i < Long.BYTES; ++i) {
            read[i] = (byte) bs.getByte(position + i);
        }
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(read);
        buffer.flip();
        return buffer.getLong();
    }

    public String getAsciiString(int position, int size) {
        byte ret[] = new byte[size];
        for (int i = 0; i < size; ++i) {
            ret[i] = (byte) getByte(position + i);
        }
        return new String(ret, Charset.forName(EndpointConstant.DEFAULT_CHARSET));
    }

    public String getLengthAsciiString(int position) {
        short length = getShort(position);
        return getAsciiString(position + 2, length);
    }

    public Point getPos(int position) {
        short x = getShort(position);
        short y = getShort(position + 2);
        return new Point(x, y);
    }

    public byte[] getByte(int position, int length) {
        byte[] ret = new byte[length];
        for (int i = 0; i < length; ++i) {
            ret[i] = getByte(position + i);
        }
        return ret;
    }

    public byte[] getPacket() {
        byte[] ret = new byte[bs.getLength()];
        for (int i = 0; i < bs.getLength(); ++i) {
            ret[i] = (byte) bs.getByte(i);
        }
        return ret;
    }

    public byte[] subByteArray(int position) {
        byte[] ret = new byte[bs.getLength() - position];
        for (int i = position; i < bs.getLength(); ++i) {
            ret[i - position] = (byte) bs.getByte(i);
        }
        return ret;
    }
}
