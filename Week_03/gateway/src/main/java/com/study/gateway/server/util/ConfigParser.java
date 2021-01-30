package com.study.gateway.server.util;

import com.study.gateway.server.config.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ConfigParser {

    public static ServerConfig parseServerConfig() throws IOException {
        InputStream stream = ConfigParser.class.getClassLoader().getResourceAsStream("server-config.properties");
        Properties properties = new Properties();
        properties.load(stream);
        ServerConfig config = new ServerConfig();
        config.setBossSize(Integer.parseInt(properties.getProperty("boss_size","1")));
        config.setWorkerSize(Integer.parseInt(properties.getProperty("worker_size","16")));
        config.setGatewayPoolSize(Integer.parseInt(properties.getProperty("gateway_pool_size","8")));
        config.setPort(Integer.parseInt(properties.getProperty("port","8801")));
        return config;
    }



    public static void parseRouterConfig() throws IOException {
        RouterConfig config = RouterConfig.getInstance();
        InputStream stream = ConfigParser.class.getClassLoader().getResourceAsStream("router-config.properties");
        Properties properties = new Properties();
        properties.load(stream);
        Set<Map.Entry<Object,Object>> set = properties.entrySet();
        for(Map.Entry<Object,Object> entry :set){
            String key = String.valueOf(entry.getKey());
            String value = String.valueOf(entry.getValue());
            //这里应该校验配置的url是否合法
            String[] urls = value.split(";");
            Set<String> sets = new HashSet<>();
            sets.addAll(Arrays.asList(urls));
            config.updateConfig(key,sets);
        }
    }

}
