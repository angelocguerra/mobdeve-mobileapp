package com.example.mobdevemobileapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private FirestoreManager db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        db = FirestoreManager.getInstance();

        // Retrieve username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        if (!username.isEmpty()) {
            // Fetch user profile data from Firestore
            db.getUserProfile(username, task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Set profile data in views
                        String fullName = document.getString("fullName");
                        String join_date = document.getString("date_created");
                        String formatted_join_dateString = "Joined on " + join_date;

                        TextView tvUsername = findViewById(R.id.tvUsername);
                        TextView tvJoinDate = findViewById(R.id.tvJoinDate);

                        tvUsername.setText(fullName);
                        tvJoinDate.setText(formatted_join_dateString);
                    } else {
                        Toast.makeText(this, "Profile data not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Failed to retrieve profile data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
        }
        // Set up the navbar
        Navbar.setupNavbar(this);

        // Set up the navbar
        Navbar.setupNavbar(this);
        // Reference to the Profile Settings button
        Button btnProfileSettings = findViewById(R.id.btnProfileSettings);


        // Set OnClickListener for navigation
        btnProfileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfile.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.rvUserReviews);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Review> reviews = fetchReviews(username);

        ArrayList<Company> companies = fetchCompanies(reviews);

        ProfileReviewsAdapter profileReviewsAdapter = new ProfileReviewsAdapter(reviews, companies, this);
        recyclerView.setAdapter(profileReviewsAdapter);
    }

    public ArrayList<Review> fetchReviews(String username) {
        ArrayList<Review> reviews = new ArrayList<>();
        db.fetchReviews(username, task -> {
            if(task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    Review review = document.toObject(Review.class);
                    reviews.add(review);
                }
            }
        });
        return reviews;
    }

    public ArrayList<Company> fetchCompanies(ArrayList<Review> reviews) {
        ArrayList<Company> companies = new ArrayList<>();
        for (Review review: reviews) {
            db.getCompanyDetailsGivenReview(review, task -> {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Company company = document.toObject(Company.class);
                    companies.add(company);
                }
            });
        }
        return companies;
    }

}
