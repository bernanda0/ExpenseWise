package com.client.expensewise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.Toast;

import com.client.expensewise.api.BaseApiService;
import com.client.expensewise.api.UtilsApi;
import com.client.expensewise.model.GetUserResponse;
import com.client.expensewise.model.LoginResponse;
import com.client.expensewise.model.User;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    SharedPreferences preferences;
    String uid = "u0";
    Button logoutButton, toastUser;
    BaseApiService mApiService;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.uid = this.preferences.getString("uid", "u0");
        logoutButton = this.findViewById(R.id.button_logout);
        toastUser = this.findViewById(R.id.show_user);
        mContext = this;
        mApiService = UtilsApi.getApiService(mContext);


        if (this.uid.equals("u0")){
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
            finishAffinity();
        } else {
            Toast.makeText(this, "Welcome to EW", Toast.LENGTH_SHORT).show();
        }

        toastUser.setOnClickListener(
                i-> {
                    requestUserInfo();
                }

        );

        logoutButton.setOnClickListener(i -> {
            requestLogout("Logout Successful");
        });


    }
    protected User requestUserInfo() {
        mApiService.getUser().enqueue(new Callback<GetUserResponse>() {
            @Override
            public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                if (response.code() == 401) {
                    requestLogout("Session Expired");
                    return;
                }
                GetUserResponse res = response.body();
                if (res == null) {
                    Gson gson = new Gson();
                    try {
                        GetUserResponse errorResponse = gson.fromJson(response.errorBody().string(), GetUserResponse.class);
                        res = errorResponse;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (res.isSuccess()) {
                    Toast.makeText(MainActivity.this, ""+res.getUser(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(MainActivity.this, ""+res.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetUserResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(MainActivity.this, "Failed to get user info", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }


    protected ResponseBody requestLogout(String m) {
        mApiService.logout().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(MainActivity.this, ""+m, Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("uid", "u0");
                editor.apply();
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
                finishAffinity();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(MainActivity.this, "Logout failed", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
}