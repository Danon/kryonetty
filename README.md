# com.esotericsoftware.kryonetty.KryoPool

Since we work with a `Kryo.class`, `Input.class` & `Output.class` kryoPool from `kryo-5.0.0`, classes are passed to the `com.esotericsoftware.kryonetty.KryoPool.class` constructor for registration. 
The `inputBufferSize` (first) and the `outputBufferSize` (second) can also be passed. If the value is `-1` the default value of `2048` is used.
Example from `SimpleTest.class`:
```java
    @Override
    public com.esotericsoftware.kryonetty.KryoPool getKryoHolder() {
        return new com.esotericsoftware.kryonetty.KryoPool(-1, -1, String.class, TestRequest.class);
    }
```
