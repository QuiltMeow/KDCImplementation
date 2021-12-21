package ew.sr.x1c.quilt.meow.kdc.constant;

import javax.crypto.spec.SecretKeySpec;

public class PluginConstant {

    public static final int MAX_ACCOUNT_LENGTH = 32;
    public static final int KEY_LENGTH = 2048;

    public static final int BUFFER_SIZE = 32768;
    public static final String DATABASE = "database.db";
    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final SecretKeySpec AES_KEY = new SecretKeySpec(new byte[]{
        0x08, 0x1C, 0x01, 0x3C, 0x1B, 0x41, 0x6B, 0x61,
        0x74, 0x73, 0x75, 0x6B, 0x69, 0x4A, 0x69, 0x61
    }, "AES");
}
