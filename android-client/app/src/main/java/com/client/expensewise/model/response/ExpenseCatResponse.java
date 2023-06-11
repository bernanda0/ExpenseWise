package com.client.expensewise.model.response;

import com.client.expensewise.model.ExpenseCategory;

import java.util.ArrayList;

public class ExpenseCatResponse extends BaseResponse {
    private ArrayList<ExpenseCategory> expense_category;

    public ArrayList<ExpenseCategory> getExpense_category() {
        return expense_category;
    }
}
