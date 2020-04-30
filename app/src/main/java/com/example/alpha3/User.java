package com.example.alpha3;

public class User {
    public String uID;
    public String name;
    public String phone;
    public String email;
    public boolean authorized;

    public User (){}

    public User(String uID, String name, String phone, String email, boolean authorized) {
        this.uID = uID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.authorized = authorized;
    }

    public String getuID() {
        return uID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Boolean getAuthorized() {
        return authorized;
    }

}