package com.client.expensewise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.client.expensewise.controller.BaseApiService;
import com.client.expensewise.controller.UtilsApi;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends AppCompatActivity {
    BottomNavigationView bottomBar;
    SharedPreferences preferences;
    FloatingActionButton logoutButton;
    BaseApiService mApiService;
    Context mContext;
    ImageView profilePhoto;
    TextView initial, username, email, phone, occupation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        logoutButton = this.findViewById(R.id.button_logout);
        profilePhoto = this.findViewById(R.id.profile_photo2);
        username = this.findViewById(R.id.userName);
        email = this.findViewById(R.id.user_email);
        phone = this.findViewById(R.id.user_phone);
        occupation = this.findViewById(R.id.user_occupation);
        mContext = this;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mApiService = UtilsApi.getApiService(mContext);
        this.bottomBar = this.findViewById(R.id.bottomNav);
        bottomBar.inflateMenu(R.menu.menu);
        bottomBar.setSelectedItemId(R.id.account);
        bottomBar.setOnItemSelectedListener(
            item -> {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        ExpenseWiseToolClass.changeMenuIntent(this, MainActivity.class);
                        this.finishAffinity();
                        break;
                    case R.id.transaction:
                        ExpenseWiseToolClass.changeMenuIntent(this, TransactionActivity.class);
                        this.finishAffinity();
                        break;
                    case R.id.goal:
                        ExpenseWiseToolClass.changeMenuIntent(this, GoalActivity.class);
                        this.finishAffinity();
                        break;
                    case R.id.account:
                        break;
                }
                return true;
            }
        );

        Glide.with(this)
                .load(MainActivity.user.getImg())
                .circleCrop()
                .into(profilePhoto);
        username.setText(MainActivity.user.getUsername());
        email.setText(MainActivity.user.getEmail());
        phone.setText(MainActivity.user.getPhoneNumber());
        occupation.setText(MainActivity.user.getOccupation());

        logoutButton.setOnClickListener(i -> {
            requestLogout("Logout Successful");
        });

    }

    protected ResponseBody requestLogout(String m) {
        mApiService.logout().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(mContext, ""+m, Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("uid", "u0");
                editor.apply();
                ExpenseWiseToolClass.destroyAll();
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
                finishAffinity();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(mContext, "Logout failed", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
}