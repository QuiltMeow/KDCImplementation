package ew.sr.x1c.quilt.meow.endpoint.client.kdc;

import ew.sr.x1c.quilt.meow.endpoint.Connection;
import ew.sr.x1c.quilt.meow.endpoint.constant.EndpointConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import java.nio.ByteOrder;
import lombok.Getter;

public final class KDCClient implements Connection {

    @Getter
    private final String host;

    @Getter
    private final int port;

    @Getter
    private final KDCSession kdcSession;

    private Bootstrap boot;
    private final EventLoopGroup group;
    private Channel channel;

    public KDCClient(String host, int port, KDCSession kdcSession) {
        group = new NioEventLoopGroup();
        this.host = host;
        this.port = port;
        this.kdcSession = kdcSession;
    }

    @Override
    public void start() {
        try {
            boot = new Bootstrap().group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        public void initChannel(SocketChannel channel) {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast("idle", new IdleStateHandler(EndpointConstant.CLIENT_WAIT_TIMEOUT, 0, 0));
                            pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, Integer.MAX_VALUE, 0, EndpointConstant.DEFAULT_PACKET_LENGTH_BYTE, 0, EndpointConstant.DEFAULT_PACKET_LENGTH_BYTE, true));
                            pipeline.addLast("frameEncoder", new LengthFieldPrepender(ByteOrder.LITTLE_ENDIAN, EndpointConstant.DEFAULT_PACKET_LENGTH_BYTE, 0, false));
                            pipeline.addLast("decoder", new ByteArrayDecoder());
                            pipeline.addLast("encoder", new ByteArrayEncoder());
                            pipeline.addLast("handler", new KDCClientHandler(kdcSession));
                        }
                    });

            channel = boot.connect(host, port).sync().channel().closeFuture().channel();
        } catch (Exception ex) {
            throw new RuntimeException("與 KDC 伺服器建立連線失敗", ex);
        }
    }

    @Override
    public void stop() {
        if (channel != null) {
            channel.close();
        }
        group.shutdownGracefully();
    }
}
