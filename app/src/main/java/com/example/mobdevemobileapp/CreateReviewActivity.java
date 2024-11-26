package com.example.mobdevemobileapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class CreateReviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_review);

        // Set up the navbar
        Navbar.setupNavbar(this);
    }
}
