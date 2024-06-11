package com.example.test;

import java.lang.annotation.Annotation;

public class MyClass {
    public static void main(String[] args) {
        //通过反射获取class字节码对象,有3种方式
        //1、根据全类名获取,forName获取
        Class<?> aClass = null;

        try {
            aClass= Class.forName("com.example.test.User");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(aClass);

        //2、通过对象来获得
        Class bClass = new User().getClass();
        System.out.println(bClass);

        //3、通过类名获取
        Class<User> cClass = User.class;
        System.out.println(cClass);

        // 通过反射获得注解
        Annotation[] annotations =  aClass.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation);
        }

        DemoAnnotation annotation =  aClass.getAnnotation(DemoAnnotation.class);
        String value = annotation.value();
        System.out.println(value);
    }

    /**
     * Annotation 是从 JDK 1.5 开始引入的新技术
     * Annotation 的作用
     * 不是程序本身，可以对程序作出解释（这一点和注释没什么区别）
     * 可以被其它程序（比如：编译器等）读取
     * Annotation 的格式
     * 注解是以 “@注释名” 在代码中存在的，还可以添加一些参数值，例如： @SuppressWarnings(value=“unchecked”)
     * Annotation 在哪里使用？
     * 可以附加在 package，class，method，field 等上面，
     * 相当于给他们添加了额外的辅助信息，我们可以通过反射机制编程实现对这些元数据的访问
     * 如：（重写父类方法的时候会有个 @Override 注解）
     */
    @MyAnnotation(value2 = "",id=0)
    class MyObject extends Object{

        @MyAnnotation(value2 = "")
        @Override
        public String toString() {
            return super.toString();
        }
    }
}