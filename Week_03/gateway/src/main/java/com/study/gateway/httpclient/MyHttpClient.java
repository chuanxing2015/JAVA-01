package com.study.gateway.httpclient;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class MyHttpClient {


    public static String get(String url) throws Exception {
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            response = httpclient.execute(httpget);
            if(response.getStatusLine().getStatusCode() == 200)
                return EntityUtils.toString(response.getEntity());
        }catch (Exception e){
            throw new Exception("net work exception",e);
        }finally {
            if(response != null){
                try {
                    response.close();
                }catch (Exception e){
                   // throw new Exception("close response exception",e);
                }
            }
        }
        throw new Exception("get failed");
    }
}
