package com.atech.foodapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public abstract class PermissionUtil {
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public enum Permissions {
        COARSE_LOCATION(android.Manifest.permission.ACCESS_COARSE_LOCATION),
        FINE_LOCATION(android.Manifest.permission.ACCESS_FINE_LOCATION);

        final String permission;

        Permissions(String permission) {
            this.permission = permission;
        }
    }

    public static Boolean checkSelfPermission(Activity activity) {
        int coarseLocationPermission = ActivityCompat.checkSelfPermission(activity, Permissions.COARSE_LOCATION.permission);
        int fineLocationPermission = ActivityCompat.checkSelfPermission(activity, Permissions.FINE_LOCATION.permission);
        return coarseLocationPermission == PackageManager.PERMISSION_GRANTED &&
                fineLocationPermission == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestLocationPermission(Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{
                        Permissions.COARSE_LOCATION.permission,
                        Permissions.FINE_LOCATION.permission
                },
                LOCATION_PERMISSION_REQUEST_CODE
        );
    }

    public static boolean isLocationServiceEnabled(Activity activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            return gpsEnabled || networkEnabled;
        }
        return false;
    }

    public static void showOpenAppSettingsDialog(Activity activity) {
        new MaterialAlertDialogBuilder(activity)
                .setTitle("Location Permission")
                .setMessage("Please enable location permission to continue")
                .setPositiveButton("Open Settings", (dialog, which) -> openAppSettings(activity))
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                    activity.finish();
                })
                .show();
    }

    private static void openAppSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }


    public static void showOpenLocationSettingsDialog(Activity activity) {
        new MaterialAlertDialogBuilder(activity)
                .setTitle("Location Service")
                .setMessage("Please enable location service to continue")
                .setPositiveButton("Open Settings", (dialog, which) -> openLocationSettings(activity))
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                    activity.finish();
                })
                .show();
    }

    private static void openLocationSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivityForResult(intent, LOCATION_PERMISSION_REQUEST_CODE);
    }
}
