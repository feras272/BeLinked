package com.example.belinked.models;

public class Organization {

    public String accountType = "organizations";
    public String firstName;
    public String lastName;
    public String email;
    public String organizationName;
    public int profileImage;

    public Organization(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Organization(String firstName, String lastName, String email, String organizationName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.organizationName = organizationName;
    }

    public Organization(String firstName, String lastName, String email, String organizationName , int profileImage) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.organizationName = organizationName;
        this.profileImage = profileImage;
    }

}
