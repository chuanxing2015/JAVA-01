package com.study.gateway.server;

import com.study.gateway.server.util.ConfigParser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class CommonPool {

    private static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private static ExecutorService service = Executors.newFixedThreadPool(getPoolSize(), new NameThreadFactory());


    public static ExecutorService getPool(){
        return service;
    }


    static class NameThreadFactory implements ThreadFactory {

        AtomicInteger count = new AtomicInteger();
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,"gateway-pool-" + count.getAndIncrement());
        }
    }

    private static int getPoolSize(){
        try {
            return ConfigParser.parseServerConfig().getGatewayPoolSize();
        }catch (Exception e){
            System.out.println("get pool size failed");
        }
        return DEFAULT_POOL_SIZE;
    }

}
