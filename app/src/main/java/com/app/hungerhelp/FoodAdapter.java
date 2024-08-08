package com.app.hungerhelp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.hungerhelp.data.api.ApiClient;
import com.app.hungerhelp.models.Food;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<Food> foodList;
    private Context context;

    public FoodAdapter(List<Food> foodList, Context context) {
        this.foodList = foodList;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.textViewFoodItem.setText(food.getFoodItem());
        holder.textViewDescription.setText(food.getDescription());

        if (food.getImages() != null && !food.getImages().isEmpty()) {
            Picasso.get()
                    .load(ApiClient.IMAGE_URL+food.getImages().get(0))
                    .placeholder(R.drawable.ic_food_placeholder)
                    .error(R.drawable.ic_food_placeholder)
                    .into(holder.imageViewFood);
        } else {
            holder.imageViewFood.setImageResource(R.drawable.ic_food_placeholder);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FoodDetailsActivity.class);
            intent.putExtra("foodDetails", food);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFoodItem;
        TextView textViewDescription;
        ImageView imageViewFood;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFoodItem = itemView.findViewById(R.id.textFoodItem);
            textViewDescription = itemView.findViewById(R.id.textDescription);
            imageViewFood = itemView.findViewById(R.id.imageFood);
        }
    }
}

