package ew.sr.x1c.quilt.meow.endpoint;

import ew.sr.x1c.quilt.meow.endpoint.constant.EndpointConstant;
import ew.sr.x1c.quilt.meow.endpoint.handler.PacketCreator;
import ew.sr.x1c.quilt.meow.endpoint.handler.PublicKeyHandler;
import ew.sr.x1c.quilt.meow.endpoint.packet.data.PacketLittleEndianWriter;
import ew.sr.x1c.quilt.meow.endpoint.schedule.TaskScheduler.ScheduleTimer;
import ew.sr.x1c.quilt.meow.endpoint.util.Randomizer;
import io.netty.util.AttributeKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.spec.IvParameterSpec;
import lombok.Getter;
import lombok.Setter;

public class RemoteUser {

    public static final AttributeKey<RemoteUser> USER_KEY = AttributeKey.valueOf("User");

    @Getter
    private IvParameterSpec iv;

    @Getter
    private final Session session;

    @Getter
    private final byte[] localNonce;

    @Getter
    private byte[] remoteNonce;

    @Getter
    @Setter
    private String remoteName;

    @Getter
    private RSAPublicKey remotePublicKey;

    @Getter
    private byte[] remotePublicKeyByte;

    @Getter
    private String timestamp;

    @Getter
    private final boolean passive;

    @Getter
    private long lastPong, lastPing;

    public RemoteUser(Session session, boolean passive) {
        this.session = session;
        this.passive = passive;
        localNonce = generateNonce();
    }

    public RemoteUser(Session session, boolean passive, String remoteName) {
        this(session, passive);
        this.remoteName = remoteName;
    }

    public void requestPublicKey() {
        timestamp = String.valueOf(System.currentTimeMillis());
        MainGUI.getInstance().getKDCSession().getSession().write(PacketCreator.KDCRequest(remoteName, timestamp));
    }

    public static byte[] generateNonce() {
        byte[] ret = new byte[EndpointConstant.NONCE_SIZE];
        Randomizer.nextByte(ret);
        return ret;
    }

    public void setRemotePublicKey(byte[] key) {
        remotePublicKeyByte = key;
        remotePublicKey = (RSAPublicKey) PublicKeyHandler.loadPublicKey(key);
    }

    public IvParameterSpec getIV() {
        return iv;
    }

    public boolean canCommunicate() {
        return iv != null;
    }

    public void setRemoteNonce(byte[] nonce) {
        if (nonce.length != EndpointConstant.NONCE_SIZE) {
            throw new RuntimeException("Nonce 長度錯誤");
        }
        remoteNonce = nonce;
    }

    public void calculateIV() {
        PacketLittleEndianWriter mplew = new PacketLittleEndianWriter();
        if (passive) {
            mplew.write(remoteNonce);
            mplew.write(localNonce);
        } else {
            mplew.write(localNonce);
            mplew.write(remoteNonce);
        }
        iv = new IvParameterSpec(mplew.getPacket());
    }

    public void disconnect() {
        MainGUI.getInstance().getUserStorage().deregisterUser(remoteName);
    }

    public long getLatency() {
        return lastPong - lastPing;
    }

    public void sendPong() {
        session.write(PacketCreator.pong());
    }

    public void sendPing() {
        session.write(PacketCreator.ping());
        lastPing = System.currentTimeMillis();

        ScheduleTimer.getInstance().schedule(() -> {
            if (getLatency() < 0) {
                disconnect();
                session.close();
            }
        }, EndpointConstant.PING_WAIT);
    }

    public void pongReceive() {
        lastPong = System.currentTimeMillis();
    }
}
