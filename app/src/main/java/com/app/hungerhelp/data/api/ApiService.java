package com.app.hungerhelp.data.api;

import com.app.hungerhelp.models.AddDonationModel;
import com.app.hungerhelp.models.Category;
import com.app.hungerhelp.models.Food;
import com.app.hungerhelp.models.FoodDonateItem;
import com.app.hungerhelp.models.FoodRequestModel;
import com.app.hungerhelp.models.LoginRequest;
import com.app.hungerhelp.models.LoginResponse;
import com.app.hungerhelp.models.User;
import com.app.hungerhelp.models.UserInfo;
import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    @POST("auth/register")
    Call<User> registerUser(@Body User user);

    @POST("auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @GET("category")
    Call<List<Category>> getCategories();

    @Multipart
    @POST("food/add-food")
    Call<AddDonationModel> createDonation(
            @Part("foodItem") RequestBody foodItem,
            @Part("description") RequestBody description,
            @Part("quantity") RequestBody quantity,
            @Part("location") RequestBody location,
            @Part("availableTill") RequestBody availableTill,
            @Part("notes") RequestBody notes,
            @Part("category") RequestBody category,
            @Part List<MultipartBody.Part> images
    );

    @GET("user/account")
    Call<User> getUserProfile();

    @PUT("/user/update")
    Call<UserInfo> updateUserProfile(@Body UserInfo user);

    @GET("food")
    Call<List<Food>> getFoodItems();

    @POST("food/request/{id}")
    Call<ResponseBody> requestFood(@Path("id") String id);

    @GET("food/mydonatedfood")
    Call<List<FoodDonateItem>> getDonatedFoods();

    @GET("food/requests")
    Call<List<FoodRequestModel>> getFoodRequests();

    @PATCH("food/update-requests/{id}")
    Call<ResponseBody> updateRequestStatus(@Path("id") String id, @Body RequestBody requestStatus);

    @DELETE("/food/{id}")
    Call<ResponseBody> deleteFoodItem(@Path("id") String foodId);
}