package com.example.mobdevemobileapp;

import android.content.Intent;
import android.os.Bundle;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;

public class EditReviewActivity extends AppCompatActivity {

    ImageView ivCompanyLogo;
    TextView tvCompanyName;
    EditText etReviewHeadline, etReviewContent;
    SimpleRatingBar srbWorkEnvironment, srbMentorship, srbWorkload;

    Spinner spnInternshipType;
    Spinner spnAllowanceProvision;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_review);

        ivCompanyLogo = findViewById(R.id.ivCompanyLogo);
        tvCompanyName = findViewById(R.id.tvCompanyName);

        srbMentorship = findViewById(R.id.srbMentorship);
        srbWorkEnvironment = findViewById(R.id.srbWorkEnvironment);
        srbWorkload = findViewById(R.id.srbWorkload);

        etReviewHeadline = findViewById(R.id.etReviewHeadline);
        etReviewContent = findViewById(R.id.etReviewContent);
        spnInternshipType = findViewById(R.id.spnInternshipType);
        spnAllowanceProvision = findViewById(R.id.spnAllowanceProvision);

        populatePage();
    }

    public void populatePage() {
        Intent intent = getIntent();

        int companyLogo = intent.getIntExtra("companyLogo", 0);
        String companyName = intent.getStringExtra("companyName");

        float workEnvironment = intent.getFloatExtra("workEnvironment", 0);
        float mentorship = intent.getFloatExtra("mentorship", 0);
        float workload = intent.getFloatExtra("workload", 0);

        String reviewHeadline = intent.getStringExtra("reviewTitle");
        String reviewContent = intent.getStringExtra("reviewContent");
        String internshipType = intent.getStringExtra("internshipType");
        String allowanceProvision = intent.getStringExtra("allowanceProvision");

        setupSpinners(internshipType, allowanceProvision);

        ivCompanyLogo.setImageResource(companyLogo);
        tvCompanyName.setText(companyName);

        srbWorkEnvironment.setRating(workEnvironment);
        srbMentorship.setRating(mentorship);
        srbWorkload.setRating(workload);

        etReviewHeadline.setText(reviewHeadline);
        etReviewContent.setText(reviewContent);
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

        spnInternshipType.setSelection(types.indexOf(internshipType));
        spnAllowanceProvision.setSelection(allowances.indexOf(allowanceProvision));
    }

    public void saveChanges(View v) {
        String updatedHeadline = etReviewHeadline.getText().toString();
        String updatedContent = etReviewContent.getText().toString();
        float updatedWorkEnvironment = srbWorkEnvironment.getRating();
        float updatedMentorship = srbMentorship.getRating();
        float updatedWorkload = srbWorkload.getRating();
        InternshipType updatedInternshipType = stringToInternshipType(spnInternshipType.getSelectedItem().toString());
        AllowanceProvision updatedAllowanceProvision = stringToAllowanceProvision(spnAllowanceProvision.getSelectedItem().toString());
        String companyName = tvCompanyName.getText().toString();

        float updatedAverageRating = (updatedWorkEnvironment + updatedMentorship + updatedWorkload) / 3;

        // Retrieve user from intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        Review updatedReview = new Review(
                updatedAverageRating,
                updatedWorkEnvironment,
                updatedMentorship,
                updatedWorkload,
                updatedInternshipType,
                updatedAllowanceProvision,
                updatedHeadline,
                new User(username), // Use the username to create a User object
                intent.getStringExtra("reviewDate"), // Keep the original review date
                updatedContent
        );
        updatedReview.setCompanyName(companyName);

        // Update Firestore with the updated review
        FirestoreManager.getInstance().updateReview(updatedReview, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditReviewActivity.this, "Review updated successfully", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(EditReviewActivity.this, MainPageActivity.class);
                intent2.putExtra("companyName", companyName);
                startActivity(intent2);
                finish();
            } else {
                Toast.makeText(EditReviewActivity.this, "Failed to save changes. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void discardChanges(View v) {
        String companyName = tvCompanyName.getText().toString();
        Intent intent = new Intent(EditReviewActivity.this, MainPageActivity.class);
        intent.putExtra("companyName", companyName);
        startActivity(intent);
        finish();
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
}