package com.client.expensewise.model.response;

import com.client.expensewise.model.IncomeCategory;

import java.util.ArrayList;

public class IncomeCatResponse extends BaseResponse {
    private ArrayList<IncomeCategory> income_category;

    public ArrayList<IncomeCategory> getIncome_category() {
        return income_category;
    }
}
