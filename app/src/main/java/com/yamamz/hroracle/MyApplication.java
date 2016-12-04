package com.yamamz.hroracle;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by AMRI on 12/4/2016.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}