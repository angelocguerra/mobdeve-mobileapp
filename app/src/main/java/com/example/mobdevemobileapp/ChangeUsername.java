package com.example.mobdevemobileapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChangeUsername extends AppCompatActivity {

    private FirestoreManager db; // Firestore Manager for database operations
    private SharedPreferences sharedPreferences;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_username_page);

        // Initialize FirestoreManager and SharedPreferences
        db = FirestoreManager.getInstance();
        sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);

        // Get the currently logged-in username from SharedPreferences
        currentUsername = sharedPreferences.getString("username", "Guest");

        // Display the current username
        TextView tvCurrentUsername = findViewById(R.id.tvCurrentUsername);
        tvCurrentUsername.setText(currentUsername);

        // Handle Change Username Button
        Button btnChangeUsername = findViewById(R.id.btnChangeUsername);
        btnChangeUsername.setOnClickListener(view -> handleChangeUsername());
    }

    private void handleChangeUsername() {
        // Get the new username from the EditText
        EditText etNewUsername = findViewById(R.id.etNewUsername);
        String newUsername = etNewUsername.getText().toString().trim();

        // Validate the input
        if (newUsername.isEmpty()) {
            Toast.makeText(this, "New username cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newUsername.equals(currentUsername)) {
            Toast.makeText(this, "New username cannot be the same as the current username", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update username in Firestore
        db.updateUsername(currentUsername, newUsername, task -> {
            if (task.isSuccessful()) {
                // Update SharedPreferences with the new username
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", newUsername);
                editor.apply();

                // Notify the user and navigate back to the ProfileActivity
                Toast.makeText(this, "Username updated successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangeUsername.this, ProfileFragment.class);
                startActivity(intent);
                finish();
            } else {
                String errorMessage = task.getException() != null ? task.getException().getMessage() : "An error occurred";
                Toast.makeText(this, "Failed to update username: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
