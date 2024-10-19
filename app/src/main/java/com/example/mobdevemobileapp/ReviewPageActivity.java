package com.example.mobdevemobileapp;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RatingBar;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReviewPageActivity extends AppCompatActivity {

    TextView reviewTitleView;
    TextView userView;
    TextView datePostedView;
    RatingBar reviewRatingView;
    TextView reviewTextView;

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
    }

    public void populatePage() {
        reviewTitleView = findViewById(R.id.reviewTitleView);
        userView = findViewById(R.id.userView);
        datePostedView = findViewById(R.id.datePostedView);
        reviewRatingView = findViewById(R.id.reviewRatingView);
        reviewTextView = findViewById(R.id.reviewTextView);

        Intent intent = getIntent();
        String reviewTitle = intent.getStringExtra("reviewTitle");
        reviewTitleView.setText(reviewTitle);
        String username = intent.getStringExtra("user");
        userView.setText(username);
        String datePosted = intent.getStringExtra("datePosted");
        datePostedView.setText(datePosted);
        float rating = intent.getFloatExtra("rating", 0);
        reviewRatingView.setRating(rating);
        String reviewText = intent.getStringExtra("reviewText");
        reviewTextView.setText(reviewText);
    }
}