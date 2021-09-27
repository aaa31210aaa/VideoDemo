package com.example.x5testdemo;

import android.app.Application;

import com.wdcs.utils.OkGoUtils;
import com.wdcs.utils.Utils;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this,true);
        OkGoUtils.initOkGo(this);
    }
}
