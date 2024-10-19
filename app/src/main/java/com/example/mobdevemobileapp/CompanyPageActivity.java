package com.example.mobdevemobileapp;

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