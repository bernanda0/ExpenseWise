package com.client.expensewise.api;

import android.content.Context;

public class UtilsApi {
    public static final String BASE_URL_API = "http://192.168.18.241:1111/";
    public static BaseApiService getApiService(Context context) {
        return RetrofitClient.getClient(BASE_URL_API, context).create(BaseApiService.class);
    }
}
