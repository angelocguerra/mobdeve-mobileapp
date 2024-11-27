package com.example.mobdevemobileapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity {

    FirestoreManager db = FirestoreManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Company> companiesArrayList = new ArrayList<>();
        db.getCompanies(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    Log.d("MainPageActivity", "No companies found");
                    // Handle empty result case if needed
                } else {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String companyName = document.getString("companyName");
                        String companyIndustry = document.getString("companyIndustry");
                        String companyLocation = document.getString("companyLocation");
                        float rating = Float.parseFloat(String.valueOf(document.getDouble("rating")));
                        int image;
                        if (document.contains("companyImage") && document.get("companyImage") != null) {
                            image = document.getLong("companyImage").intValue(); // Assuming it's stored as a long in Firestore
                        } else {
                            image = R.drawable.default_company_image; // Replace with your default drawable resource ID
                        }
                        Log.d("MainPageActivity", "Company Name: " + companyName);
                        Company newCompany = new Company(companyIndustry, companyName, image, companyLocation, rating);
                        companiesArrayList.add(newCompany);
                    }
                    CompanyAdapter companyAdapter = new CompanyAdapter(companiesArrayList.toArray(new Company[0]), MainPageActivity.this);
                    recyclerView.setAdapter(companyAdapter);
                }
            } else {
                // Log error
            }
        });
//        Company[] companies = new Company[]{
//                new Company("Restaurants & Food Service","Los Pollos Hermanos", R.drawable.los_pollos_hermanos, "Albuquerque", 4.5f),
//                new Company("Retail & Wholesale", "Dunder Mifflin Inc.", R.drawable.dunder_mifflin, "Scranton", 3.0f),
//                new Company("Security & Protective", "Ghostbusters Inc.", R.drawable.ghostbusters, "New York City", 4.0f),
//                new Company("Restaurants & Food Service", "Central Perk", R.drawable.central_perk, "New York City", 4.5f),
//                new Company("Restaurants & Food Service","Los Pollos Hermanos", R.drawable.los_pollos_hermanos, "Albuquerque", 4.5f),
//                new Company("Retail & Wholesale", "Dunder Mifflin Inc.", R.drawable.dunder_mifflin, "Scranton", 3.0f),
//                new Company("Security & Protective", "Ghostbusters Inc.", R.drawable.ghostbusters, "New York City", 4.0f),
//                new Company("Restaurants & Food Service", "Central Perk", R.drawable.central_perk, "New York City", 4.5f)
//        };
//
//        CompanyAdapter companyAdapter = new CompanyAdapter(companies, MainPageActivity.this);
//        recyclerView.setAdapter(companyAdapter);

        Navbar.setupNavbar(this);

    }

}