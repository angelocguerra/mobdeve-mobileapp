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

    private void performSearch(String searchQuery) {
        searchQuery = searchQuery.trim();

        if (searchQuery.isEmpty()) {
            Toast.makeText(this, "Please enter a company", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert search query to lowercase for normalization
        String normalizedQuery = searchQuery.toLowerCase();
        Log.d("SearchActivity", "Search Query: " + normalizedQuery);


        db.getCompanyNamesFilter(normalizedQuery, task -> {
            Log.d("SearchActivity", "Inside");

            if (task.isSuccessful()) {
                Log.d("SearchActivity", "Success");
                Log.d("SearchActivity", "Result:" + task.getResult());
                Log.d("SearchActivity", "Number of documents in result: " + task.getResult().size());

                companiesList.clear(); // Clear the list before adding new results

                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d("SearchActivity", "Loop");

                    try {
                        String industry = document.getString("companyIndustry");
                        String name = document.getString("companyName");
                        String location = document.getString("companyLocation");
                        float rating = Float.parseFloat(String.valueOf(document.getDouble("companyRating")));
                        int image = document.getLong("companyImage").intValue(); // Assuming image is stored as an int resource ID

                        Log.d("SearchActivity", "Company Name: " + name);

                        // Normalize company name for case-insensitive matching
                        if (name.toLowerCase().contains(normalizedQuery)) {
                            Company company = new Company(industry, name, image, location, rating);
                            companiesList.add(company);
                        }
                    } catch (Exception e) {
                        Log.e("SearchActivity", "Error parsing company data: " + e.getMessage(), e);
                    }
                }

                // Check if any results were found
                if (companiesList.isEmpty()) {
                    Toast.makeText(this, "No companies found", Toast.LENGTH_SHORT).show();
                } else {
                    // Update RecyclerView
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
