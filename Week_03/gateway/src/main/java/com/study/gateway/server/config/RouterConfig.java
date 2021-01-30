package com.study.gateway.server.config;

import com.study.gateway.server.util.ConfigParser;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


//这里可以考虑定时重数据库，或者配置中心加载，这样可以不用停服务，动态的扩展
public class RouterConfig {

    private RouterConfig(){}

    private static RouterConfig routerConfig = new RouterConfig();

    public static RouterConfig getInstance(){
        return routerConfig;
    }

    public void init(){
        try {
            ConfigParser.parseRouterConfig();
        }catch (Exception e){
            throw new RuntimeException("load router config failed");
        }

    }


    private Map<String,Set<String>> serverMap = new ConcurrentHashMap<>();

    public Map<String, Set<String>> getServerMap() {
        return serverMap;
    }

    public boolean containsServerKey(String serverKey){
        return serverMap.containsKey(serverKey);
    }

    public void updateConfig(String key,Set<String> urls) {
        serverMap.put(key,urls);
    }

}

