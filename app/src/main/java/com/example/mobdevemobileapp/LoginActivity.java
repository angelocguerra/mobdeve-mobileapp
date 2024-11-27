// LoginActivity.java
package com.example.mobdevemobileapp;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;

public class LoginActivity extends AppCompatActivity {
    private FirestoreManager db;
    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        db = FirestoreManager.getInstance();

        // Check if user is already logged in
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            navigateToMainPage();
        }

        // Make "Register" clickable
        TextView tvNoAccount = findViewById(R.id.tvNoAccount);
        String text = "Don't have an account? Register";

        SpannableString spannableString = new SpannableString(text);

        // Set the color and make "Register" clickable
        int startIndex = text.indexOf("Register");
        int endIndex = startIndex + "Register".length();

        // Set the text color for "Register"
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#0b70bd")), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the click action for "Register"
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Navigate to SignUpActivity
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(android.text.TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false); // Remove underline
            }
        }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Apply the SpannableString to the TextView
        tvNoAccount.setText(spannableString);
        tvNoAccount.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());

    }

    // LoginActivity.java
    public void login(View view) {
        // Show the loading screen
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_dialog_login, null);

        builder.setView(dialogView);
        builder.setCancelable(false);
        progressDialog = builder.create();
        progressDialog.show();

        String username = ((TextView) findViewById(R.id.etUsername)).getText().toString();
        String password = ((TextView) findViewById(R.id.etPassword)).getText().toString();

        db.getUser(username, task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String storedHashedPassword = document.getString("password");
                    String inputHashedPassword = User.hashPassword(password);
                    if (storedHashedPassword.equals(inputHashedPassword)) {
                        // Save login state and profile data
                        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("username", username);
                        editor.apply();

                        // Navigate to MainPageActivity
                        navigateToMainPage();
                    } else {
                        Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToMainPage() {
        Intent intent = new Intent(LoginActivity.this, MainPageActivity.class);
        startActivity(intent);
        finish(); // Finish LoginActivity to prevent going back to it
    }

    public void goToForgotPassword(View view) {
        Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
        startActivity(intent);
    }

}