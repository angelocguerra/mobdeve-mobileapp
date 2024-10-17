package com.example.mobdevemobileapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView titleTextView = findViewById(R.id.title_text_view);

        SpannableStringBuilder spannable = new SpannableStringBuilder("Welcome to\nInternSHIP");

        // Apply different font sizes and styles
        spannable.setSpan(new RelativeSizeSpan(25f / 24f), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // "Welcome to"
        spannable.setSpan(new RelativeSizeSpan(45f / 24f), 11, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // "InternSHIP"
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 17, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // "SHIP"

        titleTextView.setText(spannable);
    }
}