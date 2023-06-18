package com.client.expensewise;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.client.expensewise.controller.BaseApiService;
import com.client.expensewise.controller.UtilsApi;
import com.client.expensewise.model.Goal;
import com.client.expensewise.model.Transaction;
import com.client.expensewise.model.response.GoalResponse;
import com.client.expensewise.model.response.UserResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoalActivity extends AppCompatActivity {
    BottomNavigationView bottomBar;
    ListView listTransaction;
    BaseApiService mApiService;
    Context mContext;
    LinearLayout noOngoingGoal, deleteGoal, editGoal;
    ImageView addGoal;
    ConstraintLayout goalCard;
    TextView textStartDate;

    TextView goal_percentage, start_period, end_period, goal_expense, p_expense, goal_status;

    static ArrayList<Goal> goals = new ArrayList<>();
    static Goal ongoingGoal = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        mContext = this;
        mApiService = UtilsApi.getApiService(mContext);
        noOngoingGoal = this.findViewById(R.id.no_ongoing_goal);
        goalCard = this.findViewById(R.id.goal_card);
        goal_percentage = this.findViewById(R.id.goal_percentage);
        start_period = this.findViewById(R.id.start_period);
        end_period = this.findViewById(R.id.end_period);
        goal_expense = this.findViewById(R.id.goal_expense);
        p_expense = this.findViewById(R.id.p_expense);
        goal_status = this.findViewById(R.id.goal_status_text);
        textStartDate = this.findViewById(R.id.text_start_date);
        deleteGoal = this.findViewById(R.id.delete_goal);
        editGoal = this.findViewById(R.id.edit_goal);
        addGoal = this.findViewById(R.id.add_goal);

        this.bottomBar = this.findViewById(R.id.bottomNav);
        this.listTransaction = this.findViewById(R.id.listTransaction);
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

        requestGoal();

        // create dialog yes no
        deleteGoal.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Delete Goal");
            builder.setMessage("Are you sure you want to delete this goal?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                deleteGoal();
                dialog.dismiss();
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.show();
        });

        addGoal.setOnClickListener(v -> {
            showAddEditDialog("add");
        });
        editGoal.setOnClickListener(v -> {
            showAddEditDialog("edit");
        });
    }

    private void showAddEditDialog(String type) {
        AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_edit_goal, null);

        Button goal_save_button = dialogView.findViewById(R.id.goal_save_button);
        TextView goal_action = dialogView.findViewById(R.id.goal_action);
        TextInputEditText goal_expense = dialogView.findViewById(R.id.goal_expense_amount);
        TextInputEditText goal_end_period = dialogView.findViewById(R.id.goal_end_period);

        if (type.equals("add")) {
            goal_action.setText("Add Goal");
            goal_save_button.setText("Add Goal");
        } else {
            goal_action.setText("Edit Goal");
            goal_save_button.setText("Update Goal");
            goal_expense.setText(ongoingGoal.getGoal_expense().toString());
            goal_end_period.setText(ExpenseWiseToolClass.formatOnlyDate(ongoingGoal.getEnd_period()));
        }

        goal_end_period.setOnClickListener(v -> {
            MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker().setTheme(R.style.MaterialCalendarTheme);
            builder.setTitleText("Select Date");
            builder.setCalendarConstraints(new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now()).build());
            MaterialDatePicker datePicker = builder.build();
            datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            datePicker.addOnPositiveButtonClickListener(selection -> {
                String date = ExpenseWiseToolClass.longToDate((Long) selection);
                goal_end_period.setText(date);
            });
        });

        goal_save_button.setOnClickListener(v -> {
            if (goal_expense.getText().toString().isEmpty()) {
                goal_expense.setError("Please enter goal expense");
                goal_expense.requestFocus();
                return;
            }
            if (goal_end_period.getText().toString().isEmpty()) {
                goal_end_period.setError("Please enter goal end period");
                goal_end_period.requestFocus();
                return;
            }
            Integer goalExpense = Integer.parseInt(goal_expense.getText().toString());
            if (type.equals("add")) {
                createGoal(goalExpense, String.valueOf(goal_end_period.getText()));
            } else {
                updateGoal(goalExpense, String.valueOf(goal_end_period.getText()));
            }
            dialog.dismiss();
        });
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.show();
    }

    protected Goal requestGoal() {
        mApiService.getGoal().enqueue(new Callback<GoalResponse>() {
            @Override
            public void onResponse(Call<GoalResponse> call, Response<GoalResponse> response) {
                GoalResponse res = response.body();
                System.out.println("RES : "+response);
                if (res == null) {
                    Gson gson = new Gson();
                    try {
                        GoalResponse errorResponse = gson.fromJson(response.errorBody().string(), GoalResponse.class);
                        res = errorResponse;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (res.isSuccess()) {
                    goals = res.getGoal();
                    if (goals.size() != 0) {
                        ongoingGoal = goals.stream().filter(goal -> goal.getStatus().equals("ongoing")).findFirst().orElse(null);
                    }
                    setGoalView();
                } else {
                    Toast.makeText(mContext, ""+res.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GoalResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(mContext, "Cannot get user goal", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
    protected Goal createGoal(Integer ge, String sd) {
        mApiService.insertGoal(ge, sd).enqueue(new Callback<GoalResponse>() {
            @Override
            public void onResponse(Call<GoalResponse> call, Response<GoalResponse> response) {
                GoalResponse res = response.body();
                if (res == null) {
                    Gson gson = new Gson();
                    try {
                        GoalResponse errorResponse = gson.fromJson(response.errorBody().string(), GoalResponse.class);
                        res = errorResponse;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (res.isSuccess()) {
                    Toast.makeText(mContext, "Goal created successfully", Toast.LENGTH_SHORT).show();
                    requestGoal();
                } else {
                    Toast.makeText(mContext, ""+res.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GoalResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(mContext, "Cannot create goal", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
    protected Goal updateGoal(Integer ge, String sd) {
        mApiService.updateGoal(ongoingGoal.getUgid(), ge, sd).enqueue(new Callback<GoalResponse>() {
            @Override
            public void onResponse(Call<GoalResponse> call, Response<GoalResponse> response) {
                GoalResponse res = response.body();
                if (res == null) {
                    Gson gson = new Gson();
                    try {
                        GoalResponse errorResponse = gson.fromJson(response.errorBody().string(), GoalResponse.class);
                        res = errorResponse;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (res.isSuccess()) {
                    Toast.makeText(mContext, "Goal updated successfully", Toast.LENGTH_SHORT).show();
                    requestGoal();
                } else {
                    Toast.makeText(mContext, ""+res.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GoalResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(mContext, "Cannot update goal", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
    protected Goal deleteGoal() {
        mApiService.deleteGoal(ongoingGoal.getUgid()).enqueue(new Callback<GoalResponse>() {
            @Override
            public void onResponse(Call<GoalResponse> call, Response<GoalResponse> response) {
                GoalResponse res = response.body();
                if (res == null) {
                    Gson gson = new Gson();
                    try {
                        GoalResponse errorResponse = gson.fromJson(response.errorBody().string(), GoalResponse.class);
                        res = errorResponse;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (res.isSuccess()) {
                    Toast.makeText(mContext, "Goal deleted successfully", Toast.LENGTH_SHORT).show();
                    ongoingGoal = null;
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, ""+res.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GoalResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(mContext, "Cannot delete user goal", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }


    private void setGoalView() {
        if (ongoingGoal != null) {
            noOngoingGoal.setVisibility(LinearLayout.GONE);
            goalCard.setVisibility(ConstraintLayout.VISIBLE);
            double percentage = (1.0 * ongoingGoal.getP_expense() / ongoingGoal.getGoal_expense()) * 100;
            goal_percentage.setText(ExpenseWiseToolClass.formatPercentage(percentage));
            String startPeriod = ExpenseWiseToolClass.formatOnlyDate(ongoingGoal.getStart_period());
            start_period.setText(startPeriod);
            end_period.setText(ExpenseWiseToolClass.formatOnlyDate(ongoingGoal.getEnd_period()));
            p_expense.setText(ExpenseWiseToolClass.formatRupiah(ongoingGoal.getP_expense()));
            goal_expense.setText(ExpenseWiseToolClass.formatRupiah(ongoingGoal.getGoal_expense()));
            goal_status.setText(ongoingGoal.getStatus());
            textStartDate.setText("All transactions after "+startPeriod);

            ArrayList<Transaction> filteredTransactions = new ArrayList<>();
            String start = ongoingGoal.getStart_period(); //get all the transactions after this date
            String end = ongoingGoal.getEnd_period(); //get all the transactions before this date
            TransactionActivity.transactions.forEach(transaction -> {
                Date transactionDate = ExpenseWiseToolClass.convertStringToDate2(transaction.getT_date());
                Date startDate = ExpenseWiseToolClass.convertStringToDate2(start);
                Date endDate = ExpenseWiseToolClass.convertStringToDate2(end);
                if (transactionDate.after(startDate) && transactionDate.before(endDate)) {
                    filteredTransactions.add(transaction);
                }
            });
            listTransaction.setAdapter(new TransactionListAdapter(this, filteredTransactions, false));
        } else {
            noOngoingGoal.setVisibility(LinearLayout.VISIBLE);
            goalCard.setVisibility(ConstraintLayout.GONE);
            textStartDate.setText("No goal set");
        }
    }

    private void updateListTransaction() {
        ArrayList<Transaction> filteredTransactions = new ArrayList<>();
        String start = ongoingGoal.getStart_period(); //get all the transactions after this date
        String end = ongoingGoal.getEnd_period(); //get all the transactions before this date
        TransactionActivity.transactions.forEach(transaction -> {
            Date transactionDate = ExpenseWiseToolClass.convertStringToDate(ExpenseWiseToolClass.formatDateString(transaction.getT_date()));
            Date startDate = ExpenseWiseToolClass.convertStringToDate(start);
            Date endDate = ExpenseWiseToolClass.convertStringToDate(end);
            if (transactionDate.after(startDate) && transactionDate.before(endDate)) {
                filteredTransactions.add(transaction);
            }
        });
        listTransaction.setAdapter(new TransactionListAdapter(this, filteredTransactions, false));
    }
}