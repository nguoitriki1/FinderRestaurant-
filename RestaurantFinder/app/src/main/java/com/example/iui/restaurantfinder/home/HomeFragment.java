package com.example.iui.restaurantfinder.home;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.iui.restaurantfinder.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class HomeFragment extends Fragment implements View.OnClickListener, HomeInterface {
    private HomePresent homePresent;
    private LocationManager managerLocation;
    private ConnectivityManager managerInterNet;
    private boolean isInternet;
    private boolean isLocation;
    private ProgressBar mProgressBarLocation;
    private ImageView mBtnFindLocation;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private long mLatitude = 0;
    private long mLongtude = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        homePresent = new HomePresent(this, mFusedLocationProviderClient);
        managerLocation = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        managerInterNet = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmenthome_layout, container, false);
        mBtnFindLocation = view.findViewById(R.id.home_btn_get_location);
        mProgressBarLocation = view.findViewById(R.id.home_progess_location);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnFindLocation.setOnClickListener(this);
        checkInternet();
        checkLocation();
        homePresent.checkInternetLocation(isInternet, isLocation);
    }

    private boolean checkLocation() {
        isLocation = managerLocation.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isLocation;
    }

    private boolean checkInternet() {
        // Check for network connections
        if (managerInterNet.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                managerInterNet.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                managerInterNet.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                managerInterNet.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            // if connected with internet

            isInternet = true;

        } else if (
                managerInterNet.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        managerInterNet.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            isInternet = false;
        }
        return isInternet;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_btn_get_location:
                homePresent.checkInternetLocation(checkInternet(), checkLocation());
                mProgressBarLocation.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void showDialogInternet(int title) {
        showDiaglog(title, R.string.turn_on_internet, R.string.Cancel, Settings.ACTION_WIFI_SETTINGS);
    }

    @Override
    public void showDialogshowDialogLocation(int title) {
        showDiaglog(title, R.string.turn_on_location, R.string.Cancel, Settings.ACTION_LOCATION_SOURCE_SETTINGS);

    }

    public void showDiaglog(int title, int turnOn, int turnOff, final String action) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage(getResources().getString(title));
        dialog.setPositiveButton(getResources().getString(turnOn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent(action);
                startActivity(myIntent);
                //get gps
            }
        });
        dialog.setNegativeButton(getString(turnOff), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub

            }
        });
        dialog.show();
    }

    @Override
    public void toastMaketext(String title) {
        Toast.makeText(getActivity(), title, Toast.LENGTH_SHORT).show();
    }

    public void getLocation() {
        mLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()){
                    mLatitude = (long) location.getLatitude();
                    mLongtude = (long) location.getLongitude();
                    Log.d("Location", location.getLongitude() + "," + location.getLatitude());
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){

                }

            }
        });

        mFusedLocationProviderClient.getLastLocation().addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
