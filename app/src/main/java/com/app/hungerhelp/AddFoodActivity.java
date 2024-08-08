package com.app.hungerhelp;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.hungerhelp.data.api.ApiClient;
import com.app.hungerhelp.data.api.ApiService;
import com.app.hungerhelp.models.AddDonationModel;
import com.app.hungerhelp.models.Category;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFoodActivity extends AppCompatActivity {

    private static final int PICK_IMAGES_REQUEST = 1;
    private EditText etFoodItem, etDescription, etQuantity, etLocation, etAvailableTill, etNotes;
    private TextView tvSelectedImages;
    private Spinner spinnerCategory;
    private Button btnSelectImages, btnSubmit;
    private List<Uri> selectedImages;
    private List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back button
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        etFoodItem = findViewById(R.id.etFoodItem);
        etDescription = findViewById(R.id.etDescription);
        etQuantity = findViewById(R.id.etQuantity);
        etLocation = findViewById(R.id.etLocation);
        etAvailableTill = findViewById(R.id.etAvailableTill);
        etNotes = findViewById(R.id.etNotes);
        tvSelectedImages = findViewById(R.id.tvSelectedImages);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSelectImages = findViewById(R.id.btnSelectImages);
        btnSubmit = findViewById(R.id.btnSubmit);

        selectedImages = new ArrayList<>();
        categories = new ArrayList<>();

        btnSelectImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDonation();
            }
        });

        loadCategories();
    }


    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_IMAGES_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    selectedImages.add(imageUri);
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                selectedImages.add(imageUri);
            }
            tvSelectedImages.setText(selectedImages.size() + " images selected");
        }
    }

    private void loadCategories() {
        ApiService apiService = ApiClient.getRetrofitInstance(getApplicationContext()).create(ApiService.class);
        Call<List<Category>> call = apiService.getCategories();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories = response.body();
                    ArrayAdapter<Category> adapter = new ArrayAdapter<>(AddFoodActivity.this, android.R.layout.simple_spinner_item, categories);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(adapter);
                } else {
                    Toast.makeText(AddFoodActivity.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(AddFoodActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitDonation() {
        String foodItem = etFoodItem.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String quantity = etQuantity.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String availableTill = etAvailableTill.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();
        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();

        if (foodItem.isEmpty() || description.isEmpty() || quantity.isEmpty() || location.isEmpty() || availableTill.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Date availableTillDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(availableTill);
            if (availableTillDate == null) {
                Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = ApiClient.getRetrofitInstance(getApplicationContext()).create(ApiService.class);

            // Create RequestBody instances for the form fields
            RequestBody foodItemPart = RequestBody.create(MediaType.parse("text/plain"), foodItem);
            RequestBody descriptionPart = RequestBody.create(MediaType.parse("text/plain"), description);
            RequestBody quantityPart = RequestBody.create(MediaType.parse("text/plain"), quantity);
            RequestBody locationPart = RequestBody.create(MediaType.parse("text/plain"), location);
            RequestBody availableTillPart = RequestBody.create(MediaType.parse("text/plain"), availableTill);
            RequestBody notesPart = RequestBody.create(MediaType.parse("text/plain"), notes);
            RequestBody categoryPart = RequestBody.create(MediaType.parse("text/plain"), selectedCategory.getId());

            // Create MultipartBody.Part instances for the images
            List<MultipartBody.Part> imageParts = new ArrayList<>();
            for (Uri imageUri : selectedImages) {
                String filePath = getRealPathFromURI(imageUri);
                if (filePath != null) {
                    File imageFile = new File(filePath);
                    RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
                    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("images", imageFile.getName(), imageBody);
                    imageParts.add(imagePart);
                }
            }

            Call<AddDonationModel> call = apiService.createDonation(foodItemPart, descriptionPart, quantityPart, locationPart, availableTillPart, notesPart, categoryPart, imageParts);
            call.enqueue(new Callback<AddDonationModel>() {
                @Override
                public void onResponse(Call<AddDonationModel> call, Response<AddDonationModel> response) {

                    Log.d("Failed", response.body().toString());

                    if (response.isSuccessful() && response.body() != null) {
                        AddDonationModel donation = response.body();
                        // Use donation object safely
                        Toast.makeText(AddFoodActivity.this, "Donation submitted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddFoodActivity.this, "Failed to submit donation: " + (response.errorBody() != null ? response.errorBody().toString() : "Unknown error"), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AddDonationModel> call, Throwable t) {
                    Log.e("AddFoodActivity", "onFailure: " + t.getMessage());
                    Toast.makeText(AddFoodActivity.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return null;
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
