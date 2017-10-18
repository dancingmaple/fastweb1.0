package com.maple.fastweb.aop.dynamicproxy;

import com.xiaoleilu.hutool.aop.Aspect;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/9/28.
 */
public class TestAspect extends Aspect{
    public TestAspect(Object target) {
        super(target);
    }

    @Override
    public boolean before(Object o, Method method, Object[] objects) {
        System.out.println(method.getName()+"start");
        return true;
    }

    @Override
    public boolean after(Object o, Method method, Object[] objects) {
        System.out.println(method.getName()+"end");
        return true;
    }

    @Override
    public boolean afterException(Object o, Method method, Object[] objects, Throwable throwable) {
        System.out.println(method.getName()+"has an exception "+throwable.getLocalizedMessage());
        return true;
    }
}
