package com.example.mobdevemobileapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;

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

        holder.tvReviewRating.setText(String.valueOf(review.getRatingScore()));
        holder.tvReviewTitle.setText(review.getReviewTitle());
        holder.tvReviewAuthor.setText(review.getUser().getUsername());
        holder.tvReviewDate.setText(review.getDatePosted());
        holder.tvReviewContent.setText(review.getReviewText());
        holder.srbReviewRating.setRating(review.getRatingScore());

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
        TextView tvReviewRating, tvReviewTitle, tvReviewAuthor, tvReviewDate, tvReviewContent;
        SimpleRatingBar srbReviewRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReviewRating = itemView.findViewById(R.id.tvReviewRating);
            tvReviewTitle = itemView.findViewById(R.id.tvReviewTitle);
            tvReviewAuthor = itemView.findViewById(R.id.tvReviewAuthor);
            tvReviewDate = itemView.findViewById(R.id.tvReviewDate);
            tvReviewContent = itemView.findViewById(R.id.tvReviewContent);
            srbReviewRating = itemView.findViewById(R.id.srbReviewRating);
        }
    }
}
