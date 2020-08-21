package kryonetty.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import kryonetty.network.Endpoint;

public class Initializer extends ChannelInitializer<SocketChannel> {
    private final Codec codec;
    private final Endpoint endpoint;

    public Initializer(Codec codec, Endpoint endpoint) {
        this.codec = codec;
        this.endpoint = endpoint;
    }

    @Override
    public void initChannel(SocketChannel channel) {
        channel.pipeline()
                .addLast(new Decoder(codec))
                .addLast(new Encoder(codec))
                .addLast(new EndpointHandler(endpoint));
    }
}
