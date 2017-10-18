package com.maple.fastweb.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/9/15.
 */
public class ThreadUtil {
    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static ExecutorService getExecutors (){
        return  executorService;
    }

   /* static {
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                try {
                    shutDownExecutors();
                } catch (InterruptedException e) {

                }
            }
        });
    }*/

    public static void shutDownExecutors() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);
    }
}
