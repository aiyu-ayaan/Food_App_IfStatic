package com.atech.foodapp.data.model;

import java.util.List;

public class RestaurantResponse {
    private List<RestaurantModel> data;

    public RestaurantResponse(List<RestaurantModel> data) {
        this.data = data;
    }

    public List<RestaurantModel> getData() {
        return data;
    }
}
