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
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private FirestoreManager db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        db = FirestoreManager.getInstance();

        // Retrieve username from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        if (!username.isEmpty()) {
            // Fetch user profile data and reviews from Firestore
            Task<DocumentSnapshot> userProfileTask = db.getUserData(username);
            Task<QuerySnapshot> userReviewsTask = db.fetchReviewData(username);

            Tasks.whenAllSuccess(userProfileTask, userReviewsTask).addOnSuccessListener(results -> {
                DocumentSnapshot userProfile = (DocumentSnapshot) results.get(0);
                QuerySnapshot userReviews = (QuerySnapshot) results.get(1);

                if (userProfile.exists()) {
                    // Set profile data in views
                    String join_date = userProfile.getString("date_created");
                    String formatted_join_dateString = "Joined on " + join_date;

                    TextView tvUsername = view.findViewById(R.id.tvUsername);
                    TextView tvJoinDate = view.findViewById(R.id.tvJoinDate);

                    tvUsername.setText(username);
                    tvJoinDate.setText(formatted_join_dateString);
                } else {
                    Toast.makeText(getContext(), "Profile data not found", Toast.LENGTH_SHORT).show();
                }

                ArrayList<Review> reviews = new ArrayList<>();
                for (DocumentSnapshot document : userReviews) {
                    try {
                        if (document.exists()) {
                            String companyName = document.getString("companyName");
                            String reviewTitle = document.getString("reviewTitle");
                            String reviewText = document.getString("reviewText");
                            String datePosted = document.getString("datePosted");

                            // Display all to log
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

                RecyclerView recyclerView = view.findViewById(R.id.rvUserReviews);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                ProfileReviewsAdapter profileReviewsAdapter = new ProfileReviewsAdapter(reviews, this);
                recyclerView.setAdapter(profileReviewsAdapter);
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(getContext(), "No user logged in", Toast.LENGTH_SHORT).show();
        }

        // Reference to the Profile Settings button
        Button btnProfileSettings = view.findViewById(R.id.btnProfileSettings);

        // Set OnClickListener for navigation
        btnProfileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfile.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.rvUserReviews);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inflate the layout for this fragment
        return view;
    }
}