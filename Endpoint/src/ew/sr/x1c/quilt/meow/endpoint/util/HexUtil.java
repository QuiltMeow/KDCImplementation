package ew.sr.x1c.quilt.meow.endpoint.util;

import ew.sr.x1c.quilt.meow.endpoint.constant.EndpointConstant;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class HexUtil {

    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String toString(byte byteValue) {
        int temp = byteValue << 8;
        char[] retStr = new char[]{HEX[(temp >> 12) & 0x0F], HEX[(temp >> 8) & 0x0F]};
        return String.valueOf(retStr);
    }

    public static String toString(int intValue) {
        return Integer.toHexString(intValue);
    }

    public static String toString(byte[] byteData) {
        StringBuilder hexData = new StringBuilder();
        for (int i = 0; i < byteData.length; ++i) {
            hexData.append(toString(byteData[i]));
            hexData.append(' ');
        }
        return hexData.substring(0, hexData.length() - 1);
    }

    public static String toMacString(byte[] byteData) {
        StringBuilder hexData = new StringBuilder();
        for (int i = 0; i < byteData.length; ++i) {
            hexData.append(toString(byteData[i]));
            hexData.append('-');
        }
        return hexData.substring(0, hexData.length() - 1);
    }

    public static String toStringFromAscii(byte[] byteData) {
        byte[] ret = new byte[byteData.length];
        for (int i = 0; i < byteData.length; ++i) {
            if (byteData[i] < 32 && byteData[i] >= 0) {
                ret[i] = '.';
            } else {
                int charData = ((short) byteData[i]) & 0xFF;
                ret[i] = (byte) charData;
            }
        }
        try {
            return new String(ret, EndpointConstant.DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException ex) {
        }
        return "";
    }

    public static byte[] getByteArrayFromHexString(String hex) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int iNext = 0;
        int bNext = 0;
        boolean highOC = true;
        outer:
        for (;;) {
            int number = -1;
            while (number == -1) {
                if (iNext == hex.length()) {
                    break outer;
                }
                char character = hex.charAt(iNext);
                if (character >= '0' && character <= '9') {
                    number = character - '0';
                } else if (character >= 'a' && character <= 'f') {
                    number = character - 'a' + 10;
                } else if (character >= 'A' && character <= 'F') {
                    number = character - 'A' + 10;
                } else {
                    number = -1;
                }
                ++iNext;
            }
            if (highOC) {
                bNext = number << 4;
                highOC = false;
            } else {
                bNext |= number;
                highOC = true;
                baos.write(bNext);
            }
        }
        return baos.toByteArray();
    }

    public static String getOPCodeToString(int op) {
        return "0x" + Integer.toHexString(op).toUpperCase();
    }

    public static String toHex(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); ++i) {
            sb.append(Integer.toHexString(input.charAt(i)));
        }
        return sb.toString().toUpperCase();
    }

    public static String toOctal(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); ++i) {
            sb.append(Integer.toOctalString(input.charAt(i)));
        }
        return sb.toString();
    }

    public static String toBinary(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); ++i) {
            sb.append(Integer.toBinaryString(input.charAt(i)));
        }
        return sb.toString();
    }
}
