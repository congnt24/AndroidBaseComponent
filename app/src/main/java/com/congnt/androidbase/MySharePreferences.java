package com.congnt.androidbase;

import android.content.Context;

import com.congnt.androidbasecomponent.Awesome.AwesomeSharedPreferences;

/**
 * Created by congn_000 on 9/28/2016.
 */

public class MySharePreferences extends AwesomeSharedPreferences {
    private SingleSharedPreferences<String> str = new SingleSharedPreferences<String>(){
        @Override
        protected String ID() {
            return "str";
        }
    };

    public MySharePreferences(Context context) {
        super(context);
    }
}
