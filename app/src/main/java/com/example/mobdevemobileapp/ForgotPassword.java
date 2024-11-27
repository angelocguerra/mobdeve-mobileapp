package com.example.mobdevemobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_page);

        // Set up the navbar
        Navbar.setupNavbar(this);

        Button btnResetPassword = findViewById(R.id.btnResetPassword);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail(); // Send the email before navigating
            }
        });
    }

    private void sendEmail() {
        // Initialize Brevo API client
        BrevoApi brevoApi = RetrofitClient.getBrevoApi();



        // Create email request payload
        Map<String, String> sender = new HashMap<>();
        sender.put("email", "shem_matthew_salih@dlsu.edu.ph"); // Replace with your sender email
        sender.put("name", "Your Name"); // Replace with your sender name

        Map<String, String> recipient = new HashMap<>();
        recipient.put("email", "shemsalihte@gmail.com"); // Recipient's email
        recipient.put("name", "Shemsalihte"); // Recipient's name

        EmailRequest emailRequest = new EmailRequest(
                sender,
                Collections.singletonList(recipient),
                "Password Reset Request",
                "<h1>Reset your password</h1><p>Please click the link below to reset your password:</p><a href='https://example.com/reset-password'>Reset Password</a>"
        );

        // Send email using Brevo API
        brevoApi.sendEmail(emailRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ForgotPassword.this, "Email sent successfully!", Toast.LENGTH_SHORT).show();
                    navigateToEnterCode();
                } else {
                    Toast.makeText(ForgotPassword.this, "Failed to send email: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ForgotPassword.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigateToEnterCode() {
        Intent intent = new Intent(ForgotPassword.this, EnterCode.class);
        startActivity(intent);
        finish();
    }
}
