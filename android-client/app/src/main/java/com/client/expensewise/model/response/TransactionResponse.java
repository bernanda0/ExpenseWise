package com.client.expensewise.model.response;

import com.client.expensewise.model.Transaction;

import java.util.ArrayList;

public class TransactionResponse extends BaseResponse {
    private ArrayList<Transaction> transactions;

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

}
