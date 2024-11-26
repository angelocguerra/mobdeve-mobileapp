package com.example.mobdevemobileapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
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


    // Method linked to the button via android:onClick
    public void navigateToMainPage(View view) {
        boolean isLoginSuccessful = validateLogin();

        if (isLoginSuccessful) {
            // Navigate to MainPageActivity
            Intent intent = new Intent(LoginActivity.this, MainPageActivity.class);
            startActivity(intent);

            // Optionally finish LoginActivity to prevent going back to it
            finish();
        } else {
            // Show an error message
            Toast.makeText(this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateLogin() {
        // Placeholder for actual validation logic
        return true; // For now, assume login is always successful
    }
}

