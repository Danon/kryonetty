
package com.esotericsoftware.kryonetty.net;

import com.esotericsoftware.kryonetty.Request;
import io.netty.channel.ChannelHandlerContext;

public interface Endpoint
{
    void connected(ChannelHandlerContext ctx);

    void disconnected(ChannelHandlerContext ctx);

    void received(ChannelHandlerContext ctx, Request object);
}
