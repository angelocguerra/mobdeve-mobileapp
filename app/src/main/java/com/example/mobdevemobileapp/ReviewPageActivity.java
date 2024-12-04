package com.example.mobdevemobileapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    TextView tvInternshipType, tvAllowanceProvision;

    MaterialButton btnHelpful;

    Company currentCompany;
    String currentlyLoggedInUser;

    Button btnEdit, btnDelete;
    private Button btnEdit1;


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
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        currentlyLoggedInUser = sharedPreferences.getString("username", "");


        btnHelpful = findViewById(R.id.btnHelpful);

        //check if current review selected is equal to the user currently logged in
        Log.d("checkUser", "Currently logged in user: " + currentlyLoggedInUser);
        Log.d("checkUser", "Review author: " + tvReviewAuthor.getText().toString());
        if (!(currentlyLoggedInUser.equals(tvReviewAuthor.getText().toString()))) {
            Log.d("checkUser", "User is the author of the review");
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);

        }

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

        tvInternshipType = findViewById(R.id.tvInternshipType);
        tvAllowanceProvision = findViewById(R.id.tvAllowanceProvision);
        btnDelete = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEdit);

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

        float reviewRating = intent.getFloatExtra("rating", 0);
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
        tvInternshipType.setText(internshipType);
        String allowanceProvision = intent.getStringExtra("allowanceProvision");
        tvAllowanceProvision.setText(allowanceProvision);

        String reviewTitle = intent.getStringExtra("reviewTitle");
        tvReviewTitle.setText(reviewTitle);
        Log.d("addTitle", "title added");

        String reviewAuthor = intent.getStringExtra("username");
        tvReviewAuthor.setText(reviewAuthor);
        Log.d("addAuthor", "Author added" + reviewAuthor);

        String reviewDate = intent.getStringExtra("datePosted");
        tvReviewDate.setText(reviewDate);
        Log.d("addDate", "Date Posted added");

        String reviewContent = intent.getStringExtra("reviewText");
        tvReviewContent.setText(reviewContent);
        Log.d("addContent", "Content added");

        currentCompany = new Company(companyIndustry, companyName, companyImage, companyLocation, reviewRating);

    }

    public void editReview(View v) {
        Intent intent = new Intent(this, EditReviewActivity.class);
        intent.putExtra("companyName", currentCompany.getCompanyName());
        intent.putExtra("companyLogo", currentCompany.getCompanyImage());

        intent.putExtra("workEnvironment", srbWorkEnvironment.getRating());
        intent.putExtra("mentorship", srbMentorship.getRating());
        intent.putExtra("workload", srbWorkload.getRating());

        intent.putExtra("internshipType", tvInternshipType.getText().toString());
        intent.putExtra("allowanceProvision", tvAllowanceProvision.getText().toString());
        intent.putExtra("reviewTitle", tvReviewTitle.getText().toString());
        intent.putExtra("reviewContent", tvReviewContent.getText().toString());

        startActivity(intent);
    }

    public void deleteReview(View v) {
        // TODO: delete review
    }

    public void toggleHelpful(View v) {
        if (btnHelpful.isChecked()) {
            Log.d("showChecked", "Button.isChecked = " + btnHelpful.isChecked());
            btnHelpful.setBackgroundColor(getResources().getColor(R.color.french_blue));
            btnHelpful.setTextColor(getResources().getColor(R.color.pale_peach));
            btnHelpful.setIcon(getResources().getDrawable(R.drawable.helpful_toggled));
            Log.d("showChecked", "Button.isChecked = " + btnHelpful.isChecked());
            //review.setHelpful(review.getHelpful() + 1);
        } else {
            Log.d("showChecked", "Button.isChecked = " + btnHelpful.isChecked());
            btnHelpful.setBackgroundColor(getResources().getColor(R.color.light_grey));
            btnHelpful.setTextColor(getResources().getColor(R.color.dark_jungle_green));
            btnHelpful.setIcon(getResources().getDrawable(R.drawable.helpful));
            Log.d("showChecked", "Button.isChecked = " + btnHelpful.isChecked());
            //review.setHelpful(review.getHelpful() - 1);
        }
    }
}