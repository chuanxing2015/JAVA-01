package com.study.gateway.server.filter;

import com.study.gateway.server.config.RouterConfig;
import com.study.gateway.server.excption.GatewayException;
import com.study.gateway.server.util.UriUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

public class HttpRequestHeaderFilter implements RequestFilter{

    @Override
    public void filter(FullHttpRequest request, ChannelHandlerContext context) throws GatewayException{

        String serverKey = UriUtil.getServiceKey(request.uri());
        boolean cons = RouterConfig.getInstance().containsServerKey(serverKey);
        System.out.println("serverKey is : " + serverKey + " found : " + cons);
        if(!cons){
            throw new GatewayException(404,"Resource Not Found");
        }
    }
}
