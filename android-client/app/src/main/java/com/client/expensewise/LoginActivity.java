package com.client.expensewise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.client.expensewise.api.BaseApiService;
import com.client.expensewise.api.UtilsApi;
import com.client.expensewise.model.LoginResponse;
import com.client.expensewise.model.User;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences preferences;
    Integer uid;
    User loggedAcccount;
    BaseApiService mApiService;
    TextInputEditText email, password;
    Button loginButton;
    Context mContext;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        mApiService = UtilsApi.getApiService();
        mContext = this;
        loginButton = findViewById(R.id.button_login);
        register = findViewById(R.id.registrationPage);
        email = findViewById(R.id.et_login_email);
        password = findViewById(R.id.et_login_password);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestLogin();
            }
        });

        register.setOnClickListener(i ->
        {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    protected LoginResponse requestGoogleLogin() {
        mApiService.loginGoogle().enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                System.out.println(response);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(mContext, "Wrong email or password!", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }


    protected LoginResponse requestLogin() {
        mApiService.loginLocal(email.getText().toString(),
                password.getText().toString()).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200) {
                    loggedAcccount = response.body().getUser();
                    Toast.makeText(mContext, "Welcome to EW ", Toast.LENGTH_SHORT).show();
                    email.setText("");
                    password.setText("");
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("uid", loggedAcccount.getUid());
                    editor.apply();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                System.out.println(response);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(mContext, "Wrong email or password!", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
}