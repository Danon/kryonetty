package kryonetty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Encoder extends MessageToByteEncoder<Object> {
    private final Codec codec;

    public Encoder(Codec codec) {
        this.codec = codec;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object object, ByteBuf buffer) {
        byte[] bytes = codec.serialize(object);
        buffer.writeShort(bytes.length);
        buffer.writeBytes(bytes);
    }
}
