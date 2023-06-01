package com.client.expensewise.model;

public class LoginResponse {
    private boolean success;
    private String message;
    private User user;

    public User getUser() {
        return user;
    }
}
