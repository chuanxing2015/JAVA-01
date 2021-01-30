package com.study.gateway.server.handler;

import com.study.gateway.httpclient.MyHttpClient;
import com.study.gateway.server.DefaultResponse;
import com.study.gateway.server.config.ServerConfig;
import com.study.gateway.server.excption.GatewayException;
import com.study.gateway.server.filter.HttpRequestHeaderFilter;
import com.study.gateway.server.filter.RequestFilter;
import com.study.gateway.server.filter.ResponseFilter;
import com.study.gateway.server.filter.ResponseHeaderFilter;
import com.study.gateway.server.router.Router;
import com.study.gateway.server.router.UrlRouter;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;


@ChannelHandler.Sharable
public class HttpHandler extends ChannelInboundHandlerAdapter {


    private Router<FullHttpRequest,FullHttpResponse> router  = new UrlRouter();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            FullHttpRequest fullHttpRequest  = (FullHttpRequest) msg;
            String uri = fullHttpRequest.uri();
            System.out.println("HttpHandler uri : " + uri);
            handlerTest(fullHttpRequest,ctx);
        }finally {
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handlerTest(FullHttpRequest request, ChannelHandlerContext context){
        FullHttpResponse response = null;
        try {
            RequestFilter filter = new HttpRequestHeaderFilter();
            filter.filter(request,context);

            response = requestOtherService(request);

            ResponseFilter responseFilter = new ResponseHeaderFilter();
            responseFilter.filter(response);

        }catch (GatewayException e){
            System.out.println("HttpHandler handler Test error ");
            response = DefaultResponse.getDefaultResponse(e.getCode(),e.getMessage());
            e.printStackTrace();
        }finally {
//            if(request !=null){
//                if(!HttpUtil.isKeepAlive(request)){
//                   context.write(response).addListener(ChannelFutureListener.CLOSE) ;
//                }else{
//                    context.write(response);
//                }
//            }
            if(response == null){
                response = DefaultResponse.getDefaultResponse(500,"internal err");
            }
            context.write(response);
        }
    }

    private FullHttpResponse requestOtherService(FullHttpRequest request){
        return router.route(request);
    }

}
