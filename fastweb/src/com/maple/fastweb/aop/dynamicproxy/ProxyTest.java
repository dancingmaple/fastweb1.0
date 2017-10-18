package com.maple.fastweb.aop.dynamicproxy;

import com.xiaoleilu.hutool.aop.Aspect;
import com.xiaoleilu.hutool.aop.ProxyUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2017/9/28.
 */
public class ProxyTest {
    public static void main(String [] args){

        ProxyService service = new ProxyServiceImpl();
        System.out.println("===================InvocationHandler=========================");
        InvocationHandler handler = new MyInvocationHandler(service);
        ProxyService proxyService1 = (ProxyService) Proxy.newProxyInstance(service.getClass().getClassLoader(),service.getClass().getInterfaces()
        ,handler);
        System.out.println(proxyService1.getName(1));
        System.out.println(proxyService1.getAge(1));
        System.out.println("=====================ProxyUtil.newProxyInstance=======================");

        ProxyService proxyService2 = ProxyUtil.newProxyInstance(
                service.getClass().getClassLoader(),handler,service.getClass().getInterfaces());
        System.out.println(proxyService2.getName(1));
        System.out.println(proxyService2.getAge(1));
        System.out.println("=====================ProxyUtil.proxy=======================");
        Aspect aspect = new TestAspect(service);
        ProxyService proxyService = ProxyUtil.proxy(aspect);
        System.out.println(proxyService.getName(1));
        System.out.println(proxyService.getAge(1));
    }
}
