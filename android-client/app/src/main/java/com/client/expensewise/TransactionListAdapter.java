package com.client.expensewise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.client.expensewise.model.Transaction;
import com.google.android.material.imageview.ShapeableImageView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

public class TransactionListAdapter extends ArrayAdapter<Transaction> {
    private ArrayList<Transaction> dataSet;
    Context mContext;
    boolean concise;

    private static class ViewHolder {
        ShapeableImageView icon_transaction;
        TextView description;
        TextView amount;
        TextView date;
        ImageView icon_category;
        TextView expense_level;
        TextView percentage;
    }


    public TransactionListAdapter(Context context, ArrayList<Transaction> data, boolean concise) {
        super(context, R.layout.list_transaction, data);
        this.dataSet = data;
        this.mContext = context;
        this.concise = concise;
    }

    private int lastPosition = -1;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Transaction t = getItem(position);
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_transaction, parent, false);
            viewHolder.icon_transaction = (ShapeableImageView) convertView.findViewById(R.id.icon_transaction);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.amount);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.icon_category = (ImageView) convertView.findViewById(R.id.icon_category);
            viewHolder.expense_level = (TextView) convertView.findViewById(R.id.expense_level);
            viewHolder.percentage = (TextView) convertView.findViewById(R.id.percentage);

            if (concise) {
                viewHolder.icon_transaction.setVisibility(View.GONE);
                viewHolder.icon_category.setVisibility(View.GONE);
            }

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        // this is for expense an tid that start with "e"
        if (t.getTid().startsWith("e")) {
            viewHolder.icon_transaction.setImageResource(R.drawable.ic_expense);
            // expense level
            switch (t.getT_expense_level()) {
                case "Green":
                    viewHolder.expense_level.setTextColor(mContext.getResources().getColor(R.color.green));
                    viewHolder.expense_level.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.green_secondary));
                    viewHolder.expense_level.setText("G");
                    break;
                case "Yellow":
                    viewHolder.expense_level.setTextColor(mContext.getResources().getColor(R.color.yellow));
                    viewHolder.expense_level.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.yellow_secondary));
                    viewHolder.expense_level.setText("Y");
                    break;
                case "Orange":
                    viewHolder.expense_level.setTextColor(mContext.getResources().getColor(R.color.orange));
                    viewHolder.expense_level.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.orange_secondary));
                    viewHolder.expense_level.setText("O");
                    break;
                case "Red":
                    viewHolder.expense_level.setTextColor(mContext.getResources().getColor(R.color.red));
                    viewHolder.expense_level.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.red_secondary));
                    viewHolder.expense_level.setText("R");
                    break;

            }
            // the pecentage is upto 2 decimal places
            viewHolder.percentage.setText(String.format("%.2f", t.getT_percentage()) + "%");
        } else {
            viewHolder.icon_transaction.setImageResource(R.drawable.ic_income);
            // hide the expense level and percentage
            viewHolder.expense_level.setVisibility(View.GONE);
            viewHolder.percentage.setVisibility(View.GONE);
        }

        viewHolder.description.setText(t.getT_description());
        viewHolder.amount.setText(ExpenseWiseToolClass.formatRupiah(t.getT_amount()));
        viewHolder.date.setText(ExpenseWiseToolClass.formatDateString(t.getT_date()));

        // this is for category
        switch (t.getT_category()) {
            case "Food":
                viewHolder.icon_category.setImageResource(R.drawable.food_cat);
                break;
            case "Transportation":
                viewHolder.icon_category.setImageResource(R.drawable.transportation_cat);
                break;
            case "Entertainment":
                viewHolder.icon_category.setImageResource(R.drawable.entertainment_cat);
                break;
            case "Shopping":
                viewHolder.icon_category.setImageResource(R.drawable.shopping_cat);
                break;
            case "Bills":
                viewHolder.icon_category.setImageResource(R.drawable.bills_cat);
                break;
            case "Salary":
                viewHolder.icon_category.setImageResource(R.drawable.salary_cat);
                break;
            case "Gift":
                viewHolder.icon_category.setImageResource(R.drawable.gift_cat);
                break;
            case "Investment":
                viewHolder.icon_category.setImageResource(R.drawable.investment_cat);
                break;
            case "Others":
                viewHolder.icon_category.setImageResource(R.drawable.others_cat);
                break;
            default:
                viewHolder.icon_category.setImageResource(R.drawable.others_cat);
                break;
        }

        return convertView;
    }
}