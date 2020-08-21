
package kryonetty.network;

import io.netty.channel.ChannelHandlerContext;
import kryonetty.Request;

public interface Endpoint {
    void connected(ChannelHandlerContext ctx);

    void disconnected(ChannelHandlerContext ctx);

    void received(ChannelHandlerContext ctx, Request object);
}
