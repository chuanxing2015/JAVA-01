package com.study.gateway.server.router;


public interface Router<T,R> {
    R route(T request);
}
