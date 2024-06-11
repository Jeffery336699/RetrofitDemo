package com.example.test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//定义一个注解
//Target表示我们的注解可以用在哪些地方
@Target(value = {ElementType.METHOD,ElementType.TYPE})
//@Retention 表示我们的注解在什么地方还有效(作用域）（Runtime>Class>Source
@Retention(value = RetentionPolicy.RUNTIME)
//Documented表示是否将我们的注解生成在JavaDoc中
@Documented
//Inherited 表示子类可以继承父类的注解
@Inherited
public @interface MyAnnotation {
    String value2();
    int id() default 0;
}
