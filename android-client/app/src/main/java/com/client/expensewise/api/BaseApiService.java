package com.client.expensewise.api;

import com.client.expensewise.model.LoginResponse;
import com.client.expensewise.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseApiService {
    @GET("auth/google")
    Call<LoginResponse> loginGoogle ();

    @FormUrlEncoded
    @Headers("Cache-Control: max-age=640000")
    @POST("auth/login/local")
    Call<LoginResponse> loginLocal (@Field("email") String email,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("auth/register")
    Call<User> register (@Field("username") String username,
                         @Field("email") String email,
                         @Field("password") String password);
}
