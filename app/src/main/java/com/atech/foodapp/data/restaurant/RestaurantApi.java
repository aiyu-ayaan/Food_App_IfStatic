package com.atech.foodapp.data.restaurant;

import android.util.Pair;

import com.atech.foodapp.data.restaurant.model.RestaurantResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestaurantApi {
    String BASE_URL = "https://theoptimiz.com/restro/public/api/";

    Pair<String, String> defaultLocation = new Pair<>("23.344101", "85.309563");

    @POST("get_resturants")
    Call<RestaurantResponse> getRestaurants(
            @Query("lat") String lat,
            @Query("lng") String lng
    );
}
