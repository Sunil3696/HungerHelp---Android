package com.app.hungerhelp;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.hungerhelp.FoodAdapter;
import com.app.hungerhelp.R;
import com.app.hungerhelp.data.api.ApiClient;
import com.app.hungerhelp.data.api.ApiService;
import com.app.hungerhelp.models.Food;
import com.app.hungerhelp.models.FoodDonateItem;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DonateFoodAdapter donateFoodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back button
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchDonatedFoods();
    }

    private void fetchDonatedFoods() {
        ApiService apiService = ApiClient.getRetrofitInstance(this).create(ApiService.class);
        Call<List<FoodDonateItem>> call = apiService.getDonatedFoods();
        call.enqueue(new Callback<List<FoodDonateItem>>() {
            @Override
            public void onResponse(Call<List<FoodDonateItem>> call, Response<List<FoodDonateItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<FoodDonateItem> foodList = response.body();
                    donateFoodAdapter = new DonateFoodAdapter(foodList, DonationsActivity.this);
                    recyclerView.setAdapter(donateFoodAdapter);
                } else {
                    Toast.makeText(DonationsActivity.this, "Failed to fetch donated foods", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FoodDonateItem>> call, Throwable t) {
                Log.e("DonationsActivity", "Error fetching donated foods", t);
                Toast.makeText(DonationsActivity.this, "Error fetching donated foods", Toast.LENGTH_SHORT).show();
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
