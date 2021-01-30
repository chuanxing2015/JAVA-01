package com.study.gateway.server.config;

public class ServerConfig {


    private int port;
    private int bossSize;
    private int workerSize;
    private int gatewayPoolSize;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getBossSize() {
        return bossSize;
    }

    public void setBossSize(int bossSize) {
        this.bossSize = bossSize;
    }

    public int getWorkerSize() {
        return workerSize;
    }

    public void setWorkerSize(int workerSize) {
        this.workerSize = workerSize;
    }

    public int getGatewayPoolSize() {
        return gatewayPoolSize;
    }

    public void setGatewayPoolSize(int gatewayPoolSize) {
        this.gatewayPoolSize = gatewayPoolSize;
    }

}
