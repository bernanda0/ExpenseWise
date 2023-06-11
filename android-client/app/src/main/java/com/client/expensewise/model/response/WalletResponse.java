package com.client.expensewise.model.response;

import com.client.expensewise.model.Wallet;

public class WalletResponse extends BaseResponse {
    private Wallet wallet;

    public Wallet getWallet() {
        return wallet;
    }

    @Override
    public String toString() {
        return "RequestWallet{" +
                "success=" + super.isSuccess() +
                ", message='" + super.getMessage() + '\'' +
                ", wallet=" + wallet +
                '}';
    }
}
