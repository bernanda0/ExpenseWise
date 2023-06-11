package com.client.expensewise.model.response;

import com.client.expensewise.model.Expense;

public class ExpenseResponse extends BaseResponse {
    private Expense expense;

    public Expense getExpense() {
        return expense;
    }
}
