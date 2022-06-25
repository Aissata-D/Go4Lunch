package com.sitadigi.go4lunch.repository;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.sitadigi.go4lunch.viewModel.MainViewViewModel;

public class GeoLocateRepository {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234;
    public boolean locationPermissionGranted;
    public Location lastKnownLocation = new Location("");
    public FusedLocationProviderClient fusedLocationProviderClient;
    public MutableLiveData<Location> locationMutableLiveData;
    String location = "45.76667,46.76667";

    public GeoLocateRepository() {
        locationMutableLiveData = new MutableLiveData<>();
    }

    public void getLocationPermission(Context context, Activity activity) {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public MutableLiveData<Location> getDeviceLocation(Context context, Activity activity, MainViewViewModel mainViewViewModel) {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        lastKnownLocation.setLatitude(45.76667);
        lastKnownLocation.setLongitude(46.76667);
        getLocationPermission(context, activity);
        try {

            if (locationPermissionGranted) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);

                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(/*(Executor) this*/activity, new OnCompleteListener<Location>() {
                    //private MainViewViewModel mainViewViewModel;

                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                           // if(task.getResult() != null) {
                            lastKnownLocation = task.getResult();

                            try {
                                Thread.sleep(4500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                           // }
                                location = lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude();
                                locationMutableLiveData.setValue(lastKnownLocation);
                                //moveCamera();
                                mainViewViewModel.loadRestaurantData(location);
                        } else {
                            Log.e("TAG", "Exception: %s MainActivity", task.getException());
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
        return locationMutableLiveData;
    }

}
