package com.study.gateway.server;

import com.study.gateway.server.config.*;
import com.study.gateway.server.handler.HttpInitializer;
import com.study.gateway.server.util.ConfigParser;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class GatewayServer {


    //开启网关服务
    public void run(){
        //1.加载配置
        ServerConfig config = null;
        RouterConfig.getInstance().init();
        try {
          config =  ConfigParser.parseServerConfig();
        }catch (Exception e){
            throw new RuntimeException("load server config failed ",e);
        }



        EventLoopGroup bossGroup = new NioEventLoopGroup(config.getBossSize());
        EventLoopGroup workerGroup = new NioEventLoopGroup(config.getWorkerSize());
        try {
            ServerBootstrap b =new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG,128)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .option(ChannelOption.SO_REUSEADDR,true)
                    .option(ChannelOption.SO_RCVBUF,32*1204)
                    .option(ChannelOption.SO_SNDBUF,32*1204)
                    .option(EpollChannelOption.SO_REUSEPORT,true)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpInitializer());


            Channel channel = b.bind(config.getPort()).sync().channel();
            channel.closeFuture().sync();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
