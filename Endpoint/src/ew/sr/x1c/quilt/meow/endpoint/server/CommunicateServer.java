package ew.sr.x1c.quilt.meow.endpoint.server;

import ew.sr.x1c.quilt.meow.endpoint.constant.EndpointConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import java.nio.ByteOrder;
import lombok.Getter;

public final class CommunicateServer {

    @Getter
    private final int port;

    private ServerBootstrap boot;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private Channel channel;

    public CommunicateServer(int port) {
        this.port = port;
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
    }

    public void start() {
        try {
            boot = new ServerBootstrap().group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, EndpointConstant.USER_LIMIT)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast("idle", new IdleStateHandler(EndpointConstant.SERVER_WAIT_TIMEOUT, 0, 0));
                            pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, Integer.MAX_VALUE, 0, EndpointConstant.DEFAULT_PACKET_LENGTH_BYTE, 0, EndpointConstant.DEFAULT_PACKET_LENGTH_BYTE, true));
                            pipeline.addLast("frameEncoder", new LengthFieldPrepender(ByteOrder.LITTLE_ENDIAN, EndpointConstant.DEFAULT_PACKET_LENGTH_BYTE, 0, false));
                            pipeline.addLast("decoder", new ByteArrayDecoder());
                            pipeline.addLast("encoder", new ByteArrayEncoder());
                            pipeline.addLast("handler", new CommunicateServerHandler());
                        }
                    });

            channel = boot.bind(port).sync().channel().closeFuture().channel();
        } catch (Exception ex) {
            throw new RuntimeException("伺服器啟動失敗", ex);
        }
    }

    public void stop() {
        if (channel != null) {
            channel.close();
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
