package com.example.mobdevemobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;
import java.util.List;

public class CompanyPageActivity extends AppCompatActivity {

    FirestoreManager db;

    TextView tvIndustry, tvCompanyTitle, tvCompanyLocation, tvCompanyRating, tvCompanyReviewCount;
    ImageView ivCompanyLogo;
    SimpleRatingBar srbCompanyRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirestoreManager.getInstance();

        tvIndustry = findViewById(R.id.tvCompanyIndustry);
        tvCompanyTitle = findViewById(R.id.tvCompanyName);
        tvCompanyLocation = findViewById(R.id.tvCompanyLocation);
        tvCompanyRating = findViewById(R.id.tvCompanyRating);
        tvCompanyReviewCount = findViewById(R.id.tvCompanyReviewCount);
        ivCompanyLogo = findViewById(R.id.ivCompanyLogo);
        srbCompanyRating = findViewById(R.id.srbCompanyRating);

        Company currentCompany = this.populatePage();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchReviews(currentCompany, reviews -> {
            currentCompany.setCompanyReviews(reviews);
            tvCompanyReviewCount.setText(String.valueOf(reviews.size()));
            ReviewAdapter reviewAdapter = new ReviewAdapter(reviews, currentCompany, this);
            recyclerView.setAdapter(reviewAdapter);
        });
    }

    public Company populatePage() {
        Intent intent = getIntent();

        String industry = intent.getStringExtra("companyIndustry");
        tvIndustry.setText(industry);
        Log.d("addIndustry", "Industry added");

        String companyTitle = intent.getStringExtra("companyName");
        tvCompanyTitle.setText(companyTitle);
        Log.d("addTitle", "Title added");

        String companyLocation = intent.getStringExtra("companyLocation");
        tvCompanyLocation.setText(companyLocation);
        Log.d("addLocation", "Location added");

        int reviewsCount = intent.getIntExtra("reviewsCount", 0);
        tvCompanyReviewCount.setText(String.valueOf(reviewsCount));
        Log.d("addCount", "Count added");

        float companyRating = intent.getFloatExtra("companyRating", 0);
        tvCompanyRating.setText(String.valueOf(companyRating));
        Log.d("addRating", "Numerical Rating added");

        int companyLogo = intent.getIntExtra("companyImage", 0);
        ivCompanyLogo.setImageResource(companyLogo);
        Log.d("addLogo", "Logo added");

        srbCompanyRating.setRating(companyRating);
        Log.d("addRatingBar", "Rating Bar added");

        return new Company(industry, companyTitle, companyLogo, companyLocation, companyRating);
    }

    public void fetchReviews(Company company, OnReviewsFetchedListener listener) {
        db.retrieveAllReviewsGivenCompany(company.getCompanyName(), task -> {
            if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                ArrayList<Review> reviews = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult()) {
                    try {
                        if (document.exists()) {
                            String companyName = document.getString("companyName");
                            String reviewTitle = document.getString("reviewTitle");
                            String reviewText = document.getString("reviewText");
                            String datePosted = document.getString("datePosted");
                            String username = document.getString("username");

                            String uuid = document.getString("uuid");

                            float workEnvironment = Float.parseFloat(String.valueOf(document.getDouble("workEnvironment")));
                            float mentorship = Float.parseFloat(String.valueOf(document.getDouble("mentorship")));
                            float workload = Float.parseFloat(String.valueOf(document.getDouble("workload")));

                            float ratingScore = (workEnvironment + mentorship + workload) / 3;

                            InternshipType internshipType = InternshipType.valueOf(document.getString("internshipType"));
                            AllowanceProvision allowanceProvision = AllowanceProvision.valueOf(document.getString("allowanceProvision"));

                            Review review = new Review(ratingScore, workEnvironment, mentorship, workload,
                                    companyName, internshipType, allowanceProvision, uuid, reviewTitle, new User(username), datePosted, reviewText);

                            reviews.add(review);
                        }
                    } catch (Exception e) {
                        Log.e("emptyReviewsList", "Reviews are empty", e);
                    }
                }
                listener.onReviewsFetched(reviews);
            } else {
                listener.onReviewsFetched(new ArrayList<>());
            }
        });
    }

    public interface OnReviewsFetchedListener {
        void onReviewsFetched(ArrayList<Review> reviews);
    }
}