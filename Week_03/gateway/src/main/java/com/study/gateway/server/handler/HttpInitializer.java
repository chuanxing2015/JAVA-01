package com.study.gateway.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;


public class HttpInitializer extends ChannelInitializer<SocketChannel> {



    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());//这里是将请求反序列化，翻译成可是被识别的数据
        pipeline.addLast(new HttpObjectAggregator(1024 * 1204));
        pipeline.addLast(new HttpHandler());//业务处理的handler

    }
}
