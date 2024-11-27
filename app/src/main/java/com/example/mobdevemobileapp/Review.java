package com.example.mobdevemobileapp;

import java.util.EnumMap;

public class Review {
    float ratingScore;
    float workEnvironment;
    float mentorship;
    float workload;

    InternshipType internshipType;
    AllowanceProvision allowanceProvision;




    String companyName;

    String reviewTitle;
    User user;
    String datePosted;
    String reviewText;
    int helpful;

    public Review(float ratingScore, float workEnvironment, float mentorship, float workload,
                  InternshipType internshipType, AllowanceProvision allowanceProvision,
                  String reviewTitle, User user, String datePosted, String reviewText) {
        this.companyName = companyName;
        this.ratingScore = ratingScore;
        this.workEnvironment = workEnvironment;
        this.workload = workload;
        this.internshipType = internshipType;
        this.allowanceProvision = allowanceProvision;
        this.reviewTitle = reviewTitle;
        this.user = user;
        this.mentorship = mentorship;
        this.datePosted = datePosted;
        this.reviewText = reviewText;
        this.helpful = 0;
    }
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public float getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(float ratingScore) {
        this.ratingScore = ratingScore;
    }

    public float getWorkEnvironment() {
        return workEnvironment;
    }

    public void setWorkEnvironment(float workEnvironment) {
        this.workEnvironment = workEnvironment;
    }

    public float getMentorship() {
        return mentorship;
    }

    public void setMentorship(float mentorship) {
        this.mentorship = mentorship;
    }

    public float getWorkload() {
        return workload;
    }

    public void setWorkload(float workload) {
        this.workload = workload;
    }

    public void setInternshipType(InternshipType internshipType) {
        this.internshipType = internshipType;
    }

    public InternshipType getInternshipType() {
        return internshipType;
    }


    public String convertInternshipType(InternshipType internshipType) {
        String output = "";
        if (internshipType == InternshipType.F2F)
            output = "Full Face to Face";
        if (internshipType == InternshipType.ONLINE)
            output = "Full Online";
        if (internshipType == InternshipType.HYBRID)
            output = "Hybrid";
        return output;
    }

    public static InternshipType stringToInternshipType(String string) {
        InternshipType output = null;
        if (string.equals("Full Face to Face"))
            output = InternshipType.F2F;
        if (string.equals("Full Online"))
            output = InternshipType.ONLINE;
        if (string.equals("Hybrid"))
            output = InternshipType.HYBRID;
        return output;
    }

    public void setAllowanceProvision(AllowanceProvision allowanceProvision) {
        this.allowanceProvision = allowanceProvision;
    }

    public static AllowanceProvision stringToAllowanceProvision(String string) {
        AllowanceProvision output = null;
        if (string.equals("Less than 10k"))
            output = AllowanceProvision.LESS_THAN_TEN;
        if (string.equals("10k - 20k"))
            output = AllowanceProvision.TEN_TWENTY;
        if (string.equals("20k - 30k"))
            output = AllowanceProvision.TWENTY_THIRTY;
        if (string.equals("30k - 40k"))
            output = AllowanceProvision.THIRTY_FORTY;
        if (string.equals("40k - 50k"))
            output = AllowanceProvision.FORTY_FIFTY;
        if (string.equals("50k - 60k"))
            output = AllowanceProvision.FIFTY_SIXTY;
        if (string.equals("60k - 70k"))
            output = AllowanceProvision.SIXTY_SEVENTY;
        if (string.equals("70k - 80k"))
            output = AllowanceProvision.SEVENTY_EIGHTY;
        if (string.equals("80k - 90k"))
            output = AllowanceProvision.EIGHTY_NINETY;
        if (string.equals("90k - 100k"))
            output = AllowanceProvision.NINETY_HUNDRED;
        if (string.equals("More than 100k"))
            output = AllowanceProvision.MORE_THAN_HUNDRED;
        return output;
    }

    public AllowanceProvision getAllowanceProvision() {
        return allowanceProvision;
    }

    public String convertAllowanceProvision(AllowanceProvision allowanceProvision) {
        String output = "";
        if (allowanceProvision == AllowanceProvision.LESS_THAN_TEN)
            output = "Less than 10k";
        if (allowanceProvision == AllowanceProvision.TEN_TWENTY)
            output = "10k - 20k";
        if (allowanceProvision == AllowanceProvision.TWENTY_THIRTY)
            output = "20k - 30k";
        if (allowanceProvision == AllowanceProvision.THIRTY_FORTY)
            output = "30k - 40k";
        if (allowanceProvision == AllowanceProvision.FORTY_FIFTY)
            output = "40k - 50k";
        if (allowanceProvision == AllowanceProvision.FIFTY_SIXTY)
            output = "50k - 60k";
        if (allowanceProvision == AllowanceProvision.SIXTY_SEVENTY)
            output = "60k - 70k";
        if (allowanceProvision == AllowanceProvision.SEVENTY_EIGHTY)
            output = "70k - 80k";
        if (allowanceProvision == AllowanceProvision.EIGHTY_NINETY)
            output = "80k - 90k";
        if (allowanceProvision == AllowanceProvision.NINETY_HUNDRED)
            output = "90k - 100k";
        if (allowanceProvision == AllowanceProvision.MORE_THAN_HUNDRED)
            output = "More than 100k";
        return output;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getHelpful() {
        return helpful;
    }

    public void setHelpful(int helpful) {
        this.helpful = helpful;
    }
}
