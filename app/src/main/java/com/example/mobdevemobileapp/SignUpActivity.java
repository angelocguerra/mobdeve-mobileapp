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
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);


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

        // On successful account creation, navigate to login
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}

