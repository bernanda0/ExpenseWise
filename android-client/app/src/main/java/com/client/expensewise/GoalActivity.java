package com.client.expensewise;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GoalActivity extends AppCompatActivity {
    BottomNavigationView bottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        this.bottomBar = this.findViewById(R.id.bottomNav);
        bottomBar.inflateMenu(R.menu.menu);
        bottomBar.setSelectedItemId(R.id.goal);
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
                            break;
                        case R.id.account:
                            ExpenseWiseToolClass.changeMenuIntent(this, AccountActivity.class);
                            this.finishAffinity();
                            break;
                    }
                    return true;
                }
        );
    }
}