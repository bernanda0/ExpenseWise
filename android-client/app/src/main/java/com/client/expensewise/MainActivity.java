package com.client.expensewise;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.client.expensewise.controller.BaseApiService;
import com.client.expensewise.controller.UtilsApi;
import com.client.expensewise.model.ExpenseCategory;
import com.client.expensewise.model.IncomeCategory;
import com.client.expensewise.model.Transaction;
import com.client.expensewise.model.Wallet;
import com.client.expensewise.model.response.ExpenseCatResponse;
import com.client.expensewise.model.response.IncomeCatResponse;
import com.client.expensewise.model.response.TransactionResponse;
import com.client.expensewise.model.response.UserResponse;
import com.client.expensewise.model.User;
import com.client.expensewise.model.response.WalletResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    SharedPreferences preferences;
    String uid = "u0";
    TextView username, initial, user_rank, user_points, balance, total_income, total_expense;
    TextView food_cat, transport_cat, shopping_cat, entertainment_cat, bills_cat, other_cat;
    TextView salary_cat, gift_cat, investment_cat, other_in_cat;
    BaseApiService mApiService;
    Context mContext;
    ImageView profilePhoto;
    BottomNavigationView bottomBar;
    PieChart pieChart, pieChart2;
    FloatingActionButton addTransaction;
    ConstraintLayout income_layout, expense_layout;
    LinearLayout income_cat_layout, expense_cat_layout;
    ArrayList<Pair<LinearLayout, String>> expense_cat = new ArrayList<>();
    ArrayList<Pair<LinearLayout, String>> income_cat = new ArrayList<>();

    // ceritanya repositori
    static User user;
    static Wallet wallet;
    static ArrayList<ExpenseCategory> expenseCategories;
    static ArrayList<IncomeCategory> incomeCategories;

    //after transaction list updated
    boolean isTransactionUpdated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.uid = this.preferences.getString("uid", "u0");
        this.isTransactionUpdated = this.getIntent().getBooleanExtra("transaction_updated", false);
        username = this.findViewById(R.id.userName);
        user_rank = this.findViewById(R.id.user_rank);
        user_points = this.findViewById(R.id.user_points);
        mContext = this;
        mApiService = UtilsApi.getApiService(mContext);
        profilePhoto = this.findViewById(R.id.profile_photo);
        initial = this.findViewById(R.id.initial);
        balance = this.findViewById(R.id.total_balance);
        total_income = this.findViewById(R.id.total_income);
        total_expense = this.findViewById(R.id.total_expense);
        pieChart = this.findViewById(R.id.piechart);
        pieChart2 = this.findViewById(R.id.piechart2);
        food_cat = this.findViewById(R.id.food_percentage);
        transport_cat = this.findViewById(R.id.transportation_percentage);
        shopping_cat = this.findViewById(R.id.shopping_percentage);
        entertainment_cat = this.findViewById(R.id.entertainment_percentage);
        bills_cat = this.findViewById(R.id.bills_percentage);
        other_cat = this.findViewById(R.id.others_percentage);
        salary_cat = this.findViewById(R.id.salary_percentage);
        gift_cat = this.findViewById(R.id.gift_percentage);
        investment_cat = this.findViewById(R.id.investment_percentage);
        other_in_cat = this.findViewById(R.id.others_in_percentage);
        income_layout = this.findViewById(R.id.income_amount_layout);
        expense_layout = this.findViewById(R.id.expense_amount_layout);
        income_cat_layout = this.findViewById(R.id.income_cat_layout);
        expense_cat_layout = this.findViewById(R.id.expense_cat_layout);

        if (this.isTransactionUpdated) {
            this.requestUserInfo();
            this.requestWalletInfo();
            this.requestTransaction();
            this.requestExpenseCatInfo();
            try {
                Toast.makeText(mContext, "Updating info...", Toast.LENGTH_SHORT).show();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        expense_cat.addAll(Arrays.asList(
                new Pair<>(this.findViewById(R.id.food_cat), "food"),
                new Pair<>(this.findViewById(R.id.transportation_cat), "transportation"),
                new Pair<>(this.findViewById(R.id.shopping_cat), "shopping"),
                new Pair<>(this.findViewById(R.id.entertainment_cat), "entertainment"),
                new Pair<>(this.findViewById(R.id.bills_cat), "bills"),
                new Pair<>(this.findViewById(R.id.others_in_cat), "others_ex")
        ));
        expense_cat.forEach((item) -> item.first.setOnClickListener((view) -> {
            if (TransactionActivity.transactions.isEmpty()) {
                requestTransaction();
                try {
                    Toast.makeText(this, "Please wait...", Toast.LENGTH_SHORT).show();
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            viewDialog(item.second);
        }));

        income_cat.addAll(Arrays.asList(
                new Pair<>(this.findViewById(R.id.salary_cat), "salary"),
                new Pair<>(this.findViewById(R.id.gift_cat), "gift"),
                new Pair<>(this.findViewById(R.id.investment_cat), "investment"),
                new Pair<>(this.findViewById(R.id.others_in_cat), "others_in")
        ));
        income_cat.forEach((item) -> item.first.setOnClickListener((view) -> {
            if (TransactionActivity.transactions.isEmpty()) {
                requestTransaction();
                try {
                    Toast.makeText(this, "Please wait...", Toast.LENGTH_SHORT).show();
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            viewDialog(item.second);
        }));


        addTransaction = this.findViewById(R.id.addTransactionButton);

        this.bottomBar = this.findViewById(R.id.bottomNav);
        this.bottomBar.inflateMenu(R.menu.menu);
        this.bottomBar.setSelectedItemId(R.id.dashboard);
        this.bottomBar.setOnItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.dashboard:
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
                            ExpenseWiseToolClass.changeMenuIntent(this, AccountActivity.class);
                            this.finishAffinity();
                            break;
                    }
                    return true;
                }
        );
        if (this.uid.equals("u0")){
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
            finishAffinity();
        } else {
            if (user != null) setProfile();
            requestUserInfo();

            if (wallet != null) setWallet();
            requestWalletInfo();

            if(expenseCategories != null) sliceThePie();
            else requestExpenseCatInfo();

            if(TransactionActivity.transactions == null) requestTransaction();
        }

        addTransaction.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditTransactionActivity.class);
            intent.putExtra("action", "add");
            this.startActivity(intent);
        });

        income_layout.setOnClickListener(v -> {
            requestIncomeCatInfo();
            income_cat_layout.setVisibility(View.VISIBLE);
            expense_cat_layout.setVisibility(View.GONE);
        });

        expense_layout.setOnClickListener(v -> {
            requestExpenseCatInfo();
            income_cat_layout.setVisibility(View.GONE);
            expense_cat_layout.setVisibility(View.VISIBLE);
        });
    }

    // Function that request to the server
    protected User requestUserInfo() {
        mApiService.getUser().enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.code() == 401) {
                    requestLogout("Session Expired");
                    return;
                }
                UserResponse res = response.body();
                if (res == null) {
                    Gson gson = new Gson();
                    try {
                        UserResponse errorResponse = gson.fromJson(response.errorBody().string(), UserResponse.class);
                        res = errorResponse;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (res.isSuccess()) {
                    user = res.getUser();
                    setProfile();
                } else {
                    Toast.makeText(MainActivity.this, ""+res.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(MainActivity.this, "Failed to get user info", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
    protected Wallet requestWalletInfo() {
        mApiService.getWallet().enqueue(new Callback<WalletResponse>() {
            @Override
            public void onResponse(Call<WalletResponse> call, Response<WalletResponse> response) {
                if (response.code() == 401) {
                    requestLogout("Session Expired");
                    return;
                }
                WalletResponse res = response.body();
                if (res == null) {
                    Gson gson = new Gson();
                    try {
                        WalletResponse errorResponse = gson.fromJson(response.errorBody().string(), WalletResponse.class);
                        res = errorResponse;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (res.isSuccess()) {
                    wallet = res.getWallet();
                    setWallet();
                } else {
                    Toast.makeText(MainActivity.this, ""+res.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WalletResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(MainActivity.this, "Failed to get wallet info", Toast.LENGTH_SHORT).show();
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
    protected ExpenseCategory requestExpenseCatInfo() {
        mApiService.getExpenseCat().enqueue(new Callback<ExpenseCatResponse>() {
            @Override
            public void onResponse(Call<ExpenseCatResponse> call, Response<ExpenseCatResponse> response) {
                if (response.code() == 401) {
                    requestLogout("Session Expired");
                    return;
                }
                ExpenseCatResponse res = response.body();
                if (res == null) {
                    Gson gson = new Gson();
                    try {
                        ExpenseCatResponse errorResponse = gson.fromJson(response.errorBody().string(), ExpenseCatResponse.class);
                        res = errorResponse;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (res.isSuccess()) {
                    expenseCategories = res.getExpense_category();
                    sliceThePie();
                } else {
                    Toast.makeText(MainActivity.this, ""+res.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ExpenseCatResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(MainActivity.this, "Failed to get expense category info", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
    protected IncomeCategory requestIncomeCatInfo() {
        mApiService.getIncomeCat().enqueue(new Callback<IncomeCatResponse>() {
            @Override
            public void onResponse(Call<IncomeCatResponse> call, Response<IncomeCatResponse> response) {
                if (response.code() == 401) {
                    requestLogout("Session Expired");
                    return;
                }
                IncomeCatResponse res = response.body();
                if (res == null) {
                    Gson gson = new Gson();
                    try {
                        IncomeCatResponse errorResponse = gson.fromJson(response.errorBody().string(), IncomeCatResponse.class);
                        res = errorResponse;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (res.isSuccess()) {
                    incomeCategories = res.getIncome_category();
                    sliceThePie2();
                } else {
                    Toast.makeText(MainActivity.this, ""+res.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<IncomeCatResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(MainActivity.this, "Failed to get income category info", Toast.LENGTH_SHORT).show();
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
                    TransactionActivity.transactions = res.getTransactions();
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

    // function to implement the data to the view
    private void setProfile() {
        username.setText(user.getUsername());
        user_rank.setText("Lv. "+user.getUser_rank());
        user_points.setText(""+user.getPoints());
        if (user.getImg() != null) {
            Glide.with(mContext)
                    .load(user.getImg())
                    .circleCrop()
                    .into(profilePhoto);
        } else {
            profilePhoto.setVisibility(ImageView.GONE);
            initial.setVisibility(TextView.VISIBLE);
            initial.setText(user.getUsername().substring(0, 1));
        }
    }
    private void setWallet() {
        balance.setText(ExpenseWiseToolClass.formatRupiah(wallet.getBalance()));
        total_expense.setText(ExpenseWiseToolClass.formatRupiah(wallet.getTotal_expense()));
        total_income.setText(ExpenseWiseToolClass.formatRupiah(wallet.getTotal_income()));
    }
    private void sliceThePie() {
        pieChart.clearChart();
        expenseCategories.forEach(ec -> {
            String colorHex = "";
            switch (ec.getEc_name()){
                case "Others":
                    colorHex = getResources().getString(0+R.color.primary_700);
                    other_cat.setText(String.format("%.2f", ec.getPercentage()) + "%");
                    break;
                case "Food":
                    colorHex = getResources().getString(0+R.color.primary_600);
                    food_cat.setText(String.format("%.2f", ec.getPercentage()) + "%");
                    break;
                case "Transportation":
                    colorHex = getResources().getString(0+R.color.primary_500);
                    transport_cat.setText(String.format("%.2f", ec.getPercentage()) + "%");
                    break;
                case "Entertainment":
                    colorHex = getResources().getString(0+R.color.primary_400);
                    entertainment_cat.setText(String.format("%.2f", ec.getPercentage()) + "%");
                    break;
                case "Shopping":
                    colorHex = getResources().getString(0+R.color.primary_300);
                    shopping_cat.setText(String.format("%.2f", ec.getPercentage()) + "%");
                    break;
                case "Bills":
                    colorHex = getResources().getString(0+R.color.primary_200);
                    bills_cat.setText(String.format("%.2f", ec.getPercentage()) + "%");
                    break;

            }
            pieChart.addPieSlice(
                    new PieModel(
                            ec.getEc_name(),
                            ec.getPercentage(),
                            Color.parseColor(colorHex)
                    )
            );

        });

        pieChart.startAnimation();
    }
    private void sliceThePie2() {
        pieChart2.clearChart();
        incomeCategories.forEach(ic -> {
            String colorHex = "";

            switch (ic.getIc_name()){
                case "Others":
                    colorHex = getResources().getString(0+R.color.primary_700);
                    other_in_cat.setText(String.format("%.2f", ic.getPercentage()) + "%");
                    break;
                case "Salary":
                    colorHex = getResources().getString(0+R.color.secondary_600);
                    salary_cat.setText(String.format("%.2f", ic.getPercentage()) + "%");
                    break;
                case "Gift":
                    colorHex = getResources().getString(0+R.color.secondary_400);
                    gift_cat.setText(String.format("%.2f", ic.getPercentage()) + "%");
                    break;
                case "Investment":
                    colorHex = getResources().getString(0+R.color.secondary_200);
                    investment_cat.setText(String.format("%.2f", ic.getPercentage()) + "%");
                    break;
            }
            pieChart2.addPieSlice(
                    new PieModel(
                            ic.getIc_name(),
                            ic.getPercentage(),
                            Color.parseColor(colorHex)
                    )
            );

        });
        pieChart2.startAnimation();
    }

    private void viewDialog(String cat) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.transaction_cat_modal, null);

        dialog.setView(dialogView);
        dialog.setCancelable(true);

        ImageView ic_cat = dialogView.findViewById(R.id.ic_cat);
        TextView title_cat = dialogView.findViewById(R.id.title_cat);
        TextView total_per_cat = dialogView.findViewById(R.id.total_per_cat);
        TextView total_percent_cat = dialogView.findViewById(R.id.total_percent_cat);
        ConstraintLayout summary_cat_container = dialogView.findViewById(R.id.summary_cat_container);
        Boolean isExpense = true;

        ListView list_cat = dialogView.findViewById(R.id.listTransaction);
        ArrayList<Transaction> filteredTransaction = new ArrayList<>();
        AtomicReference<Integer> total_amount = new AtomicReference<>(0);
        switch (cat) {
            case "food" :
                ic_cat.setImageResource(R.drawable.food_cat);
                title_cat.setText("Expense : Food");
                filteredTransaction = (ArrayList<Transaction>) TransactionActivity.transactions.stream().filter(t -> Objects.equals(t.getT_category(), "Food")).collect(Collectors.toList());
                expenseCategories.stream().filter(ec -> Objects.equals(ec.getEc_name(), "Food")).forEach(ec -> total_percent_cat.setText(String.format("%.2f", ec.getPercentage()) + "%"));
                break;
            case "transportation" :
                ic_cat.setImageResource(R.drawable.transportation_cat);
                title_cat.setText("Expense : Transportation");
                filteredTransaction = (ArrayList<Transaction>) TransactionActivity.transactions.stream().filter(t -> Objects.equals(t.getT_category(), "Transportation")).collect(Collectors.toList());
                expenseCategories.stream().filter(ec -> Objects.equals(ec.getEc_name(), "Transportation")).forEach(ec -> total_percent_cat.setText(String.format("%.2f", ec.getPercentage()) + "%"));
                break;
            case "entertainment" :
                ic_cat.setImageResource(R.drawable.entertainment_cat);
                title_cat.setText("Expense : Entertainment");
                filteredTransaction = (ArrayList<Transaction>) TransactionActivity.transactions.stream().filter(t -> Objects.equals(t.getT_category(), "Entertainment")).collect(Collectors.toList());
                expenseCategories.stream().filter(ec -> Objects.equals(ec.getEc_name(), "Entertainment")).forEach(ec -> total_percent_cat.setText(String.format("%.2f", ec.getPercentage()) + "%"));
                break;
            case "shopping" :
                ic_cat.setImageResource(R.drawable.shopping_cat);
                title_cat.setText("Expense : Shopping");
                filteredTransaction = (ArrayList<Transaction>) TransactionActivity.transactions.stream().filter(t -> Objects.equals(t.getT_category(), "Shopping")).collect(Collectors.toList());
                expenseCategories.stream().filter(ec -> Objects.equals(ec.getEc_name(), "Shopping")).forEach(ec -> total_percent_cat.setText(String.format("%.2f", ec.getPercentage()) + "%"));
                break;
            case "bills" :
                ic_cat.setImageResource(R.drawable.bills_cat);
                title_cat.setText("Expense : Bills");
                filteredTransaction = (ArrayList<Transaction>) TransactionActivity.transactions.stream().filter(t -> t.getT_category() == "Bills").collect(Collectors.toList());
                expenseCategories.stream().filter(ec -> ec.getEc_name() == "Bills").forEach(ec -> total_percent_cat.setText(String.format("%.2f", ec.getPercentage()) + "%"));
                break;
            case "others_ex" :
                ic_cat.setImageResource(R.drawable.others_cat);
                title_cat.setText("Expense : Others");
                filteredTransaction = (ArrayList<Transaction>) TransactionActivity.transactions.stream().filter(t -> Objects.equals(t.getT_category(), "Others") && t.getTid().startsWith("i")).collect(Collectors.toList());
                incomeCategories.stream().filter(ic -> Objects.equals(ic.getIc_name(), "Others")).forEach(ic -> total_percent_cat.setText(String.format("%.2f", ic.getPercentage()) + "%"));
                break;
            case "salary" :
                ic_cat.setImageResource(R.drawable.salary_cat);
                title_cat.setText("Income : Salary");
                filteredTransaction = (ArrayList<Transaction>) TransactionActivity.transactions.stream().filter(t -> Objects.equals(t.getT_category(), "Salary")).collect(Collectors.toList());
                incomeCategories.stream().filter(ic -> Objects.equals(ic.getIc_name(), "Salary")).forEach(ic -> total_percent_cat.setText(String.format("%.2f", ic.getPercentage()) + "%"));
                isExpense = false;
                break;
            case "gift" :
                ic_cat.setImageResource(R.drawable.gift_cat);
                title_cat.setText("Income : Gift");
                filteredTransaction = (ArrayList<Transaction>) TransactionActivity.transactions.stream().filter(t -> Objects.equals(t.getT_category(), "Gift")).collect(Collectors.toList());
                incomeCategories.stream().filter(ic -> Objects.equals(ic.getIc_name(), "Gift")).forEach(ic -> total_percent_cat.setText(String.format("%.2f", ic.getPercentage()) + "%"));
                isExpense = false;
                break;
            case "investment" :
                ic_cat.setImageResource(R.drawable.investment_cat);
                title_cat.setText("Income : Investment");
                filteredTransaction = (ArrayList<Transaction>) TransactionActivity.transactions.stream().filter(t -> Objects.equals(t.getT_category(), "Investment")).collect(Collectors.toList());
                incomeCategories.stream().filter(ic -> Objects.equals(ic.getIc_name(), "Investment")).forEach(ic -> total_percent_cat.setText(String.format("%.2f", ic.getPercentage()) + "%"));
                isExpense = false;
                break;
            case "others_in" :
                ic_cat.setImageResource(R.drawable.others_cat);
                title_cat.setText("Income : Others");
                filteredTransaction = (ArrayList<Transaction>) TransactionActivity.transactions.stream().filter(t -> Objects.equals(t.getT_category(), "Others") && t.getTid().startsWith("i")).collect(Collectors.toList());
                incomeCategories.stream().filter(ic -> Objects.equals(ic.getIc_name(), "Others")).forEach(ec -> total_percent_cat.setText(String.format("%.2f", ec.getPercentage()) + "%"));
                isExpense = false;
                break;
        }
        if (isExpense) summary_cat_container.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_secondary)));
        else summary_cat_container.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_secondary)));

        filteredTransaction.forEach(t -> total_amount.updateAndGet(v -> v + t.getT_amount()));
        total_per_cat.setText(ExpenseWiseToolClass.formatRupiah(total_amount.get()));
        list_cat.setAdapter(new TransactionListAdapter(mContext, filteredTransaction, true));

        dialog.show();
    }

}