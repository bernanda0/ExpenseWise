package com.client.expensewise.model;

public class GetUserResponse extends BaseResponse {
    private User user;
    public User getUser() {
        return user;
    }
    @Override
    public String toString() {
        return "RequestUser{" +
                "success=" + super.isSuccess() +
                ", message='" + super.getMessage() + '\'' +
                ", user=" + user +
                '}';
    }
}
