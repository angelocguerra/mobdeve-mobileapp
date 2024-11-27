package com.example.mobdevemobileapp;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;

public class ProfileReviewsAdapter extends RecyclerView.Adapter<ProfileReviewsAdapter.ViewHolder> {
    ArrayList<Review> reviews;
    Company[] companies;
    ProfileActivity activity;

    public ProfileReviewsAdapter(ArrayList<Review> reviews, Company[] companies, ProfileActivity activity) {
        this.reviews = reviews;
        this.companies = companies;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_user_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileReviewsAdapter.ViewHolder holder, int position) {
        final Review review = reviews.get(position);
        final Company company = companies[position];

        holder.tvUserReviewRating.setText(String.valueOf(review.getRatingScore()));
        holder.tvUserReviewIndustry.setText(String.valueOf(company.getCompanyIndustry()));
        holder.tvUserReviewCompanyName.setText(String.valueOf(company.getCompanyName()));
        holder.tvUserReviewTitle.setText(String.valueOf(review.getReviewTitle()));
        holder.tvUserReviewAuthor.setText(String.valueOf(review.getUser().getUsername()));
        holder.tvUserReviewDate.setText(String.valueOf(review.getDatePosted()));
        holder.tvReviewContent.setText(String.valueOf(review.getReviewText()));
        holder.ivReviewCompanyLogo.setImageResource(company.getCompanyImage());
        holder.srbUserReviewRating.setRating(review.getRatingScore());


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

                intent.putExtra("reviewTitle", review.getReviewTitle());
                intent.putExtra("user", review.getUser().getUsername());
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserReviewRating, tvUserReviewIndustry, tvUserReviewCompanyName;
        TextView tvUserReviewTitle, tvUserReviewAuthor, tvUserReviewDate, tvReviewContent;
        ImageView ivReviewCompanyLogo;
        SimpleRatingBar srbUserReviewRating;
        MaterialButton btnHelpful;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUserReviewRating = itemView.findViewById(R.id.tvUserReviewRating);
            tvUserReviewIndustry = itemView.findViewById(R.id.tvUserReviewIndustry);
            tvUserReviewCompanyName = itemView.findViewById(R.id.tvUserReviewCompanyName);
            tvUserReviewTitle = itemView.findViewById(R.id.tvUserReviewTitle);
            tvUserReviewAuthor = itemView.findViewById(R.id.tvUserReviewAuthor);
            tvUserReviewDate = itemView.findViewById(R.id.tvUserReviewDate);
            tvReviewContent = itemView.findViewById(R.id.tvReviewContent);
            ivReviewCompanyLogo = itemView.findViewById(R.id.ivReviewCompanyLogo);
            srbUserReviewRating = itemView.findViewById(R.id.srbUserReviewRating);
            btnHelpful = itemView.findViewById(R.id.btnHelpful);
        }
    }
}
