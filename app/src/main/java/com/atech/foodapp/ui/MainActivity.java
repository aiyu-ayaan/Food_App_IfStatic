package com.atech.foodapp.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.atech.foodapp.databinding.ActivityMainBinding;
import com.atech.foodapp.ui.adapter.RestaurantAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
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

        setRecyclerView();
        observeData();
    }

    private void setRecyclerView() {
        adapter = new RestaurantAdapter();
        binding.include.recyclerView.setAdapter(adapter);
        binding.include.recyclerView.setHasFixedSize(true);
        binding.include.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void observeData() {
        viewModel.getRestaurants().observe(this, dataState -> {
            switch (dataState.getStatus()) {
                case SUCCESS -> {
                    Log.d(TAG, "onCreate: Success !!");
                    adapter.submitList(dataState.getData());
                }
                case ERROR -> Log.d(TAG, "onCreate: Error !!" + dataState.getMessage());
                case LOADING -> Log.d(TAG, "onCreate: Loading !!");
            }
        });
    }
}