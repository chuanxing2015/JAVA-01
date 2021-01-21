package com.nio.study;


import com.nio.study.server.NettyServer;

public class Main {
    public static void main(String[] args) throws Exception {
        new NettyServer().run();
    }


}
