package ew.sr.x1c.quilt.meow.endpoint.client.kdc;

import ew.sr.x1c.quilt.meow.endpoint.Session;
import lombok.Getter;
import lombok.Setter;

public class KDCSession {

    @Getter
    @Setter
    private Session session;

    @Getter
    private final KDCClient client;

    @Getter
    private final String host;

    @Getter
    private final int port;

    public KDCSession(String host, int port) {
        this.host = host;
        this.port = port;
        client = new KDCClient(host, port, this);
    }

    public void start() {
        client.start();
    }

    public void stop() {
        client.stop();
    }
}
