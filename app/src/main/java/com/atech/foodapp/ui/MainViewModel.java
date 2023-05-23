package com.atech.foodapp.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.atech.foodapp.data.RestaurantRepository;
import com.atech.foodapp.data.model.RestaurantModel;
import com.atech.foodapp.util.DataState;

import java.util.List;

public class MainViewModel extends ViewModel {

    private final LiveData<DataState<List<RestaurantModel>>> restaurants;

    public MainViewModel() {
        RestaurantRepository restaurantRepository = new RestaurantRepository();
        restaurants = restaurantRepository.getRestaurants();
    }

    public LiveData<DataState<List<RestaurantModel>>> getRestaurants() {
        return restaurants;
    }
}
