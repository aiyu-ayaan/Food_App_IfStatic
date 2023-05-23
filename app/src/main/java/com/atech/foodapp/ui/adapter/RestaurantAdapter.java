package com.atech.foodapp.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.atech.foodapp.data.model.RestaurantModel;
import com.atech.foodapp.databinding.RowResBinding;
import com.atech.foodapp.util.Utils;
import com.bumptech.glide.Glide;

public class RestaurantAdapter extends ListAdapter<RestaurantModel, RestaurantAdapter.RestaurantViewHolder> {
    public RestaurantAdapter() {
        super(RestaurantModel.itemCallback);
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RestaurantViewHolder(RowResBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class RestaurantViewHolder extends RecyclerView.ViewHolder {
        private final RowResBinding binding;

        public RestaurantViewHolder(RowResBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(RestaurantModel model) {
            binding.textViewRating.setText(model.getRating());
            binding.textViewName.setText(model.getName());
            binding.textViewDiscount.setText(model.getDiscount() + "% FLAT OFF");
            binding.textViewDistance.setText(Utils.convertToDistance(model.getDistance()));
            Glide.with(binding.getRoot()).load(model.getPrimary_image()).into(binding.imageView);
        }
    }
}
