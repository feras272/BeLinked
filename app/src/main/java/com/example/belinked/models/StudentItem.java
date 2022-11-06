package com.example.belinked.models;

public class StudentItem {
    public String volunteerAddress;
    public String signedUp;
    public String studentUsername;
    public String studentEmail;
    public String volunteerDescription;

    public StudentItem(String volunteerAddress, String signedUp, String studentUsername, String studentEmail) {
        this.volunteerAddress = volunteerAddress;
        this.signedUp = signedUp;
        this.studentUsername = studentUsername;
        this.studentEmail = studentEmail;
    }

    public StudentItem(String volunteerAddress, String signedUp, String studentUsername, String studentEmail, String volunteerDescription) {
        this.volunteerAddress = volunteerAddress;
        this.signedUp = signedUp;
        this.studentUsername = studentUsername;
        this.studentEmail = studentEmail;
        this.volunteerDescription = volunteerDescription;
    }

    public String getVolunteerAddress() {
        return volunteerAddress;
    }

    public void setVolunteerAddress(String volunteerAddress) {
        this.volunteerAddress = volunteerAddress;
    }

    public String getSignedUp() {
        return signedUp;
    }

    public void setSignedUp(String signedUp) {
        this.signedUp = signedUp;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getVolunteerDescription() {
        return volunteerDescription;
    }

    public void setVolunteerDescription(String volunteerDescription) {
        this.volunteerDescription = volunteerDescription;
    }
}
