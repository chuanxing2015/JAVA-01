package com.nio.study;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpServer {

    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private static ExecutorService executorService = Executors.newFixedThreadPool(40);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8803);
        while (true){
            Socket socket = serverSocket.accept();
            System.out.println("count :" + atomicInteger.incrementAndGet());
            //service(socket);
            executorService.execute(()->service(socket));
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    service(socket);
//                }
//            }).start();

        }
    }

    public static void service(Socket socket){
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            String body = "from 8803";
            printWriter.println("Content-Length:" + body.getBytes().length);
            printWriter.println();
            printWriter.write(body);
            printWriter.close();
            socket.close();
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }
}
