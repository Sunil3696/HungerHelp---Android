package com.app.hungerhelp.models;

public class User {
    private String fullName;     // Changed from name to fullName
    private String username;
    private String email;
    private String phoneNumber;  // Changed from phone to phoneNumber
    private String address;
    private String password;
    private String profile;      // Added profile field for profile picture

    public User(String fullName, String username, String email, String phoneNumber, String address, String password, String profile) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
        this.profile = profile;
    }

    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getProfile() { return profile; }
    public void setProfile(String profile) { this.profile = profile; }
}
