package kryonetty;

import io.netty.channel.ChannelHandlerContext;
import kryonetty.kryo.KryoCodec;
import kryonetty.kryo.KryoPool;
import kryonetty.netty.Codec;
import kryonetty.network.Client;
import kryonetty.network.Endpoint;
import kryonetty.network.Server;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleTest {
    private Server server;
    private Client client;

    @Test
    public void shouldSendAndReceive() throws InterruptedException {
        // given
        client = new Client(codec(), clientListener(() -> client.send(new TestRequest("Bwuk!"))));
        server = new Server(codec(), serverListener(request -> {
            // then
            server.close();
            client.close();
            assertEquals("Bwuk!", request.someText);

            synchronized (this) {
                notify();
            }
        }));

        // start
        server.start(54321);
        client.connect(new InetSocketAddress(54321));

        // wait
        synchronized (this) {
            wait();
        }
    }

    private Endpoint clientListener(Runnable onConnect) {
        return new Endpoint() {
            public void connected(ChannelHandlerContext ctx) {
                System.out.println("Client: Connected to server: " + ctx.channel().remoteAddress());
                onConnect.run();
            }

            public void disconnected(ChannelHandlerContext ctx) {
                System.out.println("Client: Disconnected from server: " + ctx.channel().remoteAddress());
            }

            public void received(ChannelHandlerContext ctx, Request object) {
                System.out.println("Client: Received: " + object);
            }
        };
    }

    private Endpoint serverListener(Consumer<TestRequest> received) {
        return new Endpoint() {
            public void connected(ChannelHandlerContext ctx) {
                System.out.println("Server: Client connected: " + ctx.channel().remoteAddress());
                ctx.channel().write("make a programmer rich");
            }

            public void disconnected(ChannelHandlerContext ctx) {
                System.out.println("Server: Client disconnected: " + ctx.channel().remoteAddress());
            }

            public void received(ChannelHandlerContext ctx, Request object) {
                System.out.println("Server: Received: " + object);
                if (object instanceof TestRequest) {
                    received.accept((TestRequest) object);
                }
            }
        };
    }

    private Codec codec() {
        return new KryoCodec(new KryoPool(TestRequest.class));
    }
}
