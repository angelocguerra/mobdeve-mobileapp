package com.example.mobdevemobileapp;

import android.content.Intent;
import android.os.Bundle;
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

public class CompanyPageActivity extends AppCompatActivity {

    ImageView pageImageView;
    TextView pageNameView;
    TextView pageLocationView;
    RatingBar pageRatingView;

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
        pageImageView = findViewById(R.id.pageImageView);
        pageNameView = findViewById(R.id.pageNameView);
        pageLocationView = findViewById(R.id.pageLocationView);
        pageRatingView = findViewById(R.id.pageRatingView);

        Intent intent = getIntent();
        // Get image resource
        int imageResource = intent.getIntExtra("companyImage", 0);
        pageImageView.setImageResource(imageResource);

        String pageName = intent.getStringExtra("companyName");
        pageNameView.setText(pageName);

        String pageLocation = intent.getStringExtra("companyLocation");
        pageLocationView.setText(pageLocation);

        float pageRating = intent.getFloatExtra("rating", 0);
        pageRatingView.setRating(pageRating);

    }
}