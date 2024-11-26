// SignUpActivity.java
package com.example.mobdevemobileapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class SignUpActivity extends AppCompatActivity {
    private FirestoreManager db;

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
        // Validate sign-up logic here (e.g., Firebase Authentication)
        // Get user input
        String fullName = ((TextView) findViewById(R.id.etFullName)).getText().toString();
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
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            } else {
                FirestoreManager.getInstance().checkIfUserExists("email", email, emailTask -> {
                    if (emailTask.isSuccessful() && !emailTask.getResult().isEmpty()) {
                        userExists.set(true);
                        Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!userExists.get()) {
                            // Hash the password
                            String hashedPassword = User.hashPassword(password);

                            // Add user to Firestore
                            Map<String, Object> user = new HashMap<>();
                            user.put("fullName", fullName);
                            user.put("username", username);
                            user.put("email", email);
                            user.put("password", hashedPassword);
                            user.put("date_created", dateWithoutTime);
                            // Add empty array for user's reviews
                            user.put("reviews", new HashMap<>());

                            FirestoreManager.getInstance().addUser(username, user);

                            // On successful account creation, navigate to login
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }
}