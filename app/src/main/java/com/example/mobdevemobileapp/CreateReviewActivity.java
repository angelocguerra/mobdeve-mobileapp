package com.example.mobdevemobileapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CreateReviewActivity extends AppCompatActivity {
    private FirestoreManager db;
    private String currentUsername;
    private SharedPreferences sharedPreferences;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_review);

        db = FirestoreManager.getInstance();
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        currentUsername = sharedPreferences.getString("username", "");
        user = new User(currentUsername);

        setupSpinners();

        // Set up the navbar
        Navbar.setupNavbar(this);
    }

    public void postReview(View v) {

        Review review = null;
        db.addReviewToUser(currentUsername, review, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Review added successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            } else {
                Toast.makeText(this, "Failed to add review", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void discardReview(View v){
        // do smth
    }

    public void setupSpinners() {
        Spinner spnInternshipType = findViewById(R.id.spnInternshipType);
        Spinner spnAllowanceProvision = findViewById(R.id.spnAllowanceProvision);

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
