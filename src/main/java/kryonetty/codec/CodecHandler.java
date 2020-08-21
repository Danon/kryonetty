package kryonetty.codec;

import kryonetty.KryoPool;
import kryonetty.net.Endpoint;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class CodecHandler extends ChannelInitializer<SocketChannel>
{
    private final KryoPool kryoPool;
    private final Endpoint endpoint;

    public CodecHandler(KryoPool kryoPool, Endpoint endpoint) {
        this.kryoPool = kryoPool;
        this.endpoint = endpoint;
    }

    @Override
    public void initChannel(SocketChannel channel) {
        channel.pipeline()
                .addLast(new KryoDecoder(kryoPool))
                .addLast(new KryoEncoder(kryoPool))
                .addLast(new EndpointHandler(endpoint));
    }
}
