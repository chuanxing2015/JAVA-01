package com.nio.study.server.filter;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.internal.StringUtil;

public class HttpRequestHeaderFilter implements RequestFilter{

    @Override
    public void filter(FullHttpRequest request, ChannelHandlerContext context) {
        String uid = request.headers().get("uid");
        if(StringUtil.isNullOrEmpty(uid)){
            String value = "{\"status\":-1,\"message\":\"request header not contain uid\"}";
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(value.getBytes()));
            response.headers().set("Content-Type","application/json");
            response.headers().set("Content-Length",response.content().readableBytes());
            if(!HttpUtil.isKeepAlive(request)){
                context.write(response).addListener(ChannelFutureListener.CLOSE) ;
            }else {
                context.write(response);
            }
        }
    }
}
