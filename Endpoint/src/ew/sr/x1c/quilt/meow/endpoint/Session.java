package ew.sr.x1c.quilt.meow.endpoint;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import java.net.SocketAddress;

public class Session {

    private final Channel channel;

    public Session(Channel session) {
        channel = session;
    }

    public ChannelFuture write(Object object) {
        return channel.writeAndFlush(object);
    }

    public void close() {
        channel.close();
    }

    public SocketAddress getRemoteAddress() {
        return channel.remoteAddress();
    }

    public boolean isOpen() {
        return channel.isOpen();
    }

    public Channel getChannel() {
        return channel;
    }

    public ChannelFuture writeAndFlush(Object object) {
        return channel.writeAndFlush(object);
    }

    public <T extends Object> Attribute<T> attr(AttributeKey<T> attributeKey) {
        return channel.attr(attributeKey);
    }

    public boolean isActive() {
        return channel.isActive();
    }
}
