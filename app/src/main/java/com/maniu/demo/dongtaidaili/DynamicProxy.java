package com.maniu.demo.dongtaidaili;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DynamicProxy implements InvocationHandler {
    private Object object;

    public DynamicProxy(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        method.invoke(object,args);
        return object;
    }
}
