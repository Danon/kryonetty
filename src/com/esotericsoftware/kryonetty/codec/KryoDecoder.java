package com.esotericsoftware.kryonetty.codec;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryonetty.KryoPool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.util.List;

public class KryoDecoder extends ByteToMessageDecoder
{
    private final KryoPool holder;

    public KryoDecoder(KryoPool holder) {
        this.holder = holder;
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

        out.add(decode(buffer, len));
    }

    private Object decode(ByteBuf buffer, int len) {
        byte[] buf = new byte[len];
        buffer.readBytes(buf);
        Kryo kryo = holder.getKryo();
        Input input = holder.getInput();
        input.setInputStream(new ByteArrayInputStream(buf));
        Object object = kryo.readClassAndObject(input);
        input.close();
        holder.free(kryo);
        holder.free(input);
        return object;
    }
}
