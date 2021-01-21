package com.nio.study.server;

import com.nio.study.server.handler.HttpHandler;
import com.nio.study.server.inbound.HttpInboundInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;


public class NettyServer {

    private  boolean ssl = false;
    private SslContext sslContext;
    private int port = 8801;


    public void run() throws Exception {
        if(ssl){
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslContext = SslContext.newServerContext(ssc.certificate(),ssc.privateKey());
        }else {
            sslContext = null;
        }

        EventLoopGroup bossGrop = new NioEventLoopGroup(1);
        EventLoopGroup workerGrop = new NioEventLoopGroup(16);
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
            b.group(bossGrop,workerGrop)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpInboundInitializer());


            Channel channel = b.bind(port).sync().channel();
            channel.closeFuture().sync();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGrop.shutdownGracefully();
            workerGrop.shutdownGracefully();
        }
    }
}
