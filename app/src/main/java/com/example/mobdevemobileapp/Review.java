package com.example.mobdevemobileapp;

public class Review {
    float ratingScore;
    String reviewTitle;
    User user;
    String datePosted;
    String reviewText;
    int helpful;

    public Review(float ratingScore, String reviewTitle, User user, String datePosted, String reviewText) {
        this.ratingScore = ratingScore;
        this.reviewTitle = reviewTitle;
        this.user = user;
        this.datePosted = datePosted;
        this.reviewText = reviewText;
        this.helpful = 0;
    }

    public float getRatingScore() {return ratingScore;}

    public void setRatingScore(float ratingScore) {this.ratingScore = ratingScore;}

    public String getReviewTitle() {return reviewTitle;}

    public void setReviewTitle(String reviewTitle) {this.reviewTitle = reviewTitle;}

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}

    public String getDatePosted() {return datePosted;}

    public void setDatePosted(String datePosted) {this.datePosted = datePosted;}

    public String getReviewText() {return reviewText;}

    public void setReviewText(String reviewText) {this.reviewText = reviewText;}

    public int getHelpful() {return helpful;}

    public void setHelpful(int helpful) {this.helpful = helpful;}
}
