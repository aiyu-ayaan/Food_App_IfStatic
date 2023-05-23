package com.atech.foodapp.data.restaurant.model;

import androidx.recyclerview.widget.DiffUtil;

public class RestaurantModel {
    private String id;
    private String name;
    private String tags;
    private String rating;
    private String discount;
    private String primary_image;
    private String distance;

    public RestaurantModel(String id, String name, String tags, String rating, String discount, String primary_image, String distance) {
        this.id = id;
        this.name = name;
        this.tags = tags;
        this.rating = rating;
        this.discount = discount;
        this.primary_image = primary_image;
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTags() {
        return tags;
    }

    public String getRating() {
        return rating;
    }

    public String getDiscount() {
        return discount;
    }

    public String getPrimary_image() {
        return primary_image;
    }

    public String getDistance() {
        return distance;
    }

    public static DiffUtil.ItemCallback<RestaurantModel> itemCallback = new DiffUtil.ItemCallback<RestaurantModel>() {
        @Override
        public boolean areItemsTheSame(RestaurantModel oldItem, RestaurantModel newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @Override
        public boolean areContentsTheSame(RestaurantModel oldItem, RestaurantModel newItem) {
            return oldItem.id.equals(newItem.id) &&
                    oldItem.name.equals(newItem.name) &&
                    oldItem.tags.equals(newItem.tags) &&
                    oldItem.rating.equals(newItem.rating) &&
                    oldItem.discount.equals(newItem.discount) &&
                    oldItem.primary_image.equals(newItem.primary_image) &&
                    oldItem.distance.equals(newItem.distance);
        }
    };
}
