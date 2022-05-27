package com.sitadigi.go4lunch.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.internal.location.zzz;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.sitadigi.go4lunch.databinding.FragmentMapViewBinding;
import com.sitadigi.go4lunch.models.GoogleClass1;
import com.sitadigi.go4lunch.ui.mapView.MapViewViewModel;

import java.util.ArrayList;
import java.util.List;

public class MapViewUtils {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    Context mContext;
    @NonNull
    GoogleMap mGoogleMap;
    Activity mActivity;
    Location lastKnownLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    String location;

    boolean locationPermissionGranted;


    List<GoogleClass1.Result> resultList;
    LatLng defaultLocation = new LatLng(5,8);
    List<GoogleClass1.Result> listOfRestaurent = new ArrayList<>();
    MapViewViewModel mapViewViewModel;

    public MapViewUtils(Context context, GoogleMap googleMap, boolean locationPermissionGranted,
                        Activity activity) {
        mContext = context;
        mGoogleMap = googleMap;
        this.locationPermissionGranted = locationPermissionGranted;
        mActivity = activity;
    }

    boolean locationPermissionGranted;


    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(mContext.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void updateLocationUI(GoogleMap googleMap) {
        if (googleMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    private void getDeviceLocation(GoogleMap googleMap, Context context) {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */

        try {

            if (locationPermissionGranted) {
                if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    return;
                }

                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(/*(Executor) this*/mActivity, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            location = lastKnownLocation.getLatitude()+","+lastKnownLocation.getLongitude();

                            if (lastKnownLocation != null) {
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                // executeHttpRequestWithRetrofit();
                                //Call of viewModel
                                mapViewViewModel.loadRestaurentData(location);
                                mapViewViewModel.getRestaurent().observe(getViewLifecycleOwner(), RestaurentResponse -> {
                                    //listOfRestaurent.clear();
                                    listOfRestaurent.addAll(RestaurentResponse);

                                    // 2.1 - When getting response, we update UI
                                    if (listOfRestaurent!= null) {
                                        // this.updateUIWithListOfUsers(listOfRestaurent);
                                        for(GoogleClass1.Result restaurant : listOfRestaurent) {

                                            resultList.add(restaurant);
                                            LatLng restoPosition = new LatLng(restaurant.getGeometry().getLocation().getLat()
                                                    ,restaurant.getGeometry().getLocation().getLng());
                                            String restoNameForMarker = restaurant.getName();
                                            googleMap.addMarker(new MarkerOptions()
                                                    .position(restoPosition)
                                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                                    .title(restoNameForMarker));

                                        }

                                        int a = resultList.size();
                                        // LatLng villeurbanne = new LatLng(45.771944, 4.8901709);
                                        Log.e("TAG", "onResponse: " );

                                    }
                                });

                                int b = resultList.size();


                            }
                        } else {
                            Log.d("TAG", "Current location is null. Using defaults.");
                            Log.e("TAG", "Exception: %s", task.getException());

                            googleMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
}
