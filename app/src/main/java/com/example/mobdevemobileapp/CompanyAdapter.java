package com.example.mobdevemobileapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> {

    Company[] companies;
    MainPageActivity activity;

    public CompanyAdapter(Company[] companies, MainPageActivity activity) {
        this.companies = companies;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_company, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyAdapter.ViewHolder holder, int position) {
        final Company company = companies[position];
        holder.companyImageView.setImageResource(company.getCompanyImage());
        holder.companyNameView.setText(company.getCompanyName());
        holder.companyLocationView.setText(company.getCompanyLocation());
        holder.reviewsView.setText(company.getReviews().size() + " reviews");
        holder.ratingView.setRating(company.getRating());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CompanyPageActivity.class);
                intent.putExtra("companyName", company.getCompanyName());
                intent.putExtra("companyImage", company.getCompanyImage());
                intent.putExtra("companyLocation", company.getCompanyLocation());
                intent.putExtra("rating", company.getRating());
                intent.putExtra("reviews", company.getReviews());
                intent.putExtra("reviewsCount", company.getReviews().size());

                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return companies.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView companyImageView;
        TextView companyNameView;
        TextView companyLocationView;
        RatingBar ratingView;
        TextView reviewsView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            companyImageView = itemView.findViewById(R.id.companyImageView);
            companyNameView = itemView.findViewById(R.id.companyNameView);
            companyLocationView = itemView.findViewById(R.id.companyLocationView);
            ratingView = itemView.findViewById(R.id.ratingView);
            reviewsView = itemView.findViewById(R.id.reviewsView);
        }
    }
}
