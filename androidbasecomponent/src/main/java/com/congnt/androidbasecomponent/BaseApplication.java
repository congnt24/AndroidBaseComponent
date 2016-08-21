package com.congnt.androidbasecomponent;

import android.app.Application;
import android.util.Log;

import com.congnt.androidbasecomponent.utility.LogUtil;

/**
 * Created by congn on 8/21/2016.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("On create BaseApplication");
    }
}
