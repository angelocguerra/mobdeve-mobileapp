package com.example.mobdevemobileapp;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RatingBar;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;

public class ReviewPageActivity extends AppCompatActivity {

    TextView tvReviewRating, tvReviewTitle, tvReviewAuthor, tvReviewDate, tvReviewContent;
    SimpleRatingBar srbReviewRating;

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
    }

    public void populatePage() {
        tvReviewRating = findViewById(R.id.tvReviewRating);
        tvReviewTitle = findViewById(R.id.tvReviewTitle);
        tvReviewAuthor = findViewById(R.id.tvReviewAuthor);
        tvReviewDate = findViewById(R.id.tvReviewDate);
        tvReviewContent = findViewById(R.id.tvReviewContent);
        srbReviewRating = findViewById(R.id.srbReviewRating);

        Intent intent = getIntent();

        float reviewRating = intent.getFloatExtra("rating", 0);
        tvReviewRating.setText(String.valueOf(reviewRating));
        Log.d("addRating", "Rating added");

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

        srbReviewRating.setRating(reviewRating);
        Log.d("addRatingBar", "Rating Bar added");
    }
}