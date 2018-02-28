package com.example.iui.restaurantfinder.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.iui.restaurantfinder.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by IUI on 2/12/2018.
 */

public class HomePresent {
    private HomeInterface homeInterface;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    public HomePresent(HomeInterface homeInterface, FusedLocationProviderClient mFusedLocationProviderClient) {
        this.homeInterface = homeInterface;
        this.mFusedLocationProviderClient = mFusedLocationProviderClient;

    }

    public void checkInternetLocation(boolean isInternet, boolean isLocation) {
        Log.d("SSS", "checkInternetLocation: " + isInternet + "\n" + isLocation);
        if (isInternet) {
            if (isLocation) {

            } else {
                homeInterface.showDialogshowDialogLocation(R.string.location_is_off);
            }
        } else {
            homeInterface.showDialogInternet(R.string.internet_is_off);
        }
    }

}
