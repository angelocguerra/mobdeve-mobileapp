package com.example.mobdevemobileapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    ArrayList<Review> reviews;
    Company company;
    CompanyPageActivity activity;

    public ReviewAdapter(ArrayList<Review> reviews, Company company, CompanyPageActivity activity) {
        this.reviews = reviews;
        this.company = company;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_review, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Review review = reviews.get(position);

        holder.tvReviewRating.setText(String.valueOf(review.getRatingScore()));
        holder.tvReviewTitle.setText(review.getReviewTitle());
        holder.tvReviewAuthor.setText(review.getUser() != null ? review.getUser().getUsername() : review.getUsername());
        holder.tvReviewDate.setText(review.getDatePosted());
        holder.tvReviewContent.setText(review.getReviewText());
        holder.srbReviewRating.setRating(review.getRatingScore());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ReviewPageActivity.class);
                intent.putExtra("companyName", company.getCompanyName());
                intent.putExtra("companyLocation", company.getCompanyLocation());
                intent.putExtra("companyImage", company.getCompanyImage());
                intent.putExtra("companyIndustry", company.getCompanyIndustry());

                intent.putExtra("workEnvironment", review.getWorkEnvironment());
                intent.putExtra("mentorship", review.getMentorship());
                intent.putExtra("workload", review.getWorkload());
                intent.putExtra("internshipType", review.convertInternshipType(review.getInternshipType()));
//                Log.d("showInternship", "Internship Type: " + review.convertInternshipType(review.getInternshipType()));
                intent.putExtra("allowanceProvision", review.convertAllowanceProvision(review.getAllowanceProvision()));
//                Log.d("showAllowance", "Allowance Provision: " + review.convertAllowanceProvision(review.getAllowanceProvision()));
                User user = review.getUser();
                if (user != null) {
                    intent.putExtra("username", user.getUsername());
                }
                else {
                    intent.putExtra("username", review.getUsername());
                }

                intent.putExtra("reviewTitle", review.getReviewTitle());

                intent.putExtra("datePosted", review.getDatePosted());
                intent.putExtra("rating", review.getRatingScore());
                intent.putExtra("reviewText", review.getReviewText());
                intent.putExtra("helpful", review.getHelpful());

                activity.startActivity(intent);
            }
        });

        holder.btnHelpful.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (holder.btnHelpful.isChecked()) {
                    Log.d("showChecked", "Button.isChecked = " + holder.btnHelpful.isChecked());
                    holder.btnHelpful.setBackgroundColor(activity.getResources().getColor(R.color.french_blue));
                    holder.btnHelpful.setTextColor(activity.getResources().getColor(R.color.pale_peach));
                    holder.btnHelpful.setIcon(activity.getResources().getDrawable(R.drawable.helpful_toggled));
                    Log.d("showChecked", "Button.isChecked = " + holder.btnHelpful.isChecked());
                    review.setHelpful(review.getHelpful() + 1);
                }
                else {
                    Log.d("showChecked", "Button.isChecked = " + holder.btnHelpful.isChecked());
                    holder.btnHelpful.setBackgroundColor(activity.getResources().getColor(R.color.light_grey));
                    holder.btnHelpful.setTextColor(activity.getResources().getColor(R.color.dark_jungle_green));
                    holder.btnHelpful.setIcon(activity.getResources().getDrawable(R.drawable.helpful));
                    Log.d("showChecked","Button.isChecked = " + holder.btnHelpful.isChecked());
                    review.setHelpful(review.getHelpful() - 1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvReviewRating, tvReviewTitle, tvReviewAuthor, tvReviewDate, tvReviewContent;
        SimpleRatingBar srbReviewRating;

        MaterialButton btnHelpful;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvReviewRating = itemView.findViewById(R.id.tvReviewRating);
            tvReviewTitle = itemView.findViewById(R.id.tvReviewTitle);
            tvReviewAuthor = itemView.findViewById(R.id.tvReviewAuthor);
            tvReviewDate = itemView.findViewById(R.id.tvReviewDate);
            tvReviewContent = itemView.findViewById(R.id.tvReviewContent);
            srbReviewRating = itemView.findViewById(R.id.srbReviewRating);
            btnHelpful = itemView.findViewById(R.id.btnHelpful);
        }
    }
}
