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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.atech.foodapp.R;
import com.atech.foodapp.data.location.LocationClient;
import com.atech.foodapp.data.restaurant.model.RestaurantModel;
import com.atech.foodapp.databinding.ActivityMainBinding;
import com.atech.foodapp.ui.adapter.RestaurantAdapter;
import com.atech.foodapp.util.PermissionUtil;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Aiyu";
    private ActivityMainBinding binding;

    private MainViewModel viewModel;
    private RestaurantAdapter adapter;

    private MutableLiveData<String> query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setBackground(null);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        requestPermission();
        setSearchViewAndChip();
    }

    /**
     * set search view and chip group
     * when chip is selected then set query value
     * when search view text is changed then set query value
     */
    private void setSearchViewAndChip() {
        query = new MutableLiveData<>();
        binding.include.chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            binding.include.editTextSearch.setText("");
            checkedIds.forEach(e -> {
                if (e == R.id.chipAll)
                    query.setValue("");
                else if (e == R.id.chipPizza)
                    query.setValue("pizza");
                else if (e == R.id.chipBurger)
                    query.setValue("burger");
                else if (e == R.id.chipChicken)
                    query.setValue("chicken");
                else if (e == R.id.chipSalad)
                    query.setValue("salad");
            });
        });
    }

    /**
     * request permission for location
     * if permission granted then fetch location
     * else request permission again
     * if permission denied then show dialog to open app settings
     * if permission denied permanently then show dialog to open location settings
     */
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

    /**
     * Fetch location and set city name
     * set recycler view
     * observe data
     *
     * @see LocationClient
     */

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

    /**
     * set visibility of views
     *
     * @param location pair of latitude and longitude
     */

    private void observeData(Pair<Double, Double> location) {
        viewModel.getRestaurants(location).observe(this, dataState -> {
            switch (dataState.getStatus()) {
                case SUCCESS -> {
                    setVisibility(true);
                    query.setValue("");
                    filterList(dataState.getData());
                }
                case ERROR -> Log.d(TAG, "onCreate: Error !!" + dataState.getMessage());
                case LOADING -> Log.d(TAG, "onCreate: Loading !!");
            }
        });
    }

    /**
     * filter list according to query
     *
     * @param data list of restaurant
     */
    private void filterList(List<RestaurantModel> data) {
        query.observe(this, query -> {
            List<RestaurantModel> filter;
            if (query.isEmpty()) {
                filter = data;
            } else {
                filter = data.stream().filter(e ->
                        e.getTags().toLowerCase(Locale.getDefault()).contains(query.toLowerCase()) ||
                                e.getName().toLowerCase(Locale.getDefault()).contains(query.toLowerCase())
                ).collect(Collectors.toList());
            }
            setRecyclerViewVisibility(filter);
            adapter.submitList(filter);
        });

    }

//    __________________________________________ Utils __________________________________________

    private void setRecyclerViewVisibility(List<RestaurantModel> filter) {
        if (filter.isEmpty()) {
            binding.include.linearLayoutNoData.setVisibility(VISIBLE);
            binding.include.recyclerView.setVisibility(GONE);
        } else {
            binding.include.linearLayoutNoData.setVisibility(GONE);
            binding.include.recyclerView.setVisibility(VISIBLE);
        }
    }


    private void setVisibility(boolean isVisible) {
        binding.include.getRoot().setVisibility(isVisible ? VISIBLE : GONE);
        binding.bottomAppBar.setVisibility(isVisible ? VISIBLE : GONE);
        binding.fab.setVisibility(isVisible ? VISIBLE : GONE);
        binding.progressBar.setVisibility(isVisible ? GONE : VISIBLE);
    }

//    __________________________________________ Permission __________________________________________

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                Toast.makeText(this, "Granted !!!", Toast.LENGTH_SHORT).show();
                fetchLocation();
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