package com.atech.foodapp.util;

import android.annotation.SuppressLint;

public abstract class Utils {

    @SuppressLint("DefaultLocale")
    public static String convertToDistance(String distance) {
        var value = Double.parseDouble(distance);
        if (value < 999)
            return String.format("%.0f", value) + " m";
        else
            return String.format("%.0f", value / 1000) + " km";
    }
}
