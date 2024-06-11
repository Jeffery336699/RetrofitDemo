package com.mn.inject;

import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectManager {

    public static void inject(Object obj){
        //布局注入
        injectLayout(obj);
        //控件注入
        injectViews(obj);
        //事件注入
        injectEvent(obj);
        //事件注入
        //injectClick(obj);
    }

    //需要讲解注解和元注解，可扩展性
    private static void injectEvent(Object obj) {
        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            //注意，别把代码写死了 method.getAnnotation(OnClick.class);
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                //annotation是事件比如onClick 就去取对应的注解
                Class<? extends Annotation> annotationType = annotation.annotationType();
                InjectEvent injectEvent = annotationType.getAnnotation(InjectEvent.class);
                if (injectEvent != null) {
                    //方法上面的是自定义注解
                    String listenerSetter = injectEvent.listenerSetter();
                    Class listenerType = injectEvent.listenerType();

                    String callbackMethod = injectEvent.callbackMethod();

                    //否则就是一个事件处理的方法
                    //开始获取事件处理的相关信息（三要素）
                    //用于确定是哪种事件


                    ListenerHandler listenerHandler = new ListenerHandler(obj, method);
                    Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, listenerHandler);

                    try {
                        Method valueMethod = annotationType.getDeclaredMethod("value");
                        int[] ids = (int[]) valueMethod.invoke(annotation);
                        for (int id : ids) {
                            //btn = findViewById(R.id.btn);
                            Method findViewById = clazz.getMethod("findViewById", int.class);
                            //findViewById(R.id.btn).setOnClickkListener;
                            View view = (View) findViewById.invoke(obj, id);
                            //view.setOnClickListener()
                            Method setterMethod = view.getClass().getMethod(listenerSetter, listenerType);
                            //动态代理
                            //setterMethod.invoke(view,listenerType.newInstance());
                            setterMethod.invoke(view, listener);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

        private static void injectClick(Object context) {
            //需要一次性处理安卓中23种事件
            Class<?> clazz=context.getClass();
            Method[] methods=clazz.getDeclaredMethods();

            for (Method method : methods) {
                //注意，别把代码写死了 method.getAnnotation(OnClick.class);
                Annotation[] annotations=method.getAnnotations();
                for (Annotation annotation : annotations) {
                    //annotation是事件比如onClick 就去取对应的注解
                    Class<?> annotationClass=annotation.annotationType();
                    InjectEvent eventBase=annotationClass.getAnnotation(InjectEvent.class);
                    //如果没有InjectEvent，则表示当前方法不是一个事件处理的方法
                    if(eventBase==null){
                        continue;
                    }
                    //否则就是一个事件处理的方法
                    //开始获取事件处理的相关信息（三要素）
                    //用于确定是哪种事件
//                btn.setOnClickListener（new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
                    //1.setOnClickListener 订阅关系
//                String listenerSetter();
                    String listenerSetter=eventBase.listenerSetter();
                    //2.new View.OnClickListener()  事件本身
//                Class<?> listenerType();
                    Class<?> listenerType=eventBase.listenerType();
                    //3.事件处理程序
//                String callbackMethod();
                    String callBackMethod=eventBase.callbackMethod();

                    //得到3要素之后，就可以执行代码了
                    Method valueMethod=null;
                    try{
                        //反射得到id,再根据ID号得到对应的VIEW（Button）
                        valueMethod=annotationClass.getDeclaredMethod("value");
                        int[] viewId=(int[])valueMethod.invoke(annotation);
                        for (int id : viewId) {
                            //为了得到Button对象,使用findViewById
                            Method findViewById=clazz.getMethod("findViewById",int.class);
                            View view=(View)findViewById.invoke(context,id);
                            //运行到这里，view就相到于我们写的Button
                            if(view==null){
                                continue;
                            }
                            //activity==context    click===method
                            ListenerHandler listenerInvocationHandler=
                                    new ListenerHandler(context,method);

                            //做代理   new View.OnClickListener()对象
                            Object proxy= Proxy.newProxyInstance(listenerType.getClassLoader()
                                    ,new Class[]{listenerType},listenerInvocationHandler);
                            //执行  让proxy执行的onClick()
                            //参数1  setOnClickListener（）
                            //参数2  new View.OnClickListener()对象
                            //   view.setOnClickListener（new View.OnClickListener()）
                            Method onClickMethod=view.getClass().getMethod(listenerSetter,listenerType);
                            onClickMethod.invoke(view,proxy);
                            //这时候，点击按钮时就会去执行代理类中的invoke方法()

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
    }


    private static void injectViews(Object obj){
        //object就是MainActivity
        //通过反射来找到注解的成员
        Class<?> clazz =  obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field:fields){
            InjectView injectView = field.getAnnotation(InjectView.class);
            //findViewById
            if(injectView!=null) {
                try {

                    //MainActivity一定有findViewById
                    Method method = clazz.getMethod("findViewById", int.class);
                    int value = injectView.value();
                    View view = (View) method.invoke(obj, value);
                    field.setAccessible(true);
                    field.set(obj,view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void injectLayout(Object obj){
        Class<?> clazz = obj.getClass();
        InjectLayout injectLayout = clazz.getAnnotation(InjectLayout.class);
        if(injectLayout!=null){
            //setContentView(R.layout.activity_main);
            //注解干的事情：1标记，2传值
            try {
                Method method = clazz.getMethod("setContentView",int.class);
                int value = injectLayout.value();
                method.invoke(obj,value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
