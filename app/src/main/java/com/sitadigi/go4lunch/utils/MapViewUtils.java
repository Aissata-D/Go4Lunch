package com.sitadigi.go4lunch.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sitadigi.go4lunch.models.GoogleClass1;
import com.sitadigi.go4lunch.ui.mapView.MapViewViewModel;
import com.sitadigi.go4lunch.repository.GeoLocateRepository;

import java.util.ArrayList;
import java.util.List;

public class MapViewUtils {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    public List<GoogleClass1.Result> resultList = new ArrayList<>();
    Context mContext;
    Activity mActivity;
    Location lastKnownLocation;
    boolean locationPermissionGranted;
    LatLng defaultLocation = new LatLng(5, 8);
    List<GoogleClass1.Result> listOfRestaurent = new ArrayList<>();
    MapViewViewModel mapViewViewModel;
    GeoLocateRepository mGeoLocateRepository;

    public MapViewUtils(MapViewViewModel mapViewViewModel, Context context, boolean locationPermissionGranted,
                        Activity activity) {
        this.mapViewViewModel = mapViewViewModel;
        mContext = context;
        this.locationPermissionGranted = locationPermissionGranted;
        mActivity = activity;
        mGeoLocateRepository = new GeoLocateRepository();
    }

    public List<GoogleClass1.Result> getNearRestaurantList() {
        return resultList;
    }

    public void getLocationPermission() {
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

    public void updateLocationUI(GoogleMap mGoogleMap) {
        if (mGoogleMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mGoogleMap.setMyLocationEnabled(false);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void GeolocationOfDeviceAndUpdateGoogleMapView(GoogleMap mGoogleMap, LifecycleOwner mLifecycleOwner, ImageView placeHolder) {

        //moveCamera
        if (mapViewViewModel.getLocationMutableLiveData() != null) {
            mapViewViewModel.getLocationMutableLiveData()
                    .observe(mLifecycleOwner,
                            LocationResponse -> {
                                lastKnownLocation = LocationResponse;
                                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            });
            if (mapViewViewModel.getRestaurent() != null) {

                // Call list of restaurant
                mapViewViewModel.getRestaurent().observe(mLifecycleOwner, RestaurentResponse -> {
                    listOfRestaurent.clear();
                    listOfRestaurent.addAll(RestaurentResponse);

                    //  When getting response, we update UI
                    if (listOfRestaurent != null) {
                        for (GoogleClass1.Result restaurant : listOfRestaurent) {
                            if (!resultList.contains(restaurant)) {
                                resultList.add(restaurant);
                                LatLng restoPosition = new LatLng(restaurant.getGeometry().getLocation().getLat()
                                        , restaurant.getGeometry().getLocation().getLng());
                                String restoNameForMarker = restaurant.getName();
                                mGoogleMap.addMarker(new MarkerOptions()
                                        .position(restoPosition)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                        .title(restoNameForMarker));
                            }
                        }
                    }
                });
            }
        } else {
            mGoogleMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }
}
