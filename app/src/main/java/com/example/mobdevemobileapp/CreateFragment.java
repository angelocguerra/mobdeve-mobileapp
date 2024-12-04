package com.example.mobdevemobileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentSnapshot;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;
import java.util.Date;

public class CreateFragment extends Fragment {
    private FirestoreManager db;
    private String currentUsername;
    private SharedPreferences sharedPreferences;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        db = FirestoreManager.getInstance();
        sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", getActivity().MODE_PRIVATE);
        currentUsername = sharedPreferences.getString("username", "");
        user = new User(currentUsername);

        db.getUser(currentUsername, task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    user = db.getUserFromDocument(document);
                } else {
                    Log.d("FirestoreManager", "No such document");
                }
            } else {
                Log.d("FirestoreManager", "get failed with ", task.getException());
            }
        });

        setupSpinners(view);
        setupCompanySearch(view);

        return view;
    }

    private void setupCompanySearch(View view) {
        AutoCompleteTextView actvCompanyName = view.findViewById(R.id.actvCompanyName);

        actvCompanyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text changes
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    db.getCompanyNamesFilter(s.toString(), task -> {
                        if (task.isSuccessful()) {
                            ArrayList<String> companyNames = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                companyNames.add(document.getString("companyName"));
                                Log.d("FirestoreManager", document.getString("companyName"));
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, companyNames);
                            actvCompanyName.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("FirestoreManager", "Error getting companies: ", task.getException());
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed after text changes
            }
        });
    }

    public Review getReviewDetails(View view) {
        Review review = null;

        //GET company name
        AutoCompleteTextView actvCompanyName = view.findViewById(R.id.actvCompanyName);
        SimpleRatingBar ratingBar = view.findViewById(R.id.srbWorkEnvironment);
        SimpleRatingBar ratingBar2 = view.findViewById(R.id.srbMentorship);
        SimpleRatingBar ratingBar3 = view.findViewById(R.id.srbWorkload);
        Spinner spnInternshipType = view.findViewById(R.id.spnInternshipType);
        Spinner spnAllowanceProvision = view.findViewById(R.id.spnAllowanceProvision);
        EditText etHeadline = view.findViewById(R.id.etReviewHeadline);
        EditText etContent = view.findViewById(R.id.etReviewContent);
        //get the values from the views
        String companyName = actvCompanyName.getText().toString();
        float workEnvironmentRating = ratingBar.getRating();
        float mentorshipRating = ratingBar2.getRating();
        float workloadRating = ratingBar3.getRating();
        InternshipType internshipType = Review.stringToInternshipType(spnInternshipType.getSelectedItem().toString());
        AllowanceProvision allowanceProvision = Review.stringToAllowanceProvision(spnAllowanceProvision.getSelectedItem().toString());
        String datePosted = new Date().toString();

        float avgRating = (workEnvironmentRating + mentorshipRating + workloadRating) / 3;

        String headline = etHeadline.getText().toString();
        String content = etContent.getText().toString();

        //create a new review object
        review = new Review(avgRating, workEnvironmentRating, mentorshipRating, workloadRating, internshipType, allowanceProvision, headline, user, datePosted, content);
        review.setCompanyName(companyName);
        return review;
    }

    public void postReview(View view) {
        Review review = getReviewDetails(view);

        db.addReviewToUser(currentUsername, review, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getActivity(), "Review added successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ProfileFragment.class);
                startActivity(intent);
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), "Failed to add review", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void discardReview(View view) {
        // Show a confirmation dialog to the user
        new android.app.AlertDialog.Builder(getActivity())
                .setTitle("Discard Review")
                .setMessage("Are you sure you want to discard this review? Your changes will not be saved.")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Navigate back to the previous activity
                    Intent intent = new Intent(getActivity(), MainPageActivity.class);
                    startActivity(intent);
                    getActivity().finish(); // Close the current activity
                })
                .setNegativeButton("No", null) // Dismiss the dialog
                .show();
    }

    public void setupSpinners(View view) {
        Spinner spnInternshipType = view.findViewById(R.id.spnInternshipType);
        Spinner spnAllowanceProvision = view.findViewById(R.id.spnAllowanceProvision);

        ArrayList<String> types = new ArrayList<>();
        ArrayList<String> allowances = new ArrayList<>();

        for (InternshipType type : InternshipType.values()) {
            types.add(convertInternshipType(type));
        }
        for (AllowanceProvision allowance : AllowanceProvision.values()) {
            allowances.add(convertAllowanceProvision(allowance));
        }

        ArrayAdapter internshipTypeAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, types);
        internshipTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter allowanceProvisionAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, allowances);
        allowanceProvisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnInternshipType.setAdapter(internshipTypeAdapter);
        spnAllowanceProvision.setAdapter(allowanceProvisionAdapter);
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
}