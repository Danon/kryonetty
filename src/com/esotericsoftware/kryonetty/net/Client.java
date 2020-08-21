
package com.esotericsoftware.kryonetty.net;

import com.esotericsoftware.kryonetty.KryoPool;
import com.esotericsoftware.kryonetty.codec.CodecHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.SocketAddress;

import static io.netty.channel.ChannelOption.TCP_NODELAY;

public class Client
{
    private final KryoPool holder;
    private final Endpoint endpoint;

    private Channel channel;

    public Client(KryoPool holder, Endpoint endpoint) {
        this.holder = holder;
        this.endpoint = endpoint;
    }

    public void connect(SocketAddress serverAddress) {
        channel = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(TCP_NODELAY, true)
                .handler(new CodecHandler(holder, endpoint))
                .connect(serverAddress)
                .channel();
    }

    public void send(Object obj) {
        channel.writeAndFlush(obj);
    }

    public void close() {
        channel.close();
    }
}
