package com.client.expensewise;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.client.expensewise.controller.BaseApiService;
import com.client.expensewise.controller.UtilsApi;
import com.client.expensewise.model.Transaction;
import com.client.expensewise.model.response.TransactionResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionActivity extends AppCompatActivity {
    BottomNavigationView bottomBar;
    ListView listTransaction;
    SharedPreferences preferences;
    BaseApiService mApiService;
    Context mContext;
    FloatingActionButton addTransaction;

    static ArrayList<Transaction> transactions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        listTransaction = this.findViewById(R.id.listTransaction);
        mContext = this;
        mApiService = UtilsApi.getApiService(mContext);
        this.bottomBar = this.findViewById(R.id.bottomNav);
        addTransaction = this.findViewById(R.id.addTransactionButton);

        bottomBar.inflateMenu(R.menu.menu);
        bottomBar.setSelectedItemId(R.id.transaction);
        bottomBar.setOnItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.dashboard:
                            ExpenseWiseToolClass.changeMenuIntent(this, MainActivity.class);
                            this.finishAffinity();
                            break;
                        case R.id.transaction:
                            break;
                        case R.id.goal:
                            ExpenseWiseToolClass.changeMenuIntent(this, GoalActivity.class);
                            this.finishAffinity();
                            break;
                        case R.id.account:
                            ExpenseWiseToolClass.changeMenuIntent(this, AccountActivity.class);
                            this.finishAffinity();
                            break;
                    }
                    return true;
                }
        );
        if (transactions.isEmpty()) requestTransaction();
        else updateListTransaction();

        addTransaction.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditTransactionActivity.class);
            intent.putExtra("action", "add");
            this.startActivity(intent);
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

    protected ArrayList<Transaction> requestTransaction(){
        mApiService.getTransaction().enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                if (response.code() == 401) {
                    requestLogout("Session Expired");
                    return;
                }
                TransactionResponse res = response.body();
                if (res == null) {
                    Gson gson = new Gson();
                    try {
                        TransactionResponse errorResponse = gson.fromJson(response.errorBody().string(), TransactionResponse.class);
                        res = errorResponse;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (res.isSuccess()) {
                    transactions = res.getTransactions();
                    updateListTransaction();
                } else {
                    Toast.makeText(mContext, ""+res.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(mContext, "Failed to get transaction", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
    private void updateListTransaction() {
        listTransaction.setAdapter(new TransactionListAdapter(mContext, transactions, false));
        listTransaction.setOnItemClickListener((parent, view, position, id) -> {
            Transaction transaction = transactions.get(position);
            viewTransactionModal();
        });
    }
    private void viewTransactionModal() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.transaction_info_card, null);

        dialog.setView(dialogView);
        dialog.setCancelable(true);

        dialogView.findViewById(R.id.edit_transaction).setOnClickListener(
                v -> {
                    Intent intent = new Intent(mContext, AddEditTransactionActivity.class);
                    intent.putExtra("action", "edit");
                    mContext.startActivity(intent);
                }
        );

        dialog.show();
    }
}