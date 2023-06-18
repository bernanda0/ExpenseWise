package com.client.expensewise;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.client.expensewise.controller.BaseApiService;
import com.client.expensewise.controller.UtilsApi;
import com.client.expensewise.model.Expense;
import com.client.expensewise.model.Income;
import com.client.expensewise.model.Transaction;
import com.client.expensewise.model.response.ExpenseResponse;
import com.client.expensewise.model.response.IncomeResponse;
import com.client.expensewise.model.response.ParseResponse;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditTransactionActivity extends AppCompatActivity {
    SharedPreferences preferences;
    BaseApiService mApiService;
    Context mContext;
    String action = null;
    String selectedTid = null;
    RadioGroup catRadioGroup = null;
    AutoCompleteTextView chooseCat = null;
    CheckBox nowDate = null;
    Button saveEditButton = null;
    TextInputLayout dateLayout = null;
    TextInputEditText dateText = null;
    TextInputEditText amountText = null;
    TextInputEditText descriptionText = null;
    ConstraintLayout uploadImage = null;
    ImageView uploadedImage = null;
    TextView up_img_text = null;
    ImageView up_img_icon = null;
    LinearLayout parse_action_layout = null;
    Button parse_button, discard_button;

    Integer totalAmount;
    String selectedCategory;
    String selectedDate;
    String description;

    ArrayList<CustomPair> expenseCatList = new ArrayList<>();
    ArrayList<CustomPair> incomeCatList = new ArrayList<>();
    ArrayAdapter<CustomPair> adapter;

    String path = null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            path = RealPathUtil.getRealPath(AddEditTransactionActivity.this, uri);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            uploadedImage.setVisibility(View.VISIBLE);
            uploadedImage.setImageBitmap(bitmap);
            up_img_text.setText("Change the image or remove it");
            parse_action_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_transaction);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mContext = this;
        mApiService = UtilsApi.getApiService(mContext);
        catRadioGroup = this.findViewById(R.id.radioGroup);
        chooseCat = this.findViewById(R.id.transaction_category_dropdown);
        nowDate = this.findViewById(R.id.checkBox);
        dateText = this.findViewById(R.id.selectedDateText);
        amountText = this.findViewById(R.id.transaction_amount);
        dateLayout = this.findViewById(R.id.transaction_date);
        descriptionText = this.findViewById(R.id.transaction_description);
        saveEditButton = this.findViewById(R.id.transaction_save_button);
        uploadImage = this.findViewById(R.id.upload_image);
        uploadedImage = this.findViewById(R.id.uploadedImage);
        up_img_text = this.findViewById(R.id.up_img_text);
        up_img_icon = this.findViewById(R.id.up_img_icon);
        parse_action_layout = this.findViewById(R.id.parse_action_layout);
        parse_button = this.findViewById(R.id.parse_button);
        discard_button = this.findViewById(R.id.discard_button);

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                   Intent intent = new Intent();
                   intent.setType("image/*");
                   intent.setAction(Intent.ACTION_GET_CONTENT);
                   startActivityForResult(intent, 10);
               } else {
                   ActivityCompat.requestPermissions(AddEditTransactionActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
               }
            }
        });

        expenseCatList.addAll(Arrays.asList(
                new CustomPair("ec1", "Others"),
                new CustomPair("ec2", "Food"),
                new CustomPair("ec3", "Transportation"),
                new CustomPair("ec4", "Entertainment"),
                new CustomPair("ec5", "Shopping"),
                new CustomPair("ec6", "Bills")
        ));

        incomeCatList.addAll(Arrays.asList(
                new CustomPair("ic1", "Others"),
                new CustomPair("ic2", "Salary"),
                new CustomPair("ic3", "Gift"),
                new CustomPair("ic4", "Investment")
        ));


        this.setSupportActionBar(findViewById(R.id.toolbar1));

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.action = getIntent().getStringExtra("action");
        this.selectedTid = getIntent().getStringExtra("tid");

        if (this.action.equals("add")) {
            this.getSupportActionBar().setTitle("Add Transaction");
            this.saveEditButton.setText("Add");
        } else if (this.action.equals("edit")) {
            this.getSupportActionBar().setTitle("Edit Transaction");
            this.saveEditButton.setText("Update");
            Transaction selectedTransaction = null;
            for (Transaction t : TransactionActivity.transactions) {
                if (t.getTid().equals(this.selectedTid)) {
                    selectedTransaction = t;
                    break;
                }
            }
            if (selectedTransaction != null) {
                if (selectedTransaction.getTid().startsWith("i")) {
                    catRadioGroup.check(R.id.radioButtonIncome);
                    adapter = new ArrayAdapter(mContext, R.layout.dropdown_cat, incomeCatList);
                } else {
                    catRadioGroup.check(R.id.radioButtonExpense);
                    adapter = new ArrayAdapter(mContext, R.layout.dropdown_cat, expenseCatList);
                }
                chooseCat.setText(selectedTransaction.getT_category());
                dateText.setText(ExpenseWiseToolClass.formatDateString(selectedTransaction.getT_date()));
                amountText.setText(selectedTransaction.getT_amount().toString());
                descriptionText.setText(selectedTransaction.getT_description());
            }
        }

        catRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonExpense) {
                    adapter = new ArrayAdapter(mContext, R.layout.dropdown_cat, expenseCatList);
                } else if (checkedId == R.id.radioButtonIncome) {
                    adapter = new ArrayAdapter(mContext, R.layout.dropdown_cat, incomeCatList);
                }
            }
        });

        chooseCat.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                if (adapter == null) {
                    Toast.makeText(mContext, "Please select a category", Toast.LENGTH_SHORT).show();
                } else {
                    chooseCat.setAdapter(adapter);
                    chooseCat.showDropDown();
                }
                return false;
            }
        });

        chooseCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = adapter.getItem(position).first;
            }
        });

        nowDate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedDate = ExpenseWiseToolClass.formatDate(Calendar.getInstance());
                dateText.setText(selectedDate);
            } else {
                selectedDate = null;
                dateText.setText("Date");
            }
        });

        saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalAmount = Integer.parseInt(amountText.getText().toString());
                description = descriptionText.getText().toString();
                if (validForm()) {
                    if (selectedCategory.startsWith("e") && action.equals("add")) {
                        Toast.makeText(mContext, "Adding expense...", Toast.LENGTH_SHORT).show();
                        requestInsertExpense();
                    } else if (selectedCategory.startsWith("i") && action.equals("add")) {
                        Toast.makeText(mContext, "Adding income...", Toast.LENGTH_SHORT).show();
                        requestInsertIncome();
                    } else if (selectedCategory.startsWith("e") && action.equals("edit")) {
                        Toast.makeText(mContext, "Updating expense...", Toast.LENGTH_SHORT).show();
                        requestUpdateExpense();
                    } else if (selectedCategory.startsWith("i") && action.equals("edit")) {
                        Toast.makeText(mContext, "Updating income...", Toast.LENGTH_SHORT).show();
                        requestUpdateIncome();
                    }
                }
            }
        });

        discard_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parse_action_layout.setVisibility(View.GONE);
                uploadedImage.setVisibility(View.GONE);
                up_img_text.setText("Upload receipt (optional)");
            }
        });

        parse_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Parsing receipt...", Toast.LENGTH_SHORT).show();
                requestReceiptParser();
            }
        });

        dateText.setOnClickListener( v -> {
            AlertDialog dialog = new AlertDialog.Builder(mContext).create();
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_date_setter, null);
            TextInputLayout editTextDate = dialogView.findViewById(R.id.editTextDate);
            TextInputLayout editTextTime = dialogView.findViewById(R.id.editTextTime);
            Button saveDate = dialogView.findViewById(R.id.buttonSaveDate);
            CheckBox usingTime = dialogView.findViewById(R.id.checkBoxUsingTime);
            AtomicReference<Boolean> isUsingTime = new AtomicReference<>(true);
            AtomicReference<String> theDate = new AtomicReference<>();
            AtomicReference<String> theTime = new AtomicReference<>();

            editTextDate.getEditText().setOnClickListener(t -> {
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker().setTheme(R.style.MaterialCalendarTheme);
                builder.setTitleText("Select Date");
                builder.setCalendarConstraints(new CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now()).build());
                MaterialDatePicker datePicker = builder.build();
                datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
                datePicker.addOnPositiveButtonClickListener(selection -> {
                    String date = ExpenseWiseToolClass.longToDate((Long) selection);
                    editTextDate.getEditText().setText(date);
                    theDate.set(date);
                });
            });

            usingTime.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    isUsingTime.set(true);
                } else {
                    isUsingTime.set(false);
                    editTextTime.getEditText().setText("00:00:00");
                    theTime.set("00:00:00");
                }
            });
            if (isUsingTime.get()) {
                editTextTime.getEditText().setOnClickListener(t -> {
                    MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder().setTheme(R.style.MaterialTimePickerTheme);
                    builder.setTitleText("Select Time");
                    MaterialTimePicker timePicker = builder.build();
                    timePicker.show(getSupportFragmentManager(), "TIME_PICKER");
                    timePicker.addOnPositiveButtonClickListener(selection -> {
                        String time = timePicker.getHour()+":"+timePicker.getMinute();
                        String formattedTime = ExpenseWiseToolClass.StringToTime(time);
                        editTextTime.getEditText().setText(formattedTime);
                        theTime.set(formattedTime);
                    });
                });
            }
            saveDate.setOnClickListener(t -> {
                Toast.makeText(mContext, "Date saved", Toast.LENGTH_SHORT).show();
                selectedDate = theDate.get() + " " + theTime.get();
                dateText.setText(selectedDate);
                dialog.dismiss();
            });
            dialog.setView(dialogView);
            dialog.setCancelable(true);
            dialog.show();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void requestReceiptParser() {
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imgBody = MultipartBody.Part.createFormData("receipt", file.getName(), requestFile);
        System.out.println("IMG BODY : " + imgBody);
        mApiService.receiptParse(imgBody).enqueue(new Callback<ParseResponse>() {
            @Override
            public void onResponse(Call<ParseResponse> call, Response<ParseResponse> response) {
                ParseResponse res = response.body();
//                if (res == null) {
//                    Gson gson = new Gson();
//                    try {
//                        ParseResponse errorResponse = gson.fromJson(response.errorBody().string(), ParseResponse.class);
//                        res = errorResponse;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
                System.out.println("PARSE RES : " + response);
                if (res != null) {
                    Toast.makeText(mContext, "Parsing successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Parsing failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ParseResponse> call, Throwable t) {
                Toast.makeText(mContext, "Parsing failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected Income requestInsertIncome() {
        mApiService.insertIncome(selectedCategory, totalAmount, selectedDate, description).enqueue(new Callback<IncomeResponse>() {
            @Override
            public void onResponse(Call<IncomeResponse> call, Response<IncomeResponse> response) {
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
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (res.isSuccess()) {
                    Toast.makeText(mContext, "Income added successfully", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mContext, "Failed to add income", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
    protected Income requestUpdateIncome() {
        mApiService.updateIncome(selectedTid, selectedCategory, totalAmount, selectedDate, description).enqueue(new Callback<IncomeResponse>() {
            @Override
            public void onResponse(Call<IncomeResponse> call, Response<IncomeResponse> response) {
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
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (res.isSuccess()) {
                    Toast.makeText(mContext, "Income updated successfully", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mContext, "Failed to update income", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
    protected Expense requestInsertExpense() {
        mApiService.insertExpense(selectedCategory, totalAmount, selectedDate, description).enqueue(new Callback<ExpenseResponse>() {
            @Override
            public void onResponse(Call<ExpenseResponse> call, Response<ExpenseResponse> response) {
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
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (res.isSuccess()) {
                    Toast.makeText(mContext, "Expense added successfully", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mContext, "Failed to add expense", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
    protected Expense requestUpdateExpense() {
        mApiService.updateExpense(selectedTid, selectedCategory, totalAmount, selectedDate, description).enqueue(new Callback<ExpenseResponse>() {
            @Override
            public void onResponse(Call<ExpenseResponse> call, Response<ExpenseResponse> response) {
                System.out.println("#RESPONSE " + response);
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
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (res.isSuccess()) {
                    Toast.makeText(mContext, "Expense updated successfully", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mContext, "Failed to update expense", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }

    private class CustomPair extends Pair<String, String> {
        public CustomPair(String first, String second) {
            super(first, second);
        }

        @Override
        public String toString() {
            return this.second;
        }
    }

    private boolean validForm() {
        boolean valid = true;
        if (selectedCategory == null) {
            valid = false;
            Toast.makeText(mContext, "Please select a category", Toast.LENGTH_SHORT).show();
        }
        if (selectedDate == null) {
            valid = false;
            Toast.makeText(mContext, "Please select a date", Toast.LENGTH_SHORT).show();
        }
        if (totalAmount == null) {
            valid = false;
            Toast.makeText(mContext, "Please enter an amount", Toast.LENGTH_SHORT).show();
        }
        if (description == null) {
            valid = false;
            Toast.makeText(mContext, "Please enter a description", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }
}