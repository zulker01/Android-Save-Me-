package com.example.saveme;

public class Contacts {

    public String id,name,phone;
    public Contacts(String id,String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
