package com.example.mobdevemobileapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    FirestoreManager db = FirestoreManager.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<Company> companiesArrayList = new ArrayList<>();
        db.getCompanies(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    Log.d("HomeFragment", "No companies found");
                    // Handle empty result case if needed
                } else {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String companyName = document.getString("companyName");
                        String companyIndustry = document.getString("companyIndustry");
                        String companyLocation = document.getString("companyLocation");
                        float rating = Float.parseFloat(String.valueOf(document.getDouble("rating")));
                        int image;
                        if (document.contains("image") && document.get("image") != null) {
                            String image_name = document.getString("image"); // Assuming it's stored as a long in Firestore
                            image = getResources().getIdentifier(image_name, "drawable", getActivity().getPackageName());

                        } else {
                            image = R.drawable.icon; // Replace with your default drawable resource ID
                        }
                        Log.d("HomeFragment", "Company Name: " + companyName);
                        Company newCompany = new Company(companyIndustry, companyName, image, companyLocation, rating);
                        companiesArrayList.add(newCompany);
                    }
                    CompanyAdapter companyAdapter = new CompanyAdapter(companiesArrayList.toArray(new Company[0]), getContext());
                    recyclerView.setAdapter(companyAdapter);
                }
            } else {
                // Log error
                Log.e("HomeFragment", "Error getting documents: ", task.getException());
            }
        });

        return view;
    }
}