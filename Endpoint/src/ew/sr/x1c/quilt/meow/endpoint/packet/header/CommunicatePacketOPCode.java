package ew.sr.x1c.quilt.meow.endpoint.packet.header;

public enum CommunicatePacketOPCode {

    CONNECTION_CREATE(0x00),
    VERIFY(0x01),
    MESSAGE(0x10),
    FILE(0x11);

    private final int header;

    public int getHeader() {
        return header;
    }

    private CommunicatePacketOPCode(int header) {
        this.header = header;
    }

    public static CommunicatePacketOPCode getOPCode(short header) {
        for (CommunicatePacketOPCode type : values()) {
            if (type.getHeader() == header) {
                return type;
            }
        }
        return null;
    }
}
