package com.client.expensewise.controller;

import com.client.expensewise.model.response.ExpenseCatResponse;
import com.client.expensewise.model.response.ExpenseResponse;
import com.client.expensewise.model.response.IncomeCatResponse;
import com.client.expensewise.model.response.IncomeResponse;
import com.client.expensewise.model.response.ParseResponse;
import com.client.expensewise.model.response.TransactionResponse;
import com.client.expensewise.model.response.UserResponse;
import com.client.expensewise.model.response.LoginResponse;
import com.client.expensewise.model.response.WalletResponse;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface BaseApiService {
    @FormUrlEncoded
    @POST("auth/login/local")
    Call<LoginResponse> loginLocal (@Field("email") String email,
                                   @Field("password") String password);

    @FormUrlEncoded
    @POST("auth/google/verify_token")
    Call<LoginResponse> loginGoogle (@Field("id_token") String idToken);

    @FormUrlEncoded
    @POST("auth/register")
    Call<LoginResponse> register (@Field("username") String username,
                                  @Field("email") String email,
                                  @Field("password") String password);

    @GET("auth/logout")
    Call<ResponseBody> logout ();

    @GET("app/user")
    Call<UserResponse> getUser ();

    @GET("app/wallet")
    Call<WalletResponse> getWallet();

    @GET("app/transaction/getAll")
    Call<TransactionResponse> getTransaction ();

    @GET("app/expense/category")
    Call<ExpenseCatResponse> getExpenseCat ();

    @GET("app/income/category")
    Call<IncomeCatResponse> getIncomeCat ();

    @FormUrlEncoded
    @POST("app/income/insert")
    Call<IncomeResponse> insertIncome (@Field("icid") String category,
                                       @Field("amount") Integer amount,
                                       @Field("time") String date,
                                       @Field("description") String description);
    @FormUrlEncoded
    @POST("app/expense/insert")
    Call<ExpenseResponse> insertExpense (@Field("ecid") String category,
                                         @Field("amount") Integer amount,
                                         @Field("time") String date,
                                         @Field("description") String description);

    @Multipart
    @POST("gpt/parse-receipt")
    Call<ParseResponse> receiptParse (@Part MultipartBody.Part receipt);
}
