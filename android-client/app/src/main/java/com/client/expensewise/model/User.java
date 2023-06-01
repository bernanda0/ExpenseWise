package com.client.expensewise.model;

public class User {
    private int uid;
    private String username;
    private String email;
    private String img;
    private String password;
    private String phoneNumber;
    private String occupation;
    private int points;
    private int wid;

    public int getUid() {
        return uid;
    }
    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", img='" + img + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", occupation='" + occupation + '\'' +
                ", points=" + points +
                ", wid=" + wid +
                '}';
    }
}
