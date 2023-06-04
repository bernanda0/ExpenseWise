package com.client.expensewise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.client.expensewise.api.BaseApiService;
import com.client.expensewise.api.UtilsApi;
import com.client.expensewise.model.LoginResponse;
import com.client.expensewise.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences preferences;
    String uid;
    BaseApiService mApiService;
    TextInputEditText email, password;
    Button loginButton;
    SignInButton googleSignInButton;
    Context mContext;
    TextView register;

    private static final int RC_SIGN_IN = 123;
    private GoogleSignInClient googleSignInClient;

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

        // Configure Google Sign-In options
        String webClientId = getString(R.string.default_web_client_id);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInButton = findViewById(R.id.google_sign_in_button);
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleLogin();
            }
        }
        );

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestLogin();
            }
        });

        register.setOnClickListener(i -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    protected void googleLogin() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            requestGoogleLogin(idToken);
        } catch (ApiException e) {
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    protected LoginResponse requestGoogleLogin(String idToken) {
        mApiService.loginGoogle(idToken).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse res = response.body();
                if (res == null) {
                    Gson gson = new Gson();
                    try {
                        LoginResponse errorResponse = gson.fromJson(response.errorBody().string(), LoginResponse.class);
                        res = errorResponse;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
              
                googleSignInClient.signOut();
              
                if (res.isSuccess()) {
                    uid = res.getUid();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("uid", uid);
                    editor.apply();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finishAffinity();
                }
                Toast.makeText(mContext, res.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                googleSignInClient.signOut();
                Toast.makeText(mContext, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }

    protected LoginResponse requestLogin() {
        mApiService.loginLocal(email.getText().toString(),
                password.getText().toString()).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse res = response.body();
                if (res == null) {
                    Gson gson = new Gson();
                    try {
                        LoginResponse errorResponse = gson.fromJson(response.errorBody().string(), LoginResponse.class);
                        res = errorResponse;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (res.isSuccess()) {
                    uid = res.getUid();
                    email.setText("");
                    password.setText("");
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("uid", uid);
                    editor.apply();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finishAffinity();
                }
                Toast.makeText(mContext, ""+res.getMessage(), Toast.LENGTH_SHORT).show();
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