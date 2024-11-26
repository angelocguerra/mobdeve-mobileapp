package com.example.mobdevemobileapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
public class ChangeUsername extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_username_page);
        // Set up the navbar
        Navbar.setupNavbar(this);
        Button btnChangeUsername = findViewById(R.id.btnChangeUsername);

        btnChangeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeUsername.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

    }


}


