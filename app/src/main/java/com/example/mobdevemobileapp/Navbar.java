package com.example.mobdevemobileapp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class Navbar {

    public static void setupNavbar(Activity activity) {
        // Set up listeners for each navigation button in the navbar
        activity.findViewById(R.id.nav_home).setOnClickListener(view -> {
            if (!(activity instanceof MainPageActivity)) {
                Intent intent = new Intent(activity, MainPageActivity.class);
                activity.startActivity(intent);
                activity.finish(); // Finish current activity to avoid stacking
            }
        });

        activity.findViewById(R.id.nav_search).setOnClickListener(view -> {
            if (!(activity instanceof SearchActivity)) {
                Intent intent = new Intent(activity, SearchActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        activity.findViewById(R.id.nav_add).setOnClickListener(view -> {
            if (!(activity instanceof CreateReviewActivity)) {
                Intent intent = new Intent(activity, CreateReviewActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        activity.findViewById(R.id.nav_profile).setOnClickListener(view -> {
            if (!(activity instanceof ProfileActivity)) {
                Intent intent = new Intent(activity, ProfileActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }
}
