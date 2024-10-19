package com.example.mobdevemobileapp;

import java.util.ArrayList;

public class Company {

    private String companyName;
    private Integer companyImage;
    private String companyLocation;
    private float rating;
    private ArrayList<Review> reviews;

    public Company(String companyName, Integer companyImage, String companyLocation, float rating) {
        this.companyName = companyName;
        this.companyImage = companyImage;
        this.companyLocation = companyLocation;
        this.rating = rating;
        this.reviews = new ArrayList<Review>();
    }

    // getters and setters
    public String getCompanyName() {return companyName;}

    public void setCompanyName(String companyName) {this.companyName = companyName;}

    public Integer getCompanyImage() {return companyImage;}

    public void setCompanyImage(Integer companyImage) {this.companyImage = companyImage;}

    public String getCompanyLocation() {return companyLocation;}

    public void setCompanyLocation(String companyLocation) {this.companyLocation = companyLocation;}

    public float getRating() {return rating;}

    public void setRating(float rating) {this.rating = rating;}

    public ArrayList<Review> getReviews() {return reviews;}

    public void setReviews(ArrayList<Review> reviews) {this.reviews = reviews;}
}
