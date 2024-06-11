package com.mn.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectEvent {
    //事件三要素
    //1、setOnClickListener
    //2、view.onClickListener
    //3、onclick
    String listenerSetter();
    Class listenerType();
    String callbackMethod();

}
