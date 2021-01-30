package com.study.gateway.server.util;

public class UriUtil {

    public static String getServiceKey(String uri){
        if(uri.startsWith("/")){
            uri = uri.replace("/","");
        }
        String[] uris = uri.split("/");
        if(uris.length > 0){
           return uris[0];
        }
        return "";
    }
}
