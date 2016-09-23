package com.congnt.androidbasecomponent.Awesome;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;

import com.congnt.androidbasecomponent.network.Downloader;
import com.congnt.androidbasecomponent.network.RetrofitBuilder;

import java.util.Map;

import retrofit2.Retrofit;

/**
 * Created by congn_000 on 8/22/2016.
 */
public class AwesomeNetwork {
    private static String fileName;

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static Retrofit executeRetrofit(String baseUrl, @Nullable final Map<String, String> headerMap
            , @Nullable int connectTimeoutInMs, @Nullable int readTimeoutInMs) {
        return RetrofitBuilder.getRetrofit(baseUrl, headerMap, connectTimeoutInMs, readTimeoutInMs);
    }

    public static String executeDownload(final Context context, final String urls) {
        return Downloader.executeDownload(context, urls);
    }
}
