package com.example.mobdevemobileapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChangePassword extends AppCompatActivity {
    private FirestoreManager db;
    private SharedPreferences sharedPreferences;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_page);

        // Initialize FirestoreManager and SharedPreferences
        db = FirestoreManager.getInstance();
        sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);

        // Get the current username from SharedPreferences
        currentUsername = sharedPreferences.getString("username", null);

        // Ensure a user is logged in before accessing this page
        if (currentUsername == null) {
            Toast.makeText(this, "No user logged in. Redirecting to Login...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Initialize UI components
        EditText etCurrentPassword = findViewById(R.id.etCurrentPassword);
        EditText etNewPassword = findViewById(R.id.etNewPassword);
        EditText etConfirmPassword = findViewById(R.id.etConfirmPassword);
        Button btnChangePassword = findViewById(R.id.btnChangePassword);

        // Handle Change Password button click
        btnChangePassword.setOnClickListener(view -> {
            String currentPassword = etCurrentPassword.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // Validate input fields
            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "New passwords do not match.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check the current password and validate the new password
            db.getUser(currentUsername, task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    String storedHashedPassword = task.getResult().getString("password");
                    String inputHashedPassword = User.hashPassword(currentPassword);
                    String newHashedPassword = User.hashPassword(newPassword);

                    if (!storedHashedPassword.equals(inputHashedPassword)) {
                        Toast.makeText(this, "Current password is incorrect.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (storedHashedPassword.equals(newHashedPassword)) {
                        Toast.makeText(this, "New password cannot be the same as the current password.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Update the password in Firestore
                    db.getDb()
                            .collection("users")
                            .document(currentUsername)
                            .update("password", newHashedPassword)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Password changed successfully!", Toast.LENGTH_SHORT).show();
                                navigateToProfile();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Failed to update password. Please try again.", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(this, "Failed to verify user. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void navigateToProfile() {
        Intent intent = new Intent(ChangePassword.this, ProfileFragment.class);
        startActivity(intent);
        finish(); // Finish ChangePasswordActivity
    }
}
