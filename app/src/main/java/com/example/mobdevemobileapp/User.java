package com.example.mobdevemobileapp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;
public class User {


    String username;
    String email;

    String password;
    String profileCreated;
    HashMap<String, Review> reviews;


    public User(String username, String email, String password, String profileCreated) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileCreated = profileCreated;
        this.reviews = new HashMap<>();
    }

    public User(String username) {
        this.username = username;
    }





    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileCreated() {
        return profileCreated;
    }

    public void setProfileCreated(String profileCreated) {
        this.profileCreated = profileCreated;
    }

    public HashMap<String, Review> getReviews() {
        return reviews;
    }

    public void setReviews(HashMap<String, Review> reviews) {
        this.reviews = reviews;
    }
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }


}
