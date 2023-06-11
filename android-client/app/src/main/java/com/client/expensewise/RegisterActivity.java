package com.client.expensewise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;

import com.client.expensewise.controller.BaseApiService;
import com.client.expensewise.controller.UtilsApi;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    SharedPreferences preferences;
    String uid;
    BaseApiService mApiService;
    TextInputEditText email, password;
    Button loginButton;
    Context mContext;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        mContext = this;
        mApiService = UtilsApi.getApiService(mContext);
        loginButton = findViewById(R.id.button_login);
        register = findViewById(R.id.registrationPage);
        email = findViewById(R.id.et_login_email);
        password = findViewById(R.id.et_login_password);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }
}