package com.client.expensewise.model;

public class Wallet {
    private int wid;
    private int balance;
    private boolean connectedWallet;

    public int getWid() {
        return wid;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "wid=" + wid +
                ", balance=" + balance +
                ", connectedWallet=" + connectedWallet +
                '}';
    }
}
