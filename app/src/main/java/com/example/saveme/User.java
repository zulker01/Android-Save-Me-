package com.example.saveme;

public class User {
    public String name, email, phone,userId;
    public double lattitude, longitude;

    public User(){

    }

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
