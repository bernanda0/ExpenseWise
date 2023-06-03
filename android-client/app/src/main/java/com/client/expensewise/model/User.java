package com.client.expensewise.model;

public class User {
    private String username;
    private String email;
    private String img;
    private String phoneNumber;
    private String occupation;
    private int points;
    private String user_rank;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", img='" + img + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", occupation='" + occupation + '\'' +
                ", points=" + points +
                ", user_rank='" + user_rank + '\'' +
                '}';
    }
}
