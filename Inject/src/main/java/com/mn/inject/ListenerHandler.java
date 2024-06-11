package com.mn.inject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ListenerHandler implements InvocationHandler {
    private Object obj;
    private Method myMethod;
    public ListenerHandler(Object obj,Method method){
        this.obj=obj;
        this.myMethod=method;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return myMethod.invoke(obj,args);
    }
}
