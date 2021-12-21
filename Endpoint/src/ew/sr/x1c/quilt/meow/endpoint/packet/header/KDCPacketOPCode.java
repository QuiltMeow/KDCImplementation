package ew.sr.x1c.quilt.meow.endpoint.packet.header;

public enum KDCPacketOPCode {

    REGISTER(0x00),
    LOGIN(0x01),
    PUBLIC_KEY_UPDATE(0x02),
    PUBLIC_KEY_GET(0x03),
    KDC_PUBLIC_kEY(0x04);

    private final int header;

    public int getHeader() {
        return header;
    }

    private KDCPacketOPCode(int header) {
        this.header = header;
    }

    public static KDCPacketOPCode getOPCode(short header) {
        for (KDCPacketOPCode type : values()) {
            if (type.getHeader() == header) {
                return type;
            }
        }
        return null;
    }
}
