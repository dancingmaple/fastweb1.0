package com.maple.fastweb.filter;

/**
 * Created by Administrator on 2017/9/15.
 */

public class ThreadServletCache {
   // ThreadLocal里只存储了简单的String对象，也可以自己定义对象，存储更加复杂的参数
    private static ThreadLocal<String> threadLocal = new ThreadLocal<String>();

    public static String getPostRequestParams(){
        return threadLocal.get();
    }

    public static void setPostRequestParams(String postRequestParams){
        threadLocal.set(postRequestParams);
    }

    public static void removePostRequestParams(){
        threadLocal.remove();
    }
}