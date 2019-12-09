package com.firatt.model;

public class Users {
    public String username;
    public String password;
    public String email;
    public String phone;
    public String fcmId;

    public Users(String username, String password, String email, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }
    public Users(){}
}
