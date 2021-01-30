package com.study.gateway.server.filter;

import io.netty.handler.codec.http.FullHttpResponse;

public class ResponseHeaderFilter implements ResponseFilter {

    @Override
    public void filter(FullHttpResponse response) {
        response.headers().add("soul","mao");
        response.headers().set("Content-Type","application/json;charset=UTF-8");
    }
}
