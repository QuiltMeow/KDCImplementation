package ew.sr.x1c.quilt.meow.endpoint.packet.data;

import ew.sr.x1c.quilt.meow.endpoint.util.HexUtil;
import java.io.IOException;

public final class ByteArrayByteStream {

    private int pos;
    private long byteRead;
    private final byte[] array;

    public ByteArrayByteStream(byte[] array) {
        this.array = array;
    }

    public int getByte(int position) {
        return ((int) array[position]) & 0xFF;
    }

    public long getPosition() {
        return pos;
    }

    public void seek(long offset) throws IOException {
        if (offset < 0 || offset > array.length) {
            throw new IOException();
        }
        pos = (int) offset;
    }

    public int getLength() {
        return array.length;
    }

    public long getByteRead() {
        return byteRead;
    }

    public int readByte() {
        ++byteRead;
        return ((int) array[pos++]) & 0xFF;
    }

    public void unReadByte() {
        if (pos - 1 < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        --pos;
    }

    public int readLastByte() {
        return ((int) array[pos]) & 0xFF;
    }

    public int[] readLastByte(int byteCount) {
        while (pos - byteCount < 1) {
            --byteCount;
        }
        int[] a = null;
        int b = 0;
        while (byteCount > 0) {
            a[b] += ((int) array[pos - byteCount]);
            --byteCount;
            ++b;
        }
        return a;
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean all) {
        String nowData = "";
        if (array.length - pos > 0) {
            byte[] now = new byte[array.length - pos];
            System.arraycopy(array, pos, now, 0, array.length - pos);
            nowData = HexUtil.toString(now);
        }
        if (all) {
            return "全部 : " + HexUtil.toString(array) + "\r\n目前 : " + nowData;
        } else {
            return "資料 : " + nowData;
        }
    }

    public long available() {
        return array.length - pos;
    }
}
