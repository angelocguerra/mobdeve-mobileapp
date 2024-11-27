package com.example.mobdevemobileapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Company[] companies = new Company[]{
                new Company("Restaurants & Food Service","Los Pollos Hermanos", R.drawable.los_pollos_hermanos, "Albuquerque", 4.5f),
                new Company("Retail & Wholesale", "Dunder Mifflin Inc.", R.drawable.dunder_mifflin, "Scranton", 3.0f),
                new Company("Security & Protective", "Ghostbusters Inc.", R.drawable.ghostbusters, "New York City", 4.0f),
                new Company("Restaurants & Food Service", "Central Perk", R.drawable.central_perk, "New York City", 4.5f),
                new Company("Restaurants & Food Service","Los Pollos Hermanos", R.drawable.los_pollos_hermanos, "Albuquerque", 4.5f),
                new Company("Retail & Wholesale", "Dunder Mifflin Inc.", R.drawable.dunder_mifflin, "Scranton", 3.0f),
                new Company("Security & Protective", "Ghostbusters Inc.", R.drawable.ghostbusters, "New York City", 4.0f),
                new Company("Restaurants & Food Service", "Central Perk", R.drawable.central_perk, "New York City", 4.5f)
        };

        CompanyAdapter companyAdapter = new CompanyAdapter(companies, MainPageActivity.this);
        recyclerView.setAdapter(companyAdapter);

        Navbar.setupNavbar(this);

    }

}