// SignUpActivity.java
package com.example.mobdevemobileapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.ProgressBar;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class SignUpActivity extends AppCompatActivity {
    private FirestoreManager db;
    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        db = FirestoreManager.getInstance();
        TextView tvAlreadyHaveAccount = findViewById(R.id.tvAlreadyHaveAccount);

        // Create a SpannableString for the text
        String text = "Already have an account? Log In";
        SpannableString spannableString = new SpannableString(text);

        // Make "Log In" blue and clickable
        int startIndex = text.indexOf("Log In");
        int endIndex = startIndex + "Log In".length();

        // Set the text color to blue
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the click action for "Log In"
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Navigate to LoginActivity
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Apply the SpannableString to the TextView
        tvAlreadyHaveAccount.setText(spannableString);
        tvAlreadyHaveAccount.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
    }

    public void signUp(View view) {
        // Show the loading screen
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_dialog, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        progressDialog = builder.create();
        progressDialog.show();

        // Validate sign-up logic here (e.g., Firebase Authentication)
        // Get user input

        String username = ((TextView) findViewById(R.id.etUsername)).getText().toString();
        String email = ((TextView) findViewById(R.id.etEmail)).getText().toString();
        String password = ((TextView) findViewById(R.id.etPassword)).getText().toString();
        String confirmPassword = ((TextView) findViewById(R.id.etConfirmPassword)).getText().toString();

        Date date_today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");
        String dateWithoutTime = formatter.format(date_today);

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        AtomicBoolean userExists = new AtomicBoolean(false);

        FirestoreManager.getInstance().checkIfUserExists("username", username, task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                userExists.set(true);
                progressDialog.dismiss();

                EditText etUsername = findViewById(R.id.etUsername);
                etUsername.setError("Username already exists");

                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            } else {
                FirestoreManager.getInstance().checkIfUserExists("email", email, emailTask -> {
                    if (emailTask.isSuccessful() && !emailTask.getResult().isEmpty()) {
                        userExists.set(true);
                        EditText etEmail = findViewById(R.id.etEmail);
                        etEmail.setError("Email already exists");
                        Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!userExists.get()) {
                            // Hash the password
                            String hashedPassword = User.hashPassword(password);

                            // Add user to Firestore
                            Map<String, Object> user = new HashMap<>();

                            user.put("username", username);
                            user.put("email", email);
                            user.put("password", hashedPassword);
                            user.put("date_created", dateWithoutTime);
                            // Add empty array for user's reviews
                            user.put("reviews", new HashMap<>());

                            db.addUser(username, user, task1 -> {
                                progressDialog.dismiss();
                                Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);
                            });
                        }
                    }
                });
            }
        });
    }
}