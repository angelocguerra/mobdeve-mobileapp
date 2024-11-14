package com.example.mobdevemobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RatingBar;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;

public class CompanyPageActivity extends AppCompatActivity {

    TextView tvIndustry, tvCompanyTitle, tvCompanyLocation, tvCompanyRating, tvCompanyReviewCount;
    ImageView ivCompanyLogo;
    SimpleRatingBar srbCompanyRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_company_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.populatePage();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Review[] reviews = new Review[] {
            new Review(3.0f, "Review Title 1", new User("User 1"), "Date 1", "asdf"),
            new Review(4.0f, "Review Title 2", new User("User 2"), "Date 2", "fdsa"),
            new Review(5.0f, "Review Title 3", new User("User 3"), "Date 3", "1234"),
            new Review(2.0f, "Review Title 4", new User("User 4"), "Date 4", "4321")
        };

        ReviewAdapter reviewAdapter = new ReviewAdapter(reviews, this);
        recyclerView.setAdapter(reviewAdapter);
    }

    public void populatePage() {
        tvIndustry = findViewById(R.id.tvIndustry);
        tvCompanyTitle = findViewById(R.id.tvCompanyTitle);
        tvCompanyLocation = findViewById(R.id.tvCompanyLocation);
        tvCompanyRating = findViewById(R.id.tvCompanyRating);
        tvCompanyReviewCount = findViewById(R.id.tvCompanyReviewCount);
        ivCompanyLogo = findViewById(R.id.ivCompanyLogo);
        srbCompanyRating = findViewById(R.id.srbCompanyRating);

        Intent intent = getIntent();

        String industry = intent.getStringExtra("companyIndustry");
        tvIndustry.setText(industry);
        Log.d("addIndustry", "Industry added");

        String companyTitle = intent.getStringExtra("companyName");
        tvCompanyTitle.setText(companyTitle);
        Log.d("addTitle", "Title added");

        String companyLocation = intent.getStringExtra("companyLocation");
        tvCompanyLocation.setText(companyLocation);
        Log.d("addLocation", "Location added");

        int reviewsCount = intent.getIntExtra("reviewsCount", 0);
        tvCompanyReviewCount.setText(String.valueOf(reviewsCount));
        Log.d("addCount",  "Count added");

        float companyRating = intent.getFloatExtra("companyRating", 0);
        tvCompanyRating.setText(String.valueOf(companyRating));
        Log.d("addRating", "Numerical Rating added");

        int companyLogo = intent.getIntExtra("companyImage", 0);
        ivCompanyLogo.setImageResource(companyLogo);
        Log.d("addLogo", "Logo added");

        // Set Simple Rating Bar
        srbCompanyRating.setRating(companyRating);
        Log.d("addRatingBar", "Rating Bar added");

    }
}