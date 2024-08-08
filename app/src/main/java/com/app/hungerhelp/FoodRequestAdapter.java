package com.app.hungerhelp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.app.hungerhelp.R;
import com.app.hungerhelp.data.api.ApiClient;
import com.app.hungerhelp.models.Food;
import com.app.hungerhelp.models.FoodRequestModel;
import com.squareup.picasso.Picasso;
import java.util.List;

public class FoodRequestAdapter extends RecyclerView.Adapter<FoodRequestAdapter.FoodRequestViewHolder> {

    private List<FoodRequestModel> foodList;

    public FoodRequestAdapter(List<FoodRequestModel> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_request, parent, false);
        return new FoodRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodRequestViewHolder holder, int position) {
        FoodRequestModel food = foodList.get(position);
        holder.foodItem.setText(food.getFoodItem());
        holder.description.setText(food.getDescription());
        holder.location.setText("Location: " +food.getLocation());
        holder.status.setText("Your item is "+food.getRequests().get(0).getRequestStatus());

        if (!food.getImages().isEmpty()) {
            Picasso.get().load(ApiClient.IMAGE_URL + food.getImages().get(0)).into(holder.foodImage);
        }
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    static class FoodRequestViewHolder extends RecyclerView.ViewHolder {
        TextView foodItem, description, location, status;
        ImageView foodImage;

        FoodRequestViewHolder(View itemView) {
            super(itemView);
            foodItem = itemView.findViewById(R.id.foodItem);
            description = itemView.findViewById(R.id.description);
            location = itemView.findViewById(R.id.location);
            status = itemView.findViewById(R.id.status);
            foodImage = itemView.findViewById(R.id.foodPicture);
        }
    }
}
