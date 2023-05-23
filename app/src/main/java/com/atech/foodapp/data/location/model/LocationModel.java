package com.atech.foodapp.data.location.model;

import android.util.Pair;

public class LocationModel {
    private String city;
    private double latitude;
    private double longitude;

    public LocationModel(String city, double latitude, double longitude) {
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public Pair<Double, Double> getCoordinates() {
        return new Pair<>(latitude, longitude);
    }

    @Override
    public String toString() {
        return "LocationModel{" +
                "city='" + city + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
