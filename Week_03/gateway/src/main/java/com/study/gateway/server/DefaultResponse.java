package com.study.gateway.server;

import com.google.gson.JsonObject;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class DefaultResponse {

    public static FullHttpResponse getDefaultResponse(int code,String message){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status",code);
        jsonObject.addProperty("message",message);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(jsonObject.toString().getBytes()));
        response.headers().set("Content-Type","application/json");
        response.headers().set("Content-Length",response.content().readableBytes());
        return response;
    }


    public static FullHttpResponse getDefaultResponse(String data){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status",200);
        jsonObject.addProperty("data",data);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(jsonObject.toString().getBytes()));
        response.headers().set("Content-Type","application/json");
        response.headers().set("Content-Length",response.content().readableBytes());
        return response;
    }
}
