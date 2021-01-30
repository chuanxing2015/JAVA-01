package com.study.gateway.server.filter;

import com.study.gateway.server.excption.GatewayException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public interface RequestFilter {
    void filter(FullHttpRequest request, ChannelHandlerContext context) throws GatewayException;
}
