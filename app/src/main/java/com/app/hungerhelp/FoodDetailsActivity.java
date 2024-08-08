package com.app.hungerhelp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hungerhelp.data.api.ApiClient;
import com.app.hungerhelp.data.api.ApiService;
import com.app.hungerhelp.models.Food;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodDetailsActivity extends AppCompatActivity {

    private ImageView imageViewFood;
    private TextView textViewFoodItem, textViewDescription, textViewQuantity, textViewLocation, textViewAvailableTill, textViewNotes;
    private Button buttonRequest;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imageViewFood = findViewById(R.id.imageViewFood);
        textViewFoodItem = findViewById(R.id.textViewFoodItem);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewQuantity = findViewById(R.id.textViewQuantity);
        textViewLocation = findViewById(R.id.textViewLocation);
        textViewAvailableTill = findViewById(R.id.textViewAvailableTill);
        textViewNotes = findViewById(R.id.textViewNotes);
        buttonRequest = findViewById(R.id.buttonRequest);

        apiService = ApiClient.getRetrofitInstance(this).create(ApiService.class);

        Food food = (Food) getIntent().getSerializableExtra("foodDetails");

        if (food != null) {
            textViewFoodItem.setText(food.getFoodItem());
            textViewDescription.setText(food.getDescription());
            textViewQuantity.setText(food.getQuantity());
            textViewLocation.setText(food.getLocation());
            textViewAvailableTill.setText(food.getAvailableTill());
            textViewNotes.setText(food.getNotes());

            if (food.getImages() != null && !food.getImages().isEmpty()) {
                Picasso.get()
                        .load(ApiClient.IMAGE_URL+food.getImages().get(0))
                        .placeholder(R.drawable.ic_food_placeholder)
                        .error(R.drawable.ic_food_placeholder)
                        .into(imageViewFood);
            } else {
                imageViewFood.setImageResource(R.drawable.ic_food_placeholder);
            }

            updateButtonState(food);
        }
    }

    private void updateButtonState(Food food) {
        if ("Available".equals(food.getStatus())) {
            buttonRequest.setText("Request");
            buttonRequest.setEnabled(true);
        } else if ("Already Applied".equals(food.getStatus())) {
            buttonRequest.setText("Already Applied");
            buttonRequest.setEnabled(false);
        } else if ("Not Available".equals(food.getStatus())) {
            buttonRequest.setText("Not Available");
            buttonRequest.setEnabled(false);
        }

        buttonRequest.setOnClickListener(view -> {
            if ("Available".equals(food.getStatus())) {
                requestFoodItem(food.get_id());
            }
        });
    }

    private void requestFoodItem(String foodId) {
        Log.d("Foodid", foodId); // 66b190de549820cbb5486c32
        Call<ResponseBody> call = apiService.requestFood(foodId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(FoodDetailsActivity.this, "Food request successful", Toast.LENGTH_SHORT).show();
                    buttonRequest.setText("Already Applied");
                    buttonRequest.setEnabled(false);
                } else {
                    Toast.makeText(FoodDetailsActivity.this, "Failed to request food", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(FoodDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Handle back button press
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

