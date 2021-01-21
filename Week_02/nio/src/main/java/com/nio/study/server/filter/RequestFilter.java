package com.nio.study.server.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface RequestFilter {
    void filter(FullHttpRequest request, ChannelHandlerContext context);
}
