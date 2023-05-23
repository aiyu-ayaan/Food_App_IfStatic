package com.atech.foodapp.data.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.atech.foodapp.data.location.model.LocationModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class LocationClient {
    private final FusedLocationProviderClient fusedLocationClient;
    private static LocationClient instance;
    private final WeakReference<Activity> activityRef;

    private LocationClient(Activity activity) {
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        this.activityRef = new WeakReference<>(activity);
    }

    public static LocationClient getInstance(Activity activity) {
        if (instance == null) {
            instance = new LocationClient(activity);
        }
        return instance;
    }

    private Activity getActivity() {
        return activityRef.get();
    }

    /**
     * Get current location of the user
     * suppressing missing permission because we already check for permission in PermissionUtil
     */
    @SuppressLint("MissingPermission")
    public void getCurrentLocation(Consumer<LocationModel> consumer) {
        Log.d("MainActivity", "getCurrentLocation: " + fusedLocationClient);
        fusedLocationClient.getCurrentLocation(
                100,
                null
        ).addOnSuccessListener(getActivity(), location -> {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                String cityName = "";
                try {
                    List<android.location.Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addresses != null && addresses.size() > 0) {
                        Address address = addresses.get(0);
                        String addressLine = address.getAddressLine(0); // Full address
                        cityName = address.getLocality();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                var model = new LocationModel(cityName, latitude, longitude);
                consumer.accept(model);
            } else {
                consumer.accept(null);
            }
        });
    }
}
