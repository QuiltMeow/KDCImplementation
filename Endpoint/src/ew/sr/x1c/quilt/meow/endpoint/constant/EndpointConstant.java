package ew.sr.x1c.quilt.meow.endpoint.constant;

import javax.crypto.spec.SecretKeySpec;

public class EndpointConstant {

    public static final byte EXIT_WITH_ERROR = 1;
    public static final int MAX_ACCOUNT_LENGTH = 32;
    public static final int MAX_MESSAGE_LENGTH = 2000;
    public static final int MAX_FILE_LENGTH = 1000 * 1000 * 1000;

    public static final int IMAGE_RESIZE_WIDTH = 300;
    public static final int IMAGE_RESIZE_HEIGHT = 300;

    public static final int SERVER_WAIT_TIMEOUT = 60;
    public static final int CLIENT_WAIT_TIMEOUT = 90;
    public static final int DEFAULT_PACKET_LENGTH_BYTE = 4;
    public static final int USER_LIMIT = 1000;
    public static final int KEY_LENGTH = 2048;

    public static final short PING_HEADER = 8092;
    public static final short PONG_HEADER = 8093;
    public static final int PING_WAIT = 30000;

    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final int BUFFER_SIZE = 32768;
    public static final int NONCE_SIZE = 8;

    public static final SecretKeySpec AES_KEY = new SecretKeySpec(new byte[]{
        0x08, 0x1C, 0x01, 0x3C, 0x1B, 0x41, 0x6B, 0x61,
        0x74, 0x73, 0x75, 0x6B, 0x69, 0x4A, 0x69, 0x61
    }, "AES");
}
