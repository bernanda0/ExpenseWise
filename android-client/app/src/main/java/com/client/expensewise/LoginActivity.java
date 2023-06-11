package com.client.expensewise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.client.expensewise.controller.BaseApiService;
import com.client.expensewise.controller.UtilsApi;
import com.client.expensewise.model.response.LoginResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences preferences;
    String uid;
    BaseApiService mApiService;
    TextInputLayout username;
    TextInputEditText email, password;
    Button loginButton;
    SignInButton googleSignInButton;
    Context mContext;
    TextView titleLogin, messageLogin, register, dontHaveAccount;
    Boolean clickRegister = false;

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
        dontHaveAccount = findViewById(R.id.dontHaveAcc);
        titleLogin = findViewById(R.id.titleLogin);
        messageLogin = findViewById(R.id.messageLogin);
        username = findViewById(R.id.textFieldUsername);
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

        final boolean[] validEmail = new boolean[1];
        final boolean[] validPassword = new boolean[1];
        final boolean[] validUsername = new boolean[1];
        validUsername[0] = true;

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email_str = email.getText().toString().trim();
                validEmail[0] = !email_str.isEmpty()
                        && checkEmail(email_str);
                loginButton.setEnabled(validEmail[0] && validPassword[0] && validUsername[0]);
                if (!validEmail[0]) {
                    email.setError("Invalid email format");
                } else {
                    email.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password_str = password.getText().toString().trim();
                validPassword[0] = !password_str.isEmpty()
                        && checkPassword(password_str);
                loginButton.setEnabled(validEmail[0] && validPassword[0] && validUsername[0]);
                if (!validPassword[0]) {
                    password.setError("Password must be more than equal 6 characters");
                } else {
                    password.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickRegister) {
                    requestRegister();
                } else {
                    requestLogin();
                }
            }
        });

        username.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String username_str = username.getEditText().getText().toString().trim();
                validUsername[0] = !username_str.isEmpty()
                        && checkUsername(username_str);
                loginButton.setEnabled(validEmail[0] && validPassword[0] && validUsername[0]);
                if (!validUsername[0]) {
                    username.getEditText().setError("Username must be more than 4 characters");
                } else {
                    username.getEditText().setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        register.setOnClickListener(i -> {
            clickRegister = !clickRegister;
            if (clickRegister) {
                titleLogin.setText("Register to Expense Wise");
                messageLogin.setText("Please fill in the form below to register");
                username.setVisibility(View.VISIBLE);
                loginButton.setText("Register");
                register.setText("Login");
                dontHaveAccount.setText("Already have an account?");
                validUsername[0] = false;
                loginButton.setEnabled(validEmail[0] && validPassword[0] && validUsername[0]);
            } else {
                titleLogin.setText("Login to Expense Wise");
                messageLogin.setText("Input your email and password");
                username.setVisibility(View.GONE);
                loginButton.setText("Login");
                register.setText("Register");
                dontHaveAccount.setText("Don't have an account?");
                validUsername[0] = true;
            }
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
                Toast.makeText(mContext, "Login Fail due to Server Error", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mContext, "Login Fail due to Server Error", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }

    protected LoginResponse requestRegister() {
        mApiService.register(username.getEditText().getText().toString(), email.getText().toString(),
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
                    username.getEditText().setText("");
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
                Toast.makeText(mContext, "Register Fail due to Server Error", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }

    private boolean checkEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean checkPassword(String password) {
        return password.length() >= 6;
    }
    private boolean checkUsername(String name) {
        return name.length() > 4;
    }
}