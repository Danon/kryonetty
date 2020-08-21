package kryonetty;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;

import java.util.function.Supplier;

import static java.util.Arrays.stream;

public class KryoPool
{
    private final Pool<Kryo> kryoPool;
    private final Pool<Input> inputPool;
    private final Pool<Output> outputPool;

    public KryoPool(Class... classes) {
        kryoPool = pool(() -> {
            Kryo kryo = new Kryo();
            kryo.setRegistrationRequired(true);
            kryo.setReferences(false);
            stream(classes).forEach(kryo::register);
            return kryo;
        });
        inputPool = pool(() -> new Input(2048));
        outputPool = pool(() -> new Output(2048));
    }

    public Kryo getKryo() {
        return kryoPool.obtain();
    }

    public Input getInput() {
        return inputPool.obtain();
    }

    public Output getOutput() {
        return outputPool.obtain();
    }

    public void free(Kryo kryo) {
        kryoPool.free(kryo);
    }

    public void free(Input input) {
        inputPool.free(input);
    }

    public void free(Output output) {
        outputPool.free(output);
    }

    private static <T> Pool<T> pool(Supplier<T> supplier) {
        return new Pool<T>(true, true)
        {
            @Override
            protected T create() {
                return supplier.get();
            }
        };
    }
}
