package com.example.belinked.models;

public class User {
    public String accountType;
    public String firstName;
    public String lastName;
    public String email;
    public int image;
    //public String password;


    public User() {}

    public User(String firstName, String lastName, String email, String accountType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accountType = accountType;
        //this.password = password;
    }

    public User(String accountType, String firstName, String lastName, String email, int image) {
        this.accountType = accountType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.image = image;
    }
}
