package com.example.mobdevemobileapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
                        String join_date = document.getString("date_created");
                        String formatted_join_dateString = "Joined on " + join_date;

                        TextView tvUsername = findViewById(R.id.tvUsername);
                        TextView tvJoinDate = findViewById(R.id.tvJoinDate);

                        tvUsername.setText(username);
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

        fetchReviews(username, reviews -> {
            ProfileReviewsAdapter profileReviewsAdapter = new ProfileReviewsAdapter(reviews, this);
            recyclerView.setAdapter(profileReviewsAdapter);
        });
    }

    public void fetchReviews(String username, OnReviewsFetchedListener listener) {
        db.fetchReviews(username, task -> {
            if(task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                ArrayList<Review> reviews = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult()) {
                    try {
                        if (document.exists()) {
                            String companyName = document.getString("companyName");
                            String reviewTitle = document.getString("reviewTitle");
                            String reviewText = document.getString("reviewText");
                            String datePosted = document.getString("datePosted");

                            //Display all to log
                            Log.d("fetchReviews", "Company Name: " + companyName);
                            Log.d("fetchReviews", "Review Title: " + reviewTitle);
                            Log.d("fetchReviews", "Review Text: " + reviewText);
                            Log.d("fetchReviews", "Date Posted: " + datePosted);

                            String uuid = document.getString("uuid");

                            float workEnvironment = Float.parseFloat(String.valueOf(document.getDouble("workEnvironment")));
                            float mentorship = Float.parseFloat(String.valueOf(document.getDouble("mentorship")));
                            float workload = Float.parseFloat(String.valueOf(document.getDouble("workload")));

                            float ratingScore = (workEnvironment + mentorship + workload) / 3;

                            InternshipType internshipType = InternshipType.valueOf(document.getString("internshipType"));
                            AllowanceProvision allowanceProvision = AllowanceProvision.valueOf(document.getString("allowanceProvision"));

                            Review review = new Review(ratingScore, workEnvironment, mentorship, workload,
                                    companyName, internshipType, allowanceProvision, uuid, reviewTitle, new User(username), datePosted, reviewText);

                            reviews.add(review);
                        }
                    } catch (Exception e) {
                        Log.e("emptyReviewsList", "Reviews are empty", e);
                    }
                }
                listener.onReviewsFetched(reviews);
            } else {
                listener.onReviewsFetched(new ArrayList<>());
            }
        });
    }

    public interface OnReviewsFetchedListener {
        void onReviewsFetched(ArrayList<Review> reviews);
    }
}
