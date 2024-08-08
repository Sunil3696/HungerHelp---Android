package com.app.hungerhelp;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.app.hungerhelp.data.api.ApiClient;
import com.app.hungerhelp.data.api.ApiService;
import com.app.hungerhelp.models.FoodDonateItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonateFoodAdapter extends RecyclerView.Adapter<DonateFoodAdapter.FoodViewHolder> {
    private List<FoodDonateItem> foodList;
    private Context context;
    private ApiService apiService;

    public DonateFoodAdapter(List<FoodDonateItem> foodList, Context context) {
        this.foodList = foodList;
        this.context = context;
        this.apiService = ApiClient.getRetrofitInstance(context).create(ApiService.class);
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donate_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodDonateItem food = foodList.get(position);
        holder.textViewFoodItem.setText(food.getFoodItem());
        holder.textViewDescription.setText(food.getDescription());

        if (food.getRequests() != null && !food.getRequests().isEmpty()) {
            holder.linearLayout.setVisibility(View.VISIBLE);
            if (food.getRequests().get(0).getRequestStatus().equals("Approved")) {
                holder.linearLayoutButton.setVisibility(View.GONE);
                holder.requestMessageTextView.setText("Your approval for " + food.getRequests().get(0).getUserFullName() + " has been noted. Your support is truly invaluable. " +
                        "You can reach at " +
                        food.getRequests().get(0).getUserPhoneNumber() + " to get in touch.");
            } else {
                holder.linearLayoutButton.setVisibility(View.VISIBLE);
                holder.requestMessageTextView.setText(food.getRequests().get(0).getUserFullName() +
                        " has reached out for this item. Your swift action or a quick call to " +
                        food.getRequests().get(0).getUserPhoneNumber() + " can make a big difference today. Let's make it happen!");
            }
        } else {
            holder.linearLayout.setVisibility(View.GONE);
        }

        if (food.getImages() != null && !food.getImages().isEmpty()) {
            Picasso.get()
                    .load(ApiClient.IMAGE_URL + food.getImages().get(0))
                    .placeholder(R.drawable.ic_food_placeholder)
                    .error(R.drawable.ic_food_placeholder)
                    .into(holder.imageViewFood);
        } else {
            holder.imageViewFood.setImageResource(R.drawable.ic_food_placeholder);
        }

        holder.acceptButton.setOnClickListener(v -> updateRequestStatus(food, "Approved", position));
        holder.declineButton.setOnClickListener(v -> updateRequestStatus(food, "Rejected", position));

        // Add click listener to show delete dialog
        holder.itemView.setOnClickListener(v -> showDeleteDialog(food, position));
    }

    private void showDeleteDialog(FoodDonateItem food, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Item?")
                .setMessage("Are you sure you want to delete this item from the list?")
                .setPositiveButton("Yes, I am sure", (dialog, which) -> deleteFoodItem(food, position))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteFoodItem(FoodDonateItem food, int position) {
        String foodId = food.get_id(); // Get the food item ID

        apiService.deleteFoodItem(foodId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    foodList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete item: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRequestStatus(FoodDonateItem food, String status, int position) {
        String foodId = food.get_id(); // Get the food item ID

        String jsonBody;
        RequestBody requestBody;

        if (status.equals("Approved")) {
            jsonBody = "{\"requestStatus\": \"Approved\"}";
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
        } else {
            jsonBody = "{\"requestStatus\": \"Rejected\"}";
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
        }

        apiService.updateRequestStatus(foodId, requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Request " + status, Toast.LENGTH_SHORT).show();
                    refreshData();
                } else {
                    Toast.makeText(context, "Failed to update request status: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshData() {
        apiService.getDonatedFoods().enqueue(new Callback<List<FoodDonateItem>>() {
            @Override
            public void onResponse(Call<List<FoodDonateItem>> call, Response<List<FoodDonateItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    foodList.clear();
                    foodList.addAll(response.body());
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Failed to refresh data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FoodDonateItem>> call, Throwable t) {
                Log.e("DonationsActivity", "Error fetching donated foods", t);
                Toast.makeText(context, "Error fetching donated foods", Toast.LENGTH_SHORT).show();
            }
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
        LinearLayout linearLayout;

        LinearLayout linearLayoutButton;
        TextView requestMessageTextView;
        Button acceptButton;
        Button declineButton;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFoodItem = itemView.findViewById(R.id.textFoodItem);
            textViewDescription = itemView.findViewById(R.id.textDescription);
            imageViewFood = itemView.findViewById(R.id.imageFood);
            linearLayout = itemView.findViewById(R.id.requestLayout);
            requestMessageTextView = itemView.findViewById(R.id.requestMessage);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);
            linearLayoutButton = itemView.findViewById(R.id.buttonLayout);
        }
    }
}