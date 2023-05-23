package com.atech.foodapp.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.atech.foodapp.data.model.RestaurantModel;
import com.atech.foodapp.data.model.RestaurantResponse;
import com.atech.foodapp.util.DataState;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantRepository {

    private final RestaurantApi restaurantApi;

    public RestaurantRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestaurantApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restaurantApi = retrofit.create(RestaurantApi.class);
    }

    public LiveData<DataState<List<RestaurantModel>>> getRestaurants() {
        MutableLiveData<DataState<List<RestaurantModel>>> data = new MutableLiveData<>();
        data.setValue(DataState.loading(null));
        restaurantApi.getRestaurants(RestaurantApi.defaultLocation.first, RestaurantApi.defaultLocation.second)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<RestaurantResponse> call, @NonNull Response<RestaurantResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            data.setValue(DataState.success(response.body().getData()));
                        } else {
                            data.setValue(DataState.error(response.message(), null));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RestaurantResponse> call, @NonNull Throwable t) {
                        data.setValue(DataState.error(t.getMessage(), null));
                    }
                });
        return data;
    }
}
