package com.congnt.androidbasecomponent.utility;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtil {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}