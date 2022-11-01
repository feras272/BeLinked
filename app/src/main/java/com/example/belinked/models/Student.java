package com.example.belinked.models;

public class Student {

    public String accountType = "students";
    public String firstName;
    public String lastName;
    public String email;
    public int profileImage;

    public Student(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Student(String firstName, String lastName, String email, int profileImage) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profileImage = profileImage;
    }
}
