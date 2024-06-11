package com.maniu.demo.daili;

import android.content.Context;
import android.widget.Toast;

import com.maniu.demo.dongtaidaili.ProxyInterface;

public class StaticProxy implements ProxyInterface {
    private Andy andy;

    public StaticProxy(Andy andy) {
        this.andy = andy;
    }

    @Override
    public void buyWawa(Context context) {
        Toast.makeText(context,"静态代理帮我买了冰冰,并且给了我", Toast.LENGTH_LONG).show();
    }
}
