package com.client.expensewise.api;

import android.content.Context;

import com.thomasbouvier.persistentcookiejar.ClearableCookieJar;
import com.thomasbouvier.persistentcookiejar.PersistentCookieJar;
import com.thomasbouvier.persistentcookiejar.cache.SetCookieCache;
import com.thomasbouvier.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl, Context context){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));

        OkHttpClient mInterceptor = new OkHttpClient.Builder()
                .addNetworkInterceptor(new LoggingInterceptor())
                .cookieJar(cookieJar)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(mInterceptor)
                    .build();
        }
        return retrofit;
    }
}
