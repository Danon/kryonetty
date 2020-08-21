package com.esotericsoftware.kryonetty.codec;

import com.esotericsoftware.kryonetty.Request;
import com.esotericsoftware.kryonetty.net.Endpoint;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class EndpointHandler extends SimpleChannelInboundHandler<Request>
{
    private final Endpoint endpoint;

    public EndpointHandler(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        endpoint.connected(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        endpoint.disconnected(ctx);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Request msg) {
        endpoint.received(ctx, msg);
    }
}
