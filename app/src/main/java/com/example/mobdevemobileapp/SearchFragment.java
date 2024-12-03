package com.example.mobdevemobileapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private FirestoreManager db;
    private RecyclerView rvSearchResults;
    private CompanyAdapter companyAdapter;
    private ArrayList<Company> companiesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize FirestoreManager and RecyclerView
        db = FirestoreManager.getInstance();
        rvSearchResults = view.findViewById(R.id.rvSearchResults);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        companiesList = new ArrayList<>();

        // Setup Search Button Logic
        AutoCompleteTextView etSearchField = view.findViewById(R.id.etSearchField);
        Button btnSearch = view.findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(v -> {
            String searchQuery = etSearchField.getText().toString().trim();

            if (searchQuery.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a search term", Toast.LENGTH_SHORT).show();
                return;
            }

            // Perform Firestore Query
            performSearch(searchQuery);
        });

        // Inflate the layout for this fragment
        return view;
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
                    Toast.makeText(getContext(), "No companies found", Toast.LENGTH_SHORT).show();
                } else {
                    updateRecyclerView();
                }
            } else {
                Log.e("SearchActivity", "Error fetching companies: " + task.getException());
                Toast.makeText(getContext(), "Error fetching companies", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateRecyclerView() {
        if (companyAdapter == null) {
            companyAdapter = new CompanyAdapter(companiesList.toArray(new Company[0]), getContext());
            rvSearchResults.setAdapter(companyAdapter);
        } else {
            companyAdapter.companies = companiesList.toArray(new Company[0]);
            companyAdapter.notifyDataSetChanged();
        }
    }
}