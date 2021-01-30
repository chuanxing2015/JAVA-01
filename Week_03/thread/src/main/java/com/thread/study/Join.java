package com.thread.study;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class Join {

    public static void main(String[] args) throws InterruptedException {
        Object oo = new Object();
        MyThread thread = new MyThread(oo);
        thread.start();

        AtomicInteger atomicInteger;



       // Thread.sleep(100);
        synchronized (oo){
            for(int i = 0; i< 20; i++){
                try {
                    //如果使用oo, 当i=19的时候，主线程会等待，但是没有其他线程notify
                    //thread线程会被blocked， 应为join不会释放锁，synchronized void join
                    if(i==19){
                       // thread.join(10000);
                        thread.join();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " : " + i);
            }
        }
    }





    static class MyThread extends Thread{
        public Object oo;

        public MyThread(Object oo){
            this.oo = oo;
        }

        @Override
        public void run() {
            synchronized (oo){
                for(int i = 0 ; i<20 ;  i++){
                    System.out.println("MyThread : " + i);
                }
            }
        }
    }
}
