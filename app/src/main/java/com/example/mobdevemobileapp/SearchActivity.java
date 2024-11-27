package com.example.mobdevemobileapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private FirestoreManager db;
    private RecyclerView rvSearchResults;
    private CompanyAdapter companyAdapter;
    private ArrayList<Company> companiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        // Setup Navbar
        Navbar.setupNavbar(this);

        // Initialize FirestoreManager and RecyclerView
        db = FirestoreManager.getInstance();
        rvSearchResults = findViewById(R.id.rvSearchResults);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        companiesList = new ArrayList<>();

        // Setup Search Button Logic
        AutoCompleteTextView etSearchField = findViewById(R.id.etSearchField);
        Button btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(view -> {
            String searchQuery = etSearchField.getText().toString().trim();

            if (searchQuery.isEmpty()) {
                Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
                return;
            }

            // Perform Firestore Query
            performSearch(searchQuery);
        });
    }

    private void performSearch(String search) {
        String searchQuery = search.trim().toLowerCase(); // Normalize the search query
        Log.d("SearchActivity", "Search Query: " + searchQuery);



        db.getCompanies(searchQuery, task -> {
            Log.d("SearchActivity", "Inside Firestore query callback");
            if (task.isSuccessful()) {
                Log.d("SearchActivity", "Query successful");
                companiesList.clear(); // Clear the list before adding new results
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String companyName = document.getString("companyName");
                    Log.d("SearchActivity", "Comparing: Search Query = " + searchQuery + ", Company Name = " + companyName);

                    if (companyName != null && companyName.toLowerCase().contains(searchQuery)) {
                        try {
                            String industry = document.getString("companyIndustry");
                            Log.d("SearchActivity", "1");
                            String location = document.getString("companyLocation");
                            Log.d("SearchActivity", "2");
                            float rating = Float.parseFloat(String.valueOf(document.getDouble("rating")));
                            Log.d("SearchActivity", "3");
                            int image;
                            if (document.contains("companyImage") && document.get("companyImage") != null) {
                                image = document.getLong("companyImage").intValue(); // Assuming it's stored as a long in Firestore
                            } else {
                                image = R.drawable.default_company_image; // Replace with your default drawable resource ID
                            }
                            Log.d("SearchActivity", "4");
                            Company company = new Company(industry, companyName, image, location, rating);
                            Log.d("SearchActivity", "5");
                            companiesList.add(company);
                            Log.d("SearchActivity", "6");
                        } catch (Exception e) {
                            Log.e("SearchActivity", "Error parsing company data: " + e.getMessage(), e);
                        }
                    }
                }

                if (companiesList.isEmpty()) {
                    Toast.makeText(this, "No companies found", Toast.LENGTH_SHORT).show();
                } else {
                    updateRecyclerView();
                }
            } else {
                Log.e("SearchActivity", "Error fetching companies: " + task.getException());
                Toast.makeText(this, "Error fetching companies", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRecyclerView() {
        if (companyAdapter == null) {
            companyAdapter = new CompanyAdapter(companiesList.toArray(new Company[0]), this);
            rvSearchResults.setAdapter(companyAdapter);
        } else {
            companyAdapter.companies = companiesList.toArray(new Company[0]);
            companyAdapter.notifyDataSetChanged();
        }
    }
}
