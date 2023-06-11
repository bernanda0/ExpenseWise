package com.client.expensewise.model.response;

import com.client.expensewise.model.User;

public class UserResponse extends BaseResponse {
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
