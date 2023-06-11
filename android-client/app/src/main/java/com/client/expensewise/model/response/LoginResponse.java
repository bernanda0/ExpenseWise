package com.client.expensewise.model.response;

public class LoginResponse extends BaseResponse {
    private String uid;

    public String getUid() {
        return uid;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "success=" + super.isSuccess() +
                ", message='" + super.getMessage() + '\'' +
                ", uid=" + uid +
                '}';
    }
}
