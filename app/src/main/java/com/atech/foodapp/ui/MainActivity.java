package com.atech.foodapp.ui;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.atech.foodapp.util.PermissionUtil.LOCATION_PERMISSION_REQUEST_CODE;
import static com.atech.foodapp.util.PermissionUtil.isLocationServiceEnabled;
import static com.atech.foodapp.util.PermissionUtil.showOpenAppSettingsDialog;
import static com.atech.foodapp.util.PermissionUtil.showOpenLocationSettingsDialog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.atech.foodapp.data.location.LocationClient;
import com.atech.foodapp.databinding.ActivityMainBinding;
import com.atech.foodapp.ui.adapter.RestaurantAdapter;
import com.atech.foodapp.util.PermissionUtil;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Aiyu";
    private ActivityMainBinding binding;

    private MainViewModel viewModel;
    private RestaurantAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setBackground(null);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        requestPermission();
    }

    private void requestPermission() {
        setVisibility(false);
        if (!PermissionUtil.checkSelfPermission(this)) {
            PermissionUtil.requestLocationPermission(this);
        } else {
            if (isLocationServiceEnabled(this)) {
                fetchLocation();
            } else {
                showOpenLocationSettingsDialog(this);
            }
        }
    }

    private void fetchLocation() {
        LocationClient.getInstance(this).getCurrentLocation(locationModel -> {
            if (locationModel == null) {
                Log.d(TAG, "requestPermission:  Location is NULL !! ");
                return;
            }
            setCity(locationModel.getCity());
            setRecyclerView();
            observeData(locationModel.getCoordinates());
        });
    }

    private void setCity(String city) {
        binding.include.textViewLocation.setText(city);
    }

    private void setRecyclerView() {
        adapter = new RestaurantAdapter();
        binding.include.recyclerView.setAdapter(adapter);
        binding.include.recyclerView.setHasFixedSize(true);
        binding.include.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void observeData(Pair<Double, Double> location) {
        viewModel.getRestaurants(location).observe(this, dataState -> {
            switch (dataState.getStatus()) {
                case SUCCESS -> {
                    setVisibility(true);
                    Log.d(TAG, "onCreate: Success !!");
                    adapter.submitList(dataState.getData());
                }
                case ERROR -> Log.d(TAG, "onCreate: Error !!" + dataState.getMessage());
                case LOADING -> Log.d(TAG, "onCreate: Loading !!");
            }
        });
    }

    private void setVisibility(boolean isVisible) {
        binding.include.getRoot().setVisibility(isVisible ? VISIBLE : GONE);
        binding.bottomAppBar.setVisibility(isVisible ? VISIBLE : GONE);
        binding.fab.setVisibility(isVisible ? VISIBLE : GONE);
        binding.progressBar.setVisibility(isVisible ? GONE : VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                Toast.makeText(this, "Granted !!!", Toast.LENGTH_SHORT).show();
            } else
                showOpenAppSettingsDialog(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            fetchLocation();
        }
    }
}