package com.concurrent.study;


import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    static int value = 0;
    static volatile boolean flag = false;

    public static void main(String[] args) throws ExecutionException, InterruptedException, BrokenBarrierException {
        ExecutorService pool =  Executors.newSingleThreadExecutor();

        // 一. 通过线程池实现

        //1 .通过submit实现
        Future<Integer> future = pool.submit(new Callable<Integer>() {
            public Integer call() throws Exception {
                return sum();
            }
        });
        System.out.println("pool submit : " + future.get());


        //2. 通过线程池的submit的result对象返回
        final Result result = new Result();
        Future<Result> future1 = pool.submit(new Runnable() {
            public void run() {
                result.setValue(sum());

            }
        },  result);
        Result result1 = future1.get();
        System.out.println("pool submit result : " + result1.getValue());

        //3. lockSupport
        MyRunnable myRunnable = new MyRunnable(Thread.currentThread()) {
            public void run() {
                callBack(sum());
                LockSupport.unpark(thread);
            }
        };
        pool.execute(myRunnable);
        LockSupport.park();
        System.out.println("pool execute and lockSupport : " + myRunnable.getValue());

        pool.shutdown();

        //二 通过自己创建线程实现

        // 4. join
        value = 0;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                value = sum();
            }
        });
        thread.start();
        thread.join();
        System.out.println("thread  join : " + value);


        // 5. sleep
        value = 0;
        new Thread(new Runnable() {
            public void run() {
                value = sum();
            }
        }).start();
        Thread.sleep(100);
        System.out.println("thread  sleep : " + value);


        // 6.synchronized
        value = 0;
        final  Object lock = new Object();
        MyThread myThread = new MyThread(lock);
        myThread.start();
        synchronized (lock){
            if(value == 0){
                lock.wait();
            }
            System.out.println("thread synchronized and wait : " + value);
        }


        // 7.CAS
        value = 0;
        new Thread(new Runnable() {
            public void run() {
                value = sum();
                flag = true;
            }
        }).start();
        while (!flag){
        }
        System.out.println("thread  volatile and CAS : " + value);


        // 8. ReentrantLock
        value = 0;
        final ReentrantLock reentrantLock = new ReentrantLock();
        final Condition condition = reentrantLock.newCondition();
        new Thread(new Runnable() {
            public void run() {
                reentrantLock.lock();
                value = sum();
                condition.signal();
                reentrantLock.unlock();
            }
        }).start();
        reentrantLock.lock();
        try{
             while (value == 0){
                //注意wait 和 notify 配对使用， await和signal配对使用
                condition.await();
            }
        }finally {
            reentrantLock.unlock();
        }
        System.out.println("thread  ReentrantLock : " + value);

        //9. 通过CountDownLatch实现
        value = 0;
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(new Runnable() {
            public void run() {
                value = sum();
                countDownLatch.countDown();
            }
        }).start();
        countDownLatch.await();
        System.out.println("thread  CountDownLatch : " + value);


        //10. 通过CyclicBarrier实现
        value = 0;
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        Thread th = new Thread(new Runnable() {
            public void run() {
                value = sum();
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();
        //阻塞主线程
        cyclicBarrier.await();
        System.out.println("thread  cyclicBarrier: " + value);


        //11. 通过Semaphore实现
        value = 0;
        final Semaphore semaphore = new Semaphore(0);
        Thread td = new Thread(new Runnable() {
            public void run() {
                value = sum();
                semaphore.release();

            }
        });
        td.start();
        //主线程阻塞，子线程释放
        semaphore.acquire();
        System.out.println("thread  Semaphore : " + value);



    }


    static class Result{
        int value = 0;

        public void setValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    static abstract class MyRunnable implements Runnable{

        Thread thread = null;
        int value;

        public MyRunnable(Thread thread){
            this.thread = thread;
        }

        public int getValue() {
            return value;
        }

        public void callBack(int sum){
            this.value = sum;
        }
    }

    static  class  MyThread extends Thread{

        private Object lock;
         MyThread(Object lock){
            this.lock = lock;
        }

        @Override
        public void run() {
            synchronized (lock){
                value = sum();
                lock.notify();
            }
        }
    }


    public static int sum(){
        return fibo(34);
    }


    private static int fibo(int a) {
        if ( a < 2)
            return 1;
        return fibo(a-1) + fibo(a-2);
    }
}
