package kryonetty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class Decoder extends ByteToMessageDecoder {
    private final Codec codec;

    public Decoder(Codec codec) {
        this.codec = codec;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
        if (buffer.readableBytes() < 2) {
            return;
        }
        buffer.markReaderIndex();
        int len = buffer.readUnsignedShort();
        if (buffer.readableBytes() < len) {
            buffer.resetReaderIndex();
            return;
        }
        out.add(codec.deserialize(toBytes(buffer, len)));
    }

    private byte[] toBytes(ByteBuf buffer, int len) {
        byte[] bytes = new byte[len];
        buffer.readBytes(bytes);
        return bytes;
    }
}
