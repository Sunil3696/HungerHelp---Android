package com.app.hungerhelp.data.api;

import android.content.Context;
import com.app.hungerhelp.data.api.storage.SharedPrefManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://192.168.2.196:3000/";
    public static final String IMAGE_URL = "http://192.168.2.196:3000/";
    private static Retrofit retrofit;
    private static OkHttpClient client;

    public static Retrofit getRetrofitInstance(Context context) {
        if (retrofit == null) {
            client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(context);
                            String token = sharedPrefManager.getToken();
                            Request.Builder requestBuilder = chain.request().newBuilder();
                            if (token != null) {
                                requestBuilder.addHeader("Authorization", "Bearer " + token);
                            }
                            Request newRequest = requestBuilder.build();
                            return chain.proceed(newRequest);
                        }
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}