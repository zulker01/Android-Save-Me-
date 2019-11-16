package com.example.saveme;

import android.util.Pair;

import java.util.ArrayList;

public class User {
    public String name, email, phone,userId;
    public ArrayList<Pair<String,String>> contacts;
    public ArrayList<Pair<String,String>> location;

    public void setLocation(ArrayList<Pair<String, String>> location) {
        this.location = location;
    }

    public void setContacts(ArrayList<Pair<String, String>> contacts) {
        this.contacts = contacts;
    }

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
