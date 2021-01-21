package com.nio.study.client;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class MyHttpClient {


    public String get(String url){
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            response = httpclient.execute(httpget);
            if(response.getStatusLine().getStatusCode() == 200)
                return EntityUtils.toString(response.getEntity());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(response != null){
                try {
                    response.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
