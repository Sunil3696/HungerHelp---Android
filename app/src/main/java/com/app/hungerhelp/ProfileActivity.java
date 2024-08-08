package com.app.hungerhelp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.hungerhelp.data.api.ApiClient;
import com.app.hungerhelp.data.api.ApiService;
import com.app.hungerhelp.models.User;
import com.app.hungerhelp.models.UserInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhone, etAddress;
    private ImageView ivEdit;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back button
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        ivEdit = findViewById(R.id.ivEdit);
        btnSave = findViewById(R.id.btnSave);

        loadUserProfile();

        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEditing(true);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileChanges();
            }
        });
    }

    private void loadUserProfile() {
        ApiService apiService = ApiClient.getRetrofitInstance(getApplicationContext()).create(ApiService.class);
        Call<User> call = apiService.getUserProfile();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        etName.setText(user.getFullName());
                        etEmail.setText(user.getEmail());
                        etPhone.setText(user.getPhoneNumber());
                        etAddress.setText(user.getAddress());
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enableEditing(boolean enable) {
        etName.setEnabled(enable);
        etEmail.setEnabled(enable);
        etPhone.setEnabled(enable);
        etAddress.setEnabled(enable);
        btnSave.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    private void saveProfileChanges() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        UserInfo updatedUser = new UserInfo();
        updatedUser.setName(name);
        updatedUser.setEmail(email);
        updatedUser.setPhone(phone);
        updatedUser.setAddress(address);

        ApiService apiService = ApiClient.getRetrofitInstance(getApplicationContext()).create(ApiService.class);
        Call<UserInfo> call = apiService.updateUserProfile(updatedUser);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    enableEditing(false);
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

