package com.maniu.demo;


import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import com.maniu.demo.daili.Andy;
import com.maniu.demo.daili.StaticProxy;
import com.maniu.demo.dongtaidaili.DynamicProxy;
import com.maniu.demo.dongtaidaili.ProxyInterface;
import com.mn.inject.InjectLayout;
import com.mn.inject.InjectManager;
import com.mn.inject.InjectView;
import com.mn.inject.OnClick;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@InjectLayout(R.layout.activity_main)
public class MainActivity extends Activity{
    @InjectView(R.id.postBtn)
    TextView postUrl;
    @InjectView(R.id.img)
    ImageView imageView;
    @InjectView(R.id.img2)
    ImageView imageView2;

    @InjectView(R.id.text)
    TextView textView;

    @InjectView(R.id.text2)
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        InjectManager.inject(this);

    }

    private void requesrRetrofit(){
        //https://wm.yxg12.cn/webapp.php/
        //第一种实例化方案
        //Retrofit是不是封装的okhttp
        //1、创建一个接口
        //2、创建一个Retrofit对象
        //Builder创建Platform对象，baseUrl设置了基本的域名基础连接，build就相当于建造了一个Retrofit
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://hb.yxg12.cn/").build();
        //retrofit.create,retrofit创建了1个接口
        PersonInterface personInterface = retrofit.create(PersonInterface.class);
        Call<ResponseBody> call =  personInterface.getPersonInfo();
        //https://hb.yxg12.cn/webapp.php
        //执行异步请求
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody body = response.body();
                try {
                    //这里就是主线程了，可以直接渲染数据
                    //如果是okhttp请求数据，这里是不可以的
                    String content = body.string();
                    PersonInfo personInfo = new Gson().fromJson(content,PersonInfo.class);
                    Glide.with(MainActivity.this).load("http://g.hiphotos.baidu.com/image/pic/item/0d338744ebf81a4c87a3add4d52a6059252da61e.jpg").into(imageView);
                    textView.setText("姓名："+personInfo.getName()+" 年龄："+personInfo.getAge()+" 职业："+personInfo.getJob()+" 简介："+personInfo.getDesc()+" 是否喜欢："+personInfo.getIsLove());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.postBtn)
    public void postUrl(View view) {
        requesrRetrofit();
    }

    //你买的冰冰不够大，我还要再买一个，我现在的方式要通过RXJava的方式进行购买
    @OnClick(R.id.text2)
    public void postUrl2(View view) {

    }

    public void daili(View view) {
        //静态代理
        final Andy andy = new Andy();
        /*StaticProxy proxy = new StaticProxy(andy);
        proxy.buyWawa(this);*/
        //动态代理，根本就不需要关心是谁帮我买了哇哇
        //中介公司
        /*ProxyInterface proxyInterface = (ProxyInterface) Proxy.newProxyInstance(andy.getClass().getClassLoader(),andy.getClass().getInterfaces(),new DynamicProxy(andy));
        proxyInterface.buyWawa(this);*/

        //匿名的动态代理,经常出现在第三方框架中
        ProxyInterface proxyInterface2 = (ProxyInterface) Proxy.newProxyInstance(andy.getClass().getClassLoader(), andy.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                method.invoke(andy,args);
                return andy;
            }
        });
        proxyInterface2.buyWawa(this);
        // 仅仅只是内存中构建出来的一个对象,并且把你需要代理的对象的方法给你hook出来,最终还是需要你来正确处理哟
        Log.i(TAG, "daili:  proxyInterface2-->"+proxyInterface2.getClass().getCanonicalName()); //TODO $Proxy7

    }
}
