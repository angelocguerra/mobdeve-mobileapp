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
    ProfileFragment fragment;

    public ProfileReviewsAdapter(ArrayList<Review> reviews, ProfileFragment fragment) {
        this.reviews = reviews;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileReviewsAdapter.ViewHolder holder, int position) {
        final Review review = reviews.get(position);

        // Display to log all values
        Log.d("showReview", "Rating: " + review.getRatingScore());
        Log.d("showReview", "Title: " + review.getReviewTitle());
        Log.d("showReview", "Author: " + review.getUser().getUsername());
        Log.d("showReview", "Content: " + review.getReviewText());
        Log.d("showReview", "Helpful: " + review.getHelpful());

        holder.tvReviewRating.setText(String.format("%.2f", review.getRatingScore()));
        holder.tvReviewTitle.setText(review.getReviewTitle());
        holder.tvReviewAuthor.setText(review.getUser().getUsername());
//        holder.tvReviewDate.setText(review.getDatePosted());
        holder.tvReviewContent.setText(review.getReviewText());
        holder.srbReviewRating.setRating(review.getRatingScore());


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(activity, ReviewPageActivity.class);
//                intent.putExtra("companyName", company.getCompanyName());
//                intent.putExtra("companyLocation", company.getCompanyLocation());
//                intent.putExtra("companyImage", company.getCompanyImage());
//                intent.putExtra("companyIndustry", company.getCompanyIndustry());
//
//                intent.putExtra("workEnvironment", review.getWorkEnvironment());
//                intent.putExtra("mentorship", review.getMentorship());
//                intent.putExtra("workload", review.getWorkload());
//                intent.putExtra("internshipType", review.convertInternshipType(review.getInternshipType()));
////                Log.d("showInternship", "Internship Type: " + review.convertInternshipType(review.getInternshipType()));
//                intent.putExtra("allowanceProvision", review.convertAllowanceProvision(review.getAllowanceProvision()));
////                Log.d("showAllowance", "Allowance Provision: " + review.convertAllowanceProvision(review.getAllowanceProvision()));
//
//                intent.putExtra("reviewTitle", review.getReviewTitle());
//                intent.putExtra("user", review.getUser().getUsername());
//                intent.putExtra("datePosted", review.getDatePosted());
//                intent.putExtra("rating", review.getRatingScore());
//                intent.putExtra("reviewText", review.getReviewText());
//                intent.putExtra("helpful", review.getHelpful());
//
//                activity.startActivity(intent);
//            }
//        });

        holder.btnHelpful.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (holder.btnHelpful.isChecked()) {
                    Log.d("showChecked", "Button.isChecked = " + holder.btnHelpful.isChecked());
                    holder.btnHelpful.setBackgroundColor(fragment.getResources().getColor(R.color.french_blue));
                    holder.btnHelpful.setTextColor(fragment.getResources().getColor(R.color.pale_peach));
                    holder.btnHelpful.setIcon(fragment.getResources().getDrawable(R.drawable.helpful_toggled));
                    Log.d("showChecked", "Button.isChecked = " + holder.btnHelpful.isChecked());
                    review.setHelpful(review.getHelpful() + 1);
                }
                else {
                    Log.d("showChecked", "Button.isChecked = " + holder.btnHelpful.isChecked());
                    holder.btnHelpful.setBackgroundColor(fragment.getResources().getColor(R.color.light_grey));
                    holder.btnHelpful.setTextColor(fragment.getResources().getColor(R.color.dark_jungle_green));
                    holder.btnHelpful.setIcon(fragment.getResources().getDrawable(R.drawable.helpful));
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

        TextView tvReviewRating;
        TextView tvReviewTitle, tvReviewAuthor, tvReviewDate, tvReviewContent;
        SimpleRatingBar srbReviewRating;
        MaterialButton btnHelpful;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvReviewRating = itemView.findViewById(R.id.tvReviewRating);
            tvReviewTitle = itemView.findViewById(R.id.tvReviewTitle);
            tvReviewAuthor = itemView.findViewById(R.id.tvReviewAuthor);
            tvReviewDate = itemView.findViewById(R.id.tvUserReviewDate);
            tvReviewContent = itemView.findViewById(R.id.tvReviewContent);
            srbReviewRating = itemView.findViewById(R.id.srbReviewRating);
            btnHelpful = itemView.findViewById(R.id.btnHelpful);
        }
    }
}
