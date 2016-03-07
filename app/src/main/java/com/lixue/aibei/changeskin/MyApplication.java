package com.lixue.aibei.changeskin;

import android.app.Application;

import com.lixue.aibei.changeskinlib.SkinManager;

/**
 * Created by Administrator on 2016/2/26.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstance().init(this);
    }
}
