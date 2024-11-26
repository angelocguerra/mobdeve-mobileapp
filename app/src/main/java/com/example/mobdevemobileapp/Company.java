package com.example.mobdevemobileapp;

import java.util.ArrayList;

public class Company {

    private String companyIndustry;
    private String companyName;
    private String companyLocation;
    private float companyRating;
    private Integer companyImage;
    private ArrayList<Review> companyReviews;

    public Company(String companyIndustry, String companyName, Integer companyImage, String companyLocation, float rating) {
        this.companyIndustry = companyIndustry;
        this.companyName = companyName;
        this.companyLocation = companyLocation;
        this.companyRating = rating;
        this.companyImage = companyImage;
        this.companyReviews = new ArrayList<>();
    }

    // Getters
    public String getCompanyIndustry() {return companyIndustry;}
    public String getCompanyName() {return companyName;}
    public String getCompanyLocation() {return companyLocation;}
    public float getCompanyRating() {return companyRating;}
    public Integer getCompanyImage() {return companyImage;}
    public ArrayList<Review> getCompanyReviews() {return companyReviews;}

    // Setters
    public void setCompanyIndustry(String companyIndustry) {this.companyIndustry = companyIndustry;}
    public void setCompanyName(String companyName) {this.companyName = companyName;}

    public void setCompanyLocation(String companyLocation) {this.companyLocation = companyLocation;}
    public void setCompanyRating(float companyRating) {this.companyRating = companyRating;}
    public void setCompanyImage(Integer companyImage) {this.companyImage = companyImage;}
    public void setCompanyReviews(ArrayList<Review> companyReviews) {this.companyReviews = companyReviews;}
}
