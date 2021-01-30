package com.study.gateway.server.filter;

import io.netty.handler.codec.http.FullHttpResponse;

public interface ResponseFilter {
    void filter(FullHttpResponse response);
}
