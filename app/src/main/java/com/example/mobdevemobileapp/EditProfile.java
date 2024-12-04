package com.example.mobdevemobileapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class EditProfile extends AppCompatActivity {

    private FirestoreManager db;
    private SharedPreferences sharedPreferences;
    private String currentUsername;
    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    SearchFragment searchFragment = new SearchFragment();
    CreateFragment createFragment = new CreateFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_page);

        // Navbar
        bottomNavigationView = findViewById(R.id.navbar);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.search) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, searchFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.create) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, createFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                    return true;
                }
                return false;
            }
        });

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize FirestoreManager and SharedPreferences
        db = FirestoreManager.getInstance();
        sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        currentUsername = sharedPreferences.getString("username", "");

        Button btnChangeUsername = findViewById(R.id.btnChangeUsername);
        Button btnChangePassword = findViewById(R.id.btnChangePassword);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnDeleteAccount = findViewById(R.id.btnDeleteAccount);

        // Set OnClickListener for navigation
        btnChangeUsername.setOnClickListener(view -> {
            Intent intent = new Intent(EditProfile.this, ChangeUsername.class);
            startActivity(intent);
        });

        btnChangePassword.setOnClickListener(view -> {
            Intent intent = new Intent(EditProfile.this, ChangePassword.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(view -> {
            logoutUser();
        });

        btnDeleteAccount.setOnClickListener(view -> {
            showDeleteConfirmationDialog();
        });
    }

    private void logoutUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.remove("username");
        editor.apply();

        Intent intent = new Intent(EditProfile.this, MainActivity.class);
        startActivity(intent);
        finish(); // Finish the current activity
    }

    private void showDeleteConfirmationDialog() {
        // AlertDialog
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Proceed with account deletion
                    deleteUserAccount();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Dismiss the dialog
                    dialog.dismiss();
                })
                .show();
    }

    private void deleteUserAccount() {
        if (currentUsername.isEmpty()) {
            Toast.makeText(this, "Unable to delete account.", Toast.LENGTH_SHORT).show();
            return;
        }

        db.deleteUser(currentUsername, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Account deleted successfully.", Toast.LENGTH_SHORT).show();
                logoutUser(); // Log out the user after deletion
            } else {
                Toast.makeText(this, "Failed to delete account. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
