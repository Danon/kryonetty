
package kryonetty.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import kryonetty.netty.Codec;
import kryonetty.netty.Initializer;

import java.net.SocketAddress;

import static io.netty.channel.ChannelOption.TCP_NODELAY;

public class Client {
    private final Codec codec;
    private final Endpoint endpoint;

    private Channel channel;

    public Client(Codec codec, Endpoint endpoint) {
        this.codec = codec;
        this.endpoint = endpoint;
    }

    public void connect(SocketAddress address) {
        channel = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(TCP_NODELAY, true)
                .handler(new Initializer(codec, endpoint))
                .connect(address)
                .channel();
    }

    public void send(Object obj) {
        channel.writeAndFlush(obj);
    }

    public void close() {
        channel.close();
    }
}
