package com.nio.study.server.handler;

import com.nio.study.client.MyHttpClient;
import com.nio.study.server.filter.HttpRequestHeaderFilter;
import com.nio.study.server.filter.RequestFilter;
import com.nio.study.server.filter.ResponseFilter;
import com.nio.study.server.filter.ResponseHeaderFilter;
import com.nio.study.server.router.Router;
import com.nio.study.server.router.UrlRouter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.StringUtil;

import java.util.concurrent.*;

import static io.netty.handler.codec.rtsp.RtspHeaderValues.KEEP_ALIVE;
import static sun.tools.jconsole.Messages.CONNECTION;


@ChannelHandler.Sharable
public class HttpHandler extends ChannelInboundHandlerAdapter {

    private String[] urls = {
            "http://localhost:8802",
            "http://localhost:8803"
    };

    private Router<String> router  = new UrlRouter();
    private MyHttpClient httpClient = new MyHttpClient();
    private ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()/2);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            System.out.println(msg);
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

            response = requestOtherService(request,context);

            ResponseFilter responseFilter = new ResponseHeaderFilter();
            responseFilter.filter(response);

        }catch (Exception e){
            System.out.println("HttpHandler handler Test error ");
            e.printStackTrace();
        }finally {
            if(request !=null){
                if(!HttpUtil.isKeepAlive(request)){
                   context.write(response).addListener(ChannelFutureListener.CLOSE) ;
                }else {
                   context.write(response);
                }
            }
        }
    }

    private FullHttpResponse requestOtherService(FullHttpRequest request, final ChannelHandlerContext context){
        final String node = router.route(urls);

        String value = "{\"value\":\"default\"}";

        if(StringUtil.isNullOrEmpty(node)){
            value = "{\"value\":\"nodes is empty! \"}";
        }else {
            Future<String> future = service.submit(new Callable<String>() {
                public String call() throws Exception {
                    return httpClient.get(node);
                }
            });
            try {
                value = future.get(500, TimeUnit.MILLISECONDS);
            }catch (Exception e){
                System.out.println("request other service failed url : " + node);
                e.printStackTrace();
            }
        }
        value = value == null ? "{\"value\":\"request url: \" "+ node +" failed}" : value;
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(value.getBytes()));
        response.headers().set("Content-Type","application/json");
        response.headers().set("Content-Length",response.content().readableBytes());
        return response;
    }

}
