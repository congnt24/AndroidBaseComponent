package com.congnt.androidbasecomponent.network;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by congn_000 on 8/22/2016.
 */
public class RetrofitBuilder {
    private static Retrofit instance;
    public static Retrofit getRetrofit(String baseUrl, @Nullable final Map<String, String> headerMap
            , @Nullable int connectTimeoutInMs, @Nullable int readTimeoutInMs) {
        if (instance != null){
            return instance;
        }
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (connectTimeoutInMs > 0) {
            httpClient.connectTimeout(connectTimeoutInMs, TimeUnit.MILLISECONDS);
        }
        if (connectTimeoutInMs > 0) {
            httpClient.readTimeout(readTimeoutInMs, TimeUnit.MILLISECONDS);
        }
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder builder = original.newBuilder();
                if (headerMap != null) {
                    for (String item :
                            headerMap.keySet()) {
                        builder.header(item, headerMap.get(item));
                    }
                }
                Request request = builder.method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        });
        instance = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return instance;
    }
}
