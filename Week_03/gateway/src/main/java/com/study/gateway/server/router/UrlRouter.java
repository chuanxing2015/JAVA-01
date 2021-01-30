package com.study.gateway.server.router;

import com.google.gson.JsonObject;
import com.study.gateway.httpclient.MyHttpClient;
import com.study.gateway.server.CommonPool;
import com.study.gateway.server.DefaultResponse;
import com.study.gateway.server.config.RouterConfig;
import com.study.gateway.server.util.UriUtil;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class UrlRouter implements Router<FullHttpRequest,FullHttpResponse>{

    private AtomicInteger atomicInteger = new AtomicInteger();


    //遍历路由
    @Override
    public FullHttpResponse route(FullHttpRequest request) {
        String key = UriUtil.getServiceKey(request.uri());
        Set<String> urls = RouterConfig.getInstance().getServerMap().get(key);

        int size = urls.size();
        if(size == 0){
            return DefaultResponse.getDefaultResponse(500,"node is empty, please check");
        }

        final String node = (String)urls.toArray()[atomicInteger.getAndIncrement()% size];

        String value ;
        Future<String> future = CommonPool.getPool().submit(new Callable<String>() {
            public String call() throws Exception {
                return MyHttpClient.get(node);
            }
        });
        try {
            value = future.get(500, TimeUnit.MILLISECONDS);
        }catch (Exception e){
            System.out.println("request other service failed url : " + node);
            return DefaultResponse.getDefaultResponse(500, e.getMessage());
        }
        return DefaultResponse.getDefaultResponse(value);
    }
}
