
package kryonetty.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import kryonetty.netty.Codec;
import kryonetty.netty.Initializer;

import java.net.InetSocketAddress;

import static io.netty.channel.ChannelOption.SO_REUSEADDR;
import static io.netty.channel.ChannelOption.TCP_NODELAY;

public class Server {
    private final Codec codec;
    private final Endpoint endpoint;

    private Channel channel;

    public Server(Codec codec, Endpoint endpoint) {
        this.codec = codec;
        this.endpoint = endpoint;
    }

    public void start(int port) {
        channel = new ServerBootstrap()
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new Initializer(codec, endpoint))
                .childOption(TCP_NODELAY, true)
                .childOption(SO_REUSEADDR, true)
                .bind(new InetSocketAddress(port))
                .channel();
    }

    public void close() {
        channel.close();
    }
}
