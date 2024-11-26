package com.example.mobdevemobileapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
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
                Intent intent = new Intent(ForgotPassword.this, EnterCode.class);
                startActivity(intent);
            }
        });

    }


}


