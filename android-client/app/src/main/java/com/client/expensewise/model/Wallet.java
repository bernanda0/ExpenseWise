package com.client.expensewise.model;

public class Wallet {
    private String wid;
    private int total_income;
    private int total_expense;
    private int balance;
    private String[] connectedWallet;

    public String getWid() {
        return wid;
    }

    public int getBalance() {
        return balance;
    }

    public int getTotal_income() {
        return total_income;
    }

    public int getTotal_expense() {
        return total_expense;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "wid=" + wid +
                ", total_income=" + total_income +
                ", total_expense=" + total_expense +
                ", balance=" + balance +
                ", connectedWallet=" + connectedWallet +
                '}';
    }
}
