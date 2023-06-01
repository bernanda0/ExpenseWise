package com.client.expensewise;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SharedPreferences preferences;
    Integer uid = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.uid = this.preferences.getInt("uid", -1);

        if (this.uid == -1){
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
        } else {
            Toast.makeText(this, "Welcome to EW", Toast.LENGTH_SHORT).show();
        }
    }
}