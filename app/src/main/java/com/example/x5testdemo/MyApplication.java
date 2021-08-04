package com.example.x5testdemo;

import android.app.Application;

import com.wdcs.utils.OkGoUtils;

import utils.Utils;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this,false);
        OkGoUtils.initOkGo(this);
    }
}
