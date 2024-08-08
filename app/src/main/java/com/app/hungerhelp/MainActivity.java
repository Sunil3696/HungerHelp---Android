package com.app.hungerhelp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.app.hungerhelp.data.api.ApiClient;
import com.app.hungerhelp.data.api.ApiService;
import com.app.hungerhelp.data.api.storage.SharedPrefManager;
import com.app.hungerhelp.models.Category;
import com.app.hungerhelp.models.Food;
import com.google.android.material.navigation.NavigationView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerViewCategories;
    private RecyclerView recyclerViewFoods;
    private CategoryAdapter categoryAdapter;
    private FoodAdapter foodAdapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // DrawerLayout and NavigationView setup
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // ActionBarDrawerToggle setup
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // RecyclerView setup
        recyclerViewCategories = findViewById(R.id.recyclerView);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recyclerViewFoods = findViewById(R.id.recyclerViewFoods);
        recyclerViewFoods.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Fetch categories and food items from API
        fetchCategories();
        fetchFoodItems();
    }

    private void fetchCategories() {
        ApiService apiService = ApiClient.getRetrofitInstance(getApplicationContext()).create(ApiService.class);
        Call<List<Category>> call = apiService.getCategories();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body();
                    categoryAdapter = new CategoryAdapter(categories);
                    recyclerViewCategories.setAdapter(categoryAdapter);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load categories", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.d("Failed", t.getMessage());
                Toast.makeText(MainActivity.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchFoodItems() {
        ApiService apiService = ApiClient.getRetrofitInstance(getApplicationContext()).create(ApiService.class);
        Call<List<Food>> call = apiService.getFoodItems();

        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Food> foods = response.body();
                    foodAdapter = new FoodAdapter(foods, MainActivity.this);
                    recyclerViewFoods.setAdapter(foodAdapter);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load food items", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                Log.d("Failed", t.getMessage());
                Toast.makeText(MainActivity.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_profile:
                // Handle profile click
                Intent intentProfile = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intentProfile);
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_donate_food:
                // Handle donate food click
                Intent intent = new Intent(MainActivity.this, AddFoodActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Donate Food clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_view_requested_items:
                // Handle view requested items click
                Intent requestIntent = new Intent(MainActivity.this, RequestsActivity.class);
                startActivity(requestIntent);
                Toast.makeText(this, "View Requested Items clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_view_posted_items_request:
                // Handle view posted items request click
                Intent donatedIntent = new Intent(MainActivity.this, DonationsActivity.class);
                startActivity(donatedIntent);
                Toast.makeText(this, "View Posted Items Request clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                // Handle logout click
                SharedPrefManager.getInstance(getApplicationContext()).clear(); // Clear shared preferences
                Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                finish(); // Close MainActivity to prevent the user from returning to it with the back button
                Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
