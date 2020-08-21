package kryonetty.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import kryonetty.netty.Codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class KryoCodec implements Codec {
    private final KryoPool holder;

    public KryoCodec(KryoPool holder) {
        this.holder = holder;
    }

    public byte[] serialize(Object object) {
        Kryo kryo = holder.getKryo();
        Output output = holder.getOutput();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        output.setOutputStream(outStream);
        kryo.writeClassAndObject(output, object);
        output.flush();
        output.close();
        byte[] bytes = outStream.toByteArray();
        holder.free(output);
        holder.free(kryo);
        return bytes;
    }

    public Object deserialize(byte[] bytes) {
        Kryo kryo = holder.getKryo();
        Input input = holder.getInput();
        input.setInputStream(new ByteArrayInputStream(bytes));
        Object object = kryo.readClassAndObject(input);
        input.close();
        holder.free(kryo);
        holder.free(input);
        return object;
    }
}
