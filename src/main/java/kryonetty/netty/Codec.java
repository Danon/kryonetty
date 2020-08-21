package kryonetty.netty;

public interface Codec {
    byte[] serialize(Object object);

    Object deserialize(byte[] bytes);
}
