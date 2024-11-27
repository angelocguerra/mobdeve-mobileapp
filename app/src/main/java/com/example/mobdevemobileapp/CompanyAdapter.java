package com.example.mobdevemobileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> {
    Company[] companies;
    Context context; // Use Context instead of MainPageActivity

    public CompanyAdapter(Company[] companies, Context context) {
        this.companies = companies;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_company, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyAdapter.ViewHolder holder, int position) {
        final Company company = companies[position];
        holder.tvIndustry.setText(company.getCompanyIndustry());
        holder.tvCompanyTitle.setText(company.getCompanyName());
        holder.tvCompanyLocation.setText(company.getCompanyLocation());
        holder.tvCompanyRating.setText(String.valueOf(company.getCompanyRating()));
        holder.srbCompanyRating.setRating(company.getCompanyRating());
        holder.tvCompanyReviewCount.setText(String.valueOf(company.getCompanyReviews().size()));
        holder.ivCompanyLogo.setImageResource(company.getCompanyImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CompanyPageActivity.class);
                intent.putExtra("companyIndustry", company.getCompanyIndustry());
                intent.putExtra("companyName", company.getCompanyName());
                intent.putExtra("companyLocation", company.getCompanyLocation());
                intent.putExtra("companyRating", company.getCompanyRating());
                intent.putExtra("companyImage", company.getCompanyImage());
                intent.putExtra("reviewsCount", company.getCompanyReviews().size());
                intent.putExtra("companyReviews", company.getCompanyReviews());

                context.startActivity(intent); // Use context here
            }
        });
    }

    @Override
    public int getItemCount() {
        return companies.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIndustry;
        TextView tvCompanyTitle;
        TextView tvCompanyLocation;
        TextView tvCompanyRating;
        SimpleRatingBar srbCompanyRating;
        TextView tvCompanyReviewCount;
        ImageView ivCompanyLogo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIndustry = itemView.findViewById(R.id.tvIndustry);
            tvCompanyTitle = itemView.findViewById(R.id.tvCompanyTitle);
            tvCompanyLocation = itemView.findViewById(R.id.tvCompanyLocation);
            tvCompanyRating = itemView.findViewById(R.id.tvCompanyRating);
            srbCompanyRating = itemView.findViewById(R.id.srbCompanyRating);
            tvCompanyReviewCount = itemView.findViewById(R.id.tvCompanyReviewCount);
            ivCompanyLogo = itemView.findViewById(R.id.ivCompanyLogo);
        }
    }
}
