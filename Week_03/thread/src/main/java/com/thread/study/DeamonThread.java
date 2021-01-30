package com.thread.study;

public class DeamonThread {

    public static void main(String[] args){

        Runnable task = new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5000);
                    System.out.println("threadName : " + Thread.currentThread().getName() + " 休息完了");
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println("threadName : " + Thread.currentThread().getName());
            }
        };

        Thread deamonThread = new Thread(task);
        deamonThread.setName("test thread");
        //如果是守护线程，那么主线程结束之后，守护线程也结束
        //如果不是守护线程，主线程结束后，子线程还继续执行
        deamonThread.setDaemon(false);
        deamonThread.start();
        System.out.println("主线程执行完毕！");

    }
}
