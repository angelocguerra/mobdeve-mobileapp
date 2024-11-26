package com.example.mobdevemobileapp;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;

public class ReviewPageActivity extends AppCompatActivity {

    ImageView ivCompanyLogo;
    TextView tvCompanyIndustry, tvCompanyName, tvCompanyLocation;
    TextView tvReviewTitle, tvReviewAuthor, tvReviewDate, tvReviewContent;
    SimpleRatingBar srbWorkEnvironment, srbMentorship, srbWorkload;

    Spinner spnInternshipType, spnAllowanceProvision;
    
    MaterialButton btnHelpful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.populatePage();
        Log.d("finishPopulate", "Populate page done");
        
        btnHelpful = findViewById(R.id.btnHelpful);
    }

    public void populatePage() {
        ivCompanyLogo = findViewById(R.id.ivCompanyLogo);
        tvCompanyIndustry = findViewById(R.id.tvCompanyIndustry);
        tvCompanyName = findViewById(R.id.tvCompanyName);
        tvCompanyLocation = findViewById(R.id.tvCompanyLocation);

        srbWorkEnvironment = findViewById(R.id.srbWorkEnvironment);
        srbMentorship = findViewById(R.id.srbMentorship);
        srbWorkload = findViewById(R.id.srbWorkload);

        tvReviewTitle = findViewById(R.id.tvReviewTitle);
        tvReviewAuthor = findViewById(R.id.tvReviewAuthor);
        tvReviewDate = findViewById(R.id.tvReviewDate);
        tvReviewContent = findViewById(R.id.tvReviewContent);

        spnInternshipType = findViewById(R.id.spnInternshipType);
        spnAllowanceProvision = findViewById(R.id.spnAllowanceProvision);

        Intent intent = getIntent();

        String companyName = intent.getStringExtra("companyName");
        tvCompanyName.setText(companyName);
        Log.d("addCompanyName", "Company Name added");

        String companyLocation = intent.getStringExtra("companyLocation");
        tvCompanyLocation.setText(companyLocation);
        Log.d("addCompanyLocation", "Company Location added");

        int companyImage = intent.getIntExtra("companyImage", 0);
        ivCompanyLogo.setImageResource(companyImage);
        Log.d("addCompanyImage", "Company Image added");

        String companyIndustry = intent.getStringExtra("companyIndustry");
        tvCompanyIndustry.setText(companyIndustry);
        Log.d("addCompanyIndustry", "Company Industry added");

//        float reviewRating = intent.getFloatExtra("rating", 0);
//        tvReviewRating.setText(String.valueOf(reviewRating));
//        Log.d("addRating", "Rating added");

        float workEnvironment = intent.getFloatExtra("workEnvironment", 0);
        srbWorkEnvironment.setRating(workEnvironment);
        Log.d("addWorkEnvironment", "Work Environment added");

        float mentorship = intent.getFloatExtra("mentorship", 0);
        srbMentorship.setRating(mentorship);
        Log.d("addMentorship", "Mentorship added");

        float workload = intent.getFloatExtra("workload", 0);
        srbWorkload.setRating(workload);
        Log.d("addWorkload", "Workload added");

        String internshipType = intent.getStringExtra("internshipType");
        String allowanceProvision = intent.getStringExtra("allowanceProvision");

        setupSpinners(internshipType, allowanceProvision);

        String reviewTitle = intent.getStringExtra("reviewTitle");
        tvReviewTitle.setText(reviewTitle);
        Log.d("addTitle", "title added");

        String reviewAuthor = intent.getStringExtra("user");
        tvReviewAuthor.setText(reviewAuthor);
        Log.d("addAuthor", "Author added");

        String reviewDate = intent.getStringExtra("datePosted");
        tvReviewDate.setText(reviewDate);
        Log.d("addDate", "Date Posted added");

        String reviewContent = intent.getStringExtra("reviewText");
        tvReviewContent.setText(reviewContent);
        Log.d("addContent", "Content added");

//        srbReviewRating.setRating(reviewRating);
//        Log.d("addRatingBar", "Rating Bar added");
    }

    public void setupSpinners(String internshipType, String allowanceProvision) {

        ArrayList<String> types = new ArrayList<>();
        ArrayList<String> allowances = new ArrayList<>();

        for (InternshipType type : InternshipType.values()) {
            types.add(convertInternshipType(type));
        }
        for (AllowanceProvision allowance : AllowanceProvision.values()) {
            allowances.add(convertAllowanceProvision(allowance));
        }

        ArrayAdapter internshipTypeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, types);
        internshipTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter allowanceProvisionAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, allowances);
        allowanceProvisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnInternshipType.setAdapter(internshipTypeAdapter);
        spnAllowanceProvision.setAdapter(allowanceProvisionAdapter);

        int internshipTypePosition = internshipTypeAdapter.getPosition(stringToInternshipType(internshipType));
        spnInternshipType.setSelection(internshipTypePosition);

        int allowanceProvisionPosition = allowanceProvisionAdapter.getPosition(stringToAllowanceProvision(allowanceProvision));
        spnAllowanceProvision.setSelection(allowanceProvisionPosition);
    }

    public InternshipType stringToInternshipType(String string) {
        InternshipType output = null;
        if(string.equals("Full Face to Face"))
            output = InternshipType.F2F;
        if(string.equals("Full Online"))
            output = InternshipType.ONLINE;
        if(string.equals("Hybrid"))
            output = InternshipType.HYBRID;
        return output;
    }
    public String convertInternshipType(InternshipType internshipType) {
        String output = "";
        if(internshipType == InternshipType.F2F)
            output = "Full Face to Face";
        if(internshipType == InternshipType.ONLINE)
            output = "Full Online";
        if(internshipType == InternshipType.HYBRID)
            output = "Hybrid";
        return output;
    }
    public AllowanceProvision stringToAllowanceProvision(String string) {
        AllowanceProvision output = null;
        if(string.equals("Less than 10k"))
            output = AllowanceProvision.LESS_THAN_TEN;
        if(string.equals("10k - 20k"))
            output = AllowanceProvision.TEN_TWENTY;
        if(string.equals("20k - 30k"))
            output = AllowanceProvision.TWENTY_THIRTY;
        if(string.equals("30k - 40k"))
            output = AllowanceProvision.THIRTY_FORTY;
        if(string.equals("40k - 50k"))
            output = AllowanceProvision.FORTY_FIFTY;
        if(string.equals("50k - 60k"))
            output = AllowanceProvision.FIFTY_SIXTY;
        if(string.equals("60k - 70k"))
            output = AllowanceProvision.SIXTY_SEVENTY;
        if(string.equals("70k - 80k"))
            output = AllowanceProvision.SEVENTY_EIGHTY;
        if(string.equals("80k - 90k"))
            output = AllowanceProvision.EIGHTY_NINETY;
        if(string.equals("90k - 100k"))
            output = AllowanceProvision.NINETY_HUNDRED;
        if(string.equals("More than 100k"))
            output = AllowanceProvision.MORE_THAN_HUNDRED;
        return output;
    }
    public String convertAllowanceProvision(AllowanceProvision allowanceProvision) {
        String output = "";
        if(allowanceProvision == AllowanceProvision.LESS_THAN_TEN)
            output = "Less than 10k";
        if(allowanceProvision == AllowanceProvision.TEN_TWENTY)
            output = "10k - 20k";
        if(allowanceProvision == AllowanceProvision.TWENTY_THIRTY)
            output = "20k - 30k";
        if(allowanceProvision == AllowanceProvision.THIRTY_FORTY)
            output = "30k - 40k";
        if(allowanceProvision == AllowanceProvision.FORTY_FIFTY)
            output = "40k - 50k";
        if(allowanceProvision == AllowanceProvision.FIFTY_SIXTY)
            output = "50k - 60k";
        if(allowanceProvision == AllowanceProvision.SIXTY_SEVENTY)
            output = "60k - 70k";
        if(allowanceProvision == AllowanceProvision.SEVENTY_EIGHTY)
            output = "70k - 80k";
        if(allowanceProvision == AllowanceProvision.EIGHTY_NINETY)
            output = "80k - 90k";
        if(allowanceProvision == AllowanceProvision.NINETY_HUNDRED)
            output = "90k - 100k";
        if(allowanceProvision == AllowanceProvision.MORE_THAN_HUNDRED)
            output = "More than 100k";
        return output;
    }

    public void editReview(View v) {
        Intent intent = new Intent(this, EditReviewActivity.class);
        startActivity(intent);
    }
    
    public void toggleHelpful(View v) {
        if (btnHelpful.isChecked()) {
            Log.d("showChecked", "Button.isChecked = " + btnHelpful.isChecked());
            btnHelpful.setBackgroundColor(getResources().getColor(R.color.french_blue));
            btnHelpful.setTextColor(getResources().getColor(R.color.pale_peach));
            btnHelpful.setIcon(getResources().getDrawable(R.drawable.helpful_toggled));
            Log.d("showChecked", "Button.isChecked = " + btnHelpful.isChecked());
            //review.setHelpful(review.getHelpful() + 1);
        }
        else {
            Log.d("showChecked", "Button.isChecked = " + btnHelpful.isChecked());
            btnHelpful.setBackgroundColor(getResources().getColor(R.color.light_grey));
            btnHelpful.setTextColor(getResources().getColor(R.color.dark_jungle_green));
            btnHelpful.setIcon(getResources().getDrawable(R.drawable.helpful));
            Log.d("showChecked","Button.isChecked = " + btnHelpful.isChecked());
            //review.setHelpful(review.getHelpful() - 1);
        }
    }
}