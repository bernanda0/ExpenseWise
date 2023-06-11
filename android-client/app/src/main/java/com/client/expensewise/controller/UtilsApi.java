package com.client.expensewise.controller;

import android.content.Context;

import okhttp3.HttpUrl;

public class UtilsApi {
    private static HttpUrl baseUrl = new HttpUrl.Builder()
            .scheme("https")
            .host("api-expensewise.netlify.app")
            .encodedPath("/.netlify/functions/server/")
            .build();
    public static BaseApiService getApiService(Context context) {
        return RetrofitClient.getClient(baseUrl, context).create(BaseApiService.class);
    }
}
