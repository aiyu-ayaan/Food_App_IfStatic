package com.atech.foodapp.ui;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.atech.foodapp.data.restaurant.RestaurantRepository;
import com.atech.foodapp.data.restaurant.model.RestaurantModel;
import com.atech.foodapp.util.DataState;

import java.util.List;

public class MainViewModel extends ViewModel {

    private final RestaurantRepository restaurantRepository;

    public MainViewModel() {
        restaurantRepository = new RestaurantRepository();
    }

    public LiveData<DataState<List<RestaurantModel>>> getRestaurants(
            Pair<Double, Double> location
    ) {
        return restaurantRepository.getRestaurants(location);
    }
}
