package com.study.gateway;

import com.study.gateway.server.GatewayServer;

public class GatewayApplication {

    public static void main (String[] args){
        //开启网关服务
        GatewayServer server = new GatewayServer();
        server.run();
    }
}
