package com.client.expensewise;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.client.expensewise.controller.BaseApiService;
import com.client.expensewise.controller.UtilsApi;
import com.client.expensewise.model.Expense;
import com.client.expensewise.model.Transaction;
import com.client.expensewise.model.response.ExpenseResponse;
import com.client.expensewise.model.response.IncomeResponse;
import com.client.expensewise.model.response.TransactionResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

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
    ImageView filter_button;

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
        filter_button = this.findViewById(R.id.filter_button);

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
        else updateListTransaction(transactions);

        addTransaction.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditTransactionActivity.class);
            intent.putExtra("action", "add");
            this.startActivity(intent);
        });

        filter_button.setOnClickListener(v -> {
            viewFilterDialog();
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
                    updateListTransaction(transactions);
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
    protected void requestDeleteIncome(String iid) {
        mApiService.deleteIncome(iid).enqueue(new Callback<IncomeResponse>() {
            @Override
            public void onResponse(Call<IncomeResponse> call, Response<IncomeResponse> response) {
                if (response.code() == 401) {
                    requestLogout("Session Expired");
                    return;
                }
                IncomeResponse res = response.body();
                if (res == null) {
                    Gson gson = new Gson();
                    try {
                        IncomeResponse errorResponse = gson.fromJson(response.errorBody().string(), IncomeResponse.class);
                        res = errorResponse;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (res.isSuccess()) {
                    Toast.makeText(mContext, "Income deleted successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("transaction_updated", true);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, ""+res.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<IncomeResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(mContext, "Failed to delete transaction", Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void requestDeleteExpense(String eid) {
        mApiService.deleteExpense(eid).enqueue(new Callback<ExpenseResponse>() {
            @Override
            public void onResponse(Call<ExpenseResponse> call, Response<ExpenseResponse> response) {
                if (response.code() == 401) {
                    requestLogout("Session Expired");
                    return;
                }
                ExpenseResponse res = response.body();
                if (res == null) {
                    Gson gson = new Gson();
                    try {
                        ExpenseResponse errorResponse = gson.fromJson(response.errorBody().string(), ExpenseResponse.class);
                        res = errorResponse;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (res.isSuccess()) {
                    Toast.makeText(mContext, "Expense deleted successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("transaction_updated", true);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, ""+res.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ExpenseResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(mContext, "Failed to delete transaction", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateListTransaction(ArrayList<Transaction> ts) {
        listTransaction.setAdapter(new TransactionListAdapter(mContext, ts, false));
        listTransaction.setOnItemClickListener((parent, view, position, id) -> {
            Transaction transaction = ts.get(position);
            viewTransactionModal(transaction);
        });
    }
    private void viewTransactionModal(Transaction t) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.transaction_info_card, null);

        dialog.setView(dialogView);
        dialog.setCancelable(true);

        ShapeableImageView icon_transaction = dialogView.findViewById(R.id.icon_transaction2);
        LinearLayout ll_percentage = dialogView.findViewById(R.id.linearLayoutPercentage);
        LinearLayout ll_category = dialogView.findViewById(R.id.linearLayoutLevel);
        TextView info_for_expense = dialogView.findViewById(R.id.info_for_expense);

        TextView transactionAmount = dialogView.findViewById(R.id.transaction_amount2);
        TextView transactionDate = dialogView.findViewById(R.id.transaction_date2);
        TextView transactionCategory = dialogView.findViewById(R.id.transaction_category2);
        TextView transactionDescription = dialogView.findViewById(R.id.transaction_description2);
        TextView transactionPercentage = dialogView.findViewById(R.id.transaction_percentage2);
        TextView transactionLevel = dialogView.findViewById(R.id.transaction_level2);

        transactionAmount.setText(ExpenseWiseToolClass.formatRupiah(t.getT_amount()));
        transactionDate.setText(ExpenseWiseToolClass.formatDateString(t.getT_date()));
        transactionCategory.setText(t.getT_category());
        transactionDescription.setText(t.getT_description());

        if (t.getTid().startsWith("i")) {
            icon_transaction.setImageResource(R.drawable.ic_income);
            ll_percentage.setVisibility(View.GONE);
            ll_category.setVisibility(View.GONE);
            info_for_expense.setVisibility(View.GONE);
        } else {
            icon_transaction.setImageResource(R.drawable.ic_expense);
            ll_percentage.setVisibility(View.VISIBLE);
            ll_category.setVisibility(View.VISIBLE);
            info_for_expense.setVisibility(View.VISIBLE);
            transactionPercentage.setText(String.format("%.2f", t.getT_percentage()) + "%");
            transactionLevel.setText(t.getT_expense_level());
        }

        dialogView.findViewById(R.id.edit_transaction).setOnClickListener(
                v -> {
                    Intent intent = new Intent(mContext, AddEditTransactionActivity.class);
                    intent.putExtra("action", "edit");
                    intent.putExtra("tid", t.getTid());
                    mContext.startActivity(intent);
                }
        );

        dialogView.findViewById(R.id.delete_transaction).setOnClickListener(
                v -> {
                    AlertDialog.Builder dialog2 = new AlertDialog.Builder(mContext);
                    dialog2.setTitle("Delete Transaction");
                    dialog2.setMessage("Are you sure you want to delete this transaction?");
                    dialog2.setPositiveButton("Yes", (dialog1, which) -> {
                        if (t.getTid().startsWith("i")) {
                            requestDeleteIncome(t.getTid());
                        } else {
                            requestDeleteExpense(t.getTid());
                        }
                    });
                    dialog2.setNegativeButton("No", (dialog1, which) -> dialog1.dismiss());
                    dialog2.show();
                }
        );

        dialog.show();
    }
    private void viewFilterDialog() {
        AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.filter_dialog, null);

        RadioGroup rg_periode = dialogView.findViewById(R.id.rg_period);
        RadioGroup rg_sort = dialogView.findViewById(R.id.rg_sorting_by);
        MaterialButton apply_filter = dialogView.findViewById(R.id.apply_filter);
        MaterialButton reset_filter = dialogView.findViewById(R.id.reset_filter);

        AtomicReference<Pair<LocalDateTime, LocalDateTime>> dateRange = new AtomicReference<>(new Pair<>(null, null));;
        rg_periode.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_today:
                    dateRange.set(getToday());
                    break;
                case R.id.rb_this_week:
                    dateRange.set(getThisWeek());
                    break;
                case R.id.rb_this_month:
                    dateRange.set(getThisMonth());
                    break;
            }
        });

        AtomicReference<Integer> mode = new AtomicReference<>(0);
        rg_sort.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_highest_amount:
                    mode.set(1);
                    break;
                case R.id.rb_lowest_amount:
                    mode.set(2);
                    break;
                case R.id.rb_expense_level:
                    mode.set(3);
                    break;

            }
        });

        apply_filter.setOnClickListener(v -> {
            Toast.makeText(mContext, ""+dateRange.get().first+" "+dateRange.get().second+" "+mode.get(), Toast.LENGTH_SHORT).show();
            if (dateRange.get().first == null && dateRange.get().second == null && mode.get() == 0) {
                Toast.makeText(mContext, "Please select filter", Toast.LENGTH_SHORT).show();
            } else {
                ArrayList<Transaction> filtered = new ArrayList<>();
                TransactionActivity.transactions.forEach(t -> {
                    LocalDateTime date = ExpenseWiseToolClass.convertStringToLDT(t.getT_date());
                    if (dateRange.get().first == null && dateRange.get().second == null) {
                        filtered.add(t);
                    } else {
                        if (date.isAfter(dateRange.get().first) && date.isBefore(dateRange.get().second)) {
                            filtered.add(t);
                        }
                    }
                });
                switch (mode.get()) {
                    case 0:
                        break;
                    case 1:
                        filtered.sort((o1, o2) -> (int) (o2.getT_amount() - o1.getT_amount()));
                        break;
                    case 2:
                        filtered.sort((o1, o2) -> (int) (o1.getT_amount() - o2.getT_amount()));
                        break;
                    case 3:
                        filtered.removeIf(t -> t.getTid().startsWith("i"));
                        filtered.sort((o1, o2) -> (int) (o2.getT_percentage() - o1.getT_percentage()));
                        break;
                }
                updateListTransaction(filtered);
                dialog.dismiss();
            }
        });

        reset_filter.setOnClickListener(v -> {
            updateListTransaction(TransactionActivity.transactions);
            dialog.dismiss();
        });

        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.show();
    }

    private Pair<LocalDateTime, LocalDateTime> getToday() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
        return new Pair<>(startOfDay, endOfDay);
    }

    private Pair<LocalDateTime, LocalDateTime> getThisWeek() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1).toLocalDate().atStartOfDay();
        LocalDateTime endOfWeek = startOfWeek.plusWeeks(1).minusNanos(1);
        return new Pair<>(startOfWeek, endOfWeek);
    }

    private Pair<LocalDateTime, LocalDateTime>  getThisMonth() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);
        return new Pair<>(startOfMonth, endOfMonth);
    }
}