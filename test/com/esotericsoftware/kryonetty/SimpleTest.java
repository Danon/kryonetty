
package com.esotericsoftware.kryonetty;

import com.esotericsoftware.kryonetty.net.Client;
import com.esotericsoftware.kryonetty.net.Endpoint;
import com.esotericsoftware.kryonetty.net.Server;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

public class SimpleTest
{
    private Server server;
    private Client client;

    @Test
    public void shouldSendAndReceive() throws InterruptedException {
        // given
        client = new Client(holder(), clientListener(() -> client.send(new TestRequest("Bwuk!"))));
        server = new Server(holder(), serverListener(request -> {
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
        client.connect(new InetSocketAddress("localhost", 54321));

        // wait
        synchronized (this) {
            wait();
        }
    }

    private Endpoint clientListener(Runnable onConnect) {
        return new Endpoint()
        {
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
        return new Endpoint()
        {
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

    private KryoPool holder() {
        return new KryoPool(TestRequest.class);
    }
}
