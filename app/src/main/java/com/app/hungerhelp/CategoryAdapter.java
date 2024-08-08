package com.app.hungerhelp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.hungerhelp.data.api.ApiClient;
import com.app.hungerhelp.models.Category;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> categoryList;

    public CategoryAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.categoryTitle.setText(category.getTitle());

        String imagePath = category.getIcon();

        Log.d("Picasso", "Loading image URL: " + category.getIcon());

        Picasso.get()
                .load(ApiClient.IMAGE_URL+imagePath)
                .placeholder(R.drawable.ic_food_placeholder) // Optional placeholder image
                .error(R.drawable.ic_food_placeholder)             // Optional error image
                .into(holder.categoryIcon, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("Picasso", "Image loaded successfully");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Picasso", "Failed to load image", e);
                    }
                });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryIcon;
        TextView categoryTitle;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.category_icon);
            categoryTitle = itemView.findViewById(R.id.category_title);
        }
    }
}