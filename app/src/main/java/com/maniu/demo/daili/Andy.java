package com.maniu.demo.daili;

import android.content.Context;
import android.widget.Toast;

import com.maniu.demo.dongtaidaili.ProxyInterface;

public class Andy implements ProxyInterface {
    @Override
    public void buyWawa(Context context) {
        Toast.makeText(context,"动态代理帮我买了冰冰",Toast.LENGTH_LONG).show();
    }
}
