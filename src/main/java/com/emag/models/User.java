package com.emag.models;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String dateOfBirth;
    private String pinCode;
    private String password;

    public User() {
    }

    public User(String firstName, String lastName, String email, String address, String dateOfBirth, String pinCode, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.pinCode = pinCode;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    @Override
    public String toString() {
        return "User: " + this.email + ", " + this.firstName + ", " + this.lastName + ", " + this.address + ", " +
                this.dateOfBirth + ", " + this.pinCode + ", " + this.password;
    }
}
