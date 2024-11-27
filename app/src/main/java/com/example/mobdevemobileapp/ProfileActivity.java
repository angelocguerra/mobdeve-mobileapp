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

        ArrayList<Review> reviews = new ArrayList<Review>() {
            {
                add(new Review(3.0f, 1.0f, 2.0f, 3.0f, InternshipType.F2F, AllowanceProvision.THIRTY_FORTY, "Review Title 1", new User("User 1"), "Date 1", "asdf"));
                add(new Review(4.0f, 1.0f, 2.0f, 3.0f, InternshipType.ONLINE, AllowanceProvision.TEN_TWENTY, "Review Title 2", new User("User 2"), "Date 2", "fdsa"));
                add(new Review(5.0f, 1.0f, 2.0f, 3.0f, InternshipType.HYBRID, AllowanceProvision.TWENTY_THIRTY, "Review Title 3", new User("User 3"), "Date 3", "1234"));
                add(new Review(2.0f, 1.0f, 2.0f, 3.0f, InternshipType.F2F, AllowanceProvision.LESS_THAN_TEN, "Review Title 4", new User("User 4"), "Date 4", "4321"));
            }
        };

        Company[] companies = new Company[]{
                new Company("Restaurants & Food Service","Los Pollos Hermanos", R.drawable.los_pollos_hermanos, "Albuquerque", 4.5f),
                new Company("Retail & Wholesale", "Dunder Mifflin Inc.", R.drawable.dunder_mifflin, "Scranton", 3.0f),
                new Company("Security & Protective", "Ghostbusters Inc.", R.drawable.ghostbusters, "New York City", 4.0f),
                new Company("Restaurants & Food Service", "Central Perk", R.drawable.central_perk, "New York City", 4.5f),
        };

        ProfileReviewsAdapter profileReviewsAdapter = new ProfileReviewsAdapter(reviews, companies, this);
        recyclerView.setAdapter(profileReviewsAdapter);
    }
}
