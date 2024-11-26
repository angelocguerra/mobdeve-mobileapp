package com.example.mobdevemobileapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
public class EnterCode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_code);
        // Set up the navbar
        Navbar.setupNavbar(this);
        Button btnEnterCode = findViewById(R.id.btnEnterCode);

        btnEnterCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EnterCode.this, ResetPassword.class);
                startActivity(intent);
            }
        });

    }


}


