package kryonetty.codec;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import kryonetty.KryoPool;

import java.io.ByteArrayOutputStream;

public class KryoEncoder extends MessageToByteEncoder<Object>
{
    private final KryoPool holder;

    public KryoEncoder(KryoPool holder) {
        this.holder = holder;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object object, ByteBuf buffer) {
        Kryo kryo = holder.getKryo();
        Output output = holder.getOutput();
        encodeAndWrite(object, buffer, kryo, output);
        holder.free(output);
        holder.free(kryo);
    }

    private void encodeAndWrite(Object object, ByteBuf buffer, Kryo kryo, Output output) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        output.setOutputStream(outStream);

        kryo.writeClassAndObject(output, object);
        output.flush();
        output.close();

        byte[] outArray = outStream.toByteArray();
        buffer.writeShort(outArray.length);
        buffer.writeBytes(outArray);
    }
}
