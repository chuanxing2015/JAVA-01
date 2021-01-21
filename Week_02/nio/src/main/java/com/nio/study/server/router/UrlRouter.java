package com.nio.study.server.router;

import java.util.concurrent.atomic.AtomicInteger;

public class UrlRouter implements Router<String>{

    private AtomicInteger atomicInteger = new AtomicInteger();

    @Override
    public String route(String[] nodes) {
        int size = nodes.length;
        String node = nodes[atomicInteger.getAndIncrement()% size];
        return node;
    }
}
