package com.maple.fastweb.aop.dynamicproxy;

/**
 * Created by Administrator on 2017/9/28.
 */
public class ProxyServiceImpl implements ProxyService{
    @Override
    public String getName(int id) {
        System.out.println("------getName------");
        return "Tom";
    }

    @Override
    public Integer getAge(int id) {
        System.out.println("------getAge------");
//        int a = 10 / 0 ;
        return 10;
    }
}
