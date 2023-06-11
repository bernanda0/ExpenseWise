package com.client.expensewise;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExpenseWiseToolClass {
    static void changeMenuIntent(Context ctx, Class cls) {
        Intent intent = new Intent(ctx, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        ctx.startActivity(intent);
    }
    //format so that the output will be like Rp99.999.999
    static String formatRupiah(Integer amount) {
        return DecimalFormat.getCurrencyInstance(new Locale("in", "ID")).format(amount);
    }
    static String formatDate(Calendar calendar) {
        return DateFormat.format("yyyy-MM-dd HH:mm:ss", calendar).toString();
    }
    static String formatDateString(String originalDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date = inputFormat.parse(originalDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
    //function to destroy or nullify all the static variables
    static void destroyAll() {
        MainActivity.user = null;
        MainActivity.wallet = null;
        MainActivity.expenseCategories = null;
        TransactionActivity.transactions = null;
    }
}
