package com.example.mobdevemobileapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    Review[] reviews;
    CompanyPageActivity activity;

    public ReviewAdapter(Review[] reviews, CompanyPageActivity activity) {
        this.reviews = reviews;
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
        final Review review = reviews[position];
        holder.reviewTitleView.setText(review.getReviewTitle());
        holder.userView.setText(review.getUser().getUsername());
        holder.datePostedView.setText(review.getDatePosted());
        holder.reviewRatingView.setRating(review.getRatingScore());
        holder.reviewTextView.setText(review.getReviewText());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ReviewPageActivity.class);
                intent.putExtra("reviewTitle", review.getReviewTitle());
                intent.putExtra("user", review.getUser().getUsername());
                intent.putExtra("datePosted", review.getDatePosted());
                intent.putExtra("rating", review.getRatingScore());
                intent.putExtra("reviewText", review.getReviewText());
                intent.putExtra("helpful", review.getHelpful());

                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviews.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView reviewTitleView;
        TextView userView;
        TextView datePostedView;
        RatingBar reviewRatingView;
        TextView reviewTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewTitleView = itemView.findViewById(R.id.reviewTitleView);
            userView = itemView.findViewById(R.id.userView);
            datePostedView = itemView.findViewById(R.id.datePostedView);
            reviewRatingView = itemView.findViewById(R.id.reviewRatingView);
            reviewTextView = itemView.findViewById(R.id.reviewTextView);
        }
    }
}
