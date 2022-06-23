package com.sitadigi.go4lunch.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sitadigi.go4lunch.R;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.viewModel.MainViewViewModel;
import com.sitadigi.go4lunch.repository.GeoLocateRepository;

import java.util.ArrayList;
import java.util.List;

public class UtilsMapView {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    public List<GoogleMapApiClass.Result> resultList = new ArrayList<>();
    Context mContext;
    Activity mActivity;
    Location lastKnownLocation;
    boolean locationPermissionGranted;
    LatLng defaultLocation = new LatLng(5, 8);
    List<GoogleMapApiClass.Result> listOfRestaurant = new ArrayList<>();
    MainViewViewModel mMainViewViewModel;
    GeoLocateRepository mGeoLocateRepository;

    public UtilsMapView(MainViewViewModel mainViewViewModel, Context context, boolean locationPermissionGranted,
                        Activity activity) {
        this.mMainViewViewModel = mainViewViewModel;
        mContext = context;
        this.locationPermissionGranted = locationPermissionGranted;
        mActivity = activity;
        mGeoLocateRepository = new GeoLocateRepository();
    }

    public List<GoogleMapApiClass.Result> getNearRestaurantList() {
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

    public void GeolocationOfDeviceAndUpdateGoogleMapView(GoogleMap mGoogleMap
            , LifecycleOwner mLifecycleOwner, List<User> mUsers) {

        // create icon
        int height = 120;
        int width = 120;
        // Icon Green
        BitmapDrawable bitMapMarkerGreen=(BitmapDrawable) mContext.getResources().getDrawable(R.drawable.marker_restaurant_green);
        Bitmap b1 = bitMapMarkerGreen.getBitmap();
        Bitmap iconRestaurantGreen = Bitmap.createScaledBitmap(b1, width, height, false);
        //Icon Orange
        BitmapDrawable bitMapMarkerOrange=(BitmapDrawable) mContext.getResources().getDrawable(R.drawable.marker_restaurant_orange);
        Bitmap b2 = bitMapMarkerOrange.getBitmap();
        Bitmap iconRestaurantOrange = Bitmap.createScaledBitmap(b2, width, height, false);
        //moveCamera
        if (mMainViewViewModel.getLocationMutableLiveData() != null) {
            mMainViewViewModel.getLocationMutableLiveData()
                    .observe(mLifecycleOwner,
                            LocationResponse -> {
                                lastKnownLocation = LocationResponse;
                                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            });
            if (mMainViewViewModel.getRestaurant() != null) {

                // Call list of restaurant
                mMainViewViewModel.getRestaurant().observe(mLifecycleOwner, RestaurantResponse -> {
                    listOfRestaurant.clear();
                    listOfRestaurant.addAll(RestaurantResponse);

                    //  When getting response, we update UI
                    if (listOfRestaurant != null) {
                        for (GoogleMapApiClass.Result restaurant : listOfRestaurant) {
                            if (!resultList.contains(restaurant)) {
                                resultList.add(restaurant);
                                LatLng restoPosition = new LatLng(restaurant.getGeometry().getLocation().getLat()
                                        , restaurant.getGeometry().getLocation().getLng());
                                String restoNameForMarker = restaurant.getName();
                                if(mMainViewViewModel.isRestaurantSelectedByOneWorkmate(restaurant.getPlaceId()
                                        ,mUsers)) {
                                    mGoogleMap.addMarker(new MarkerOptions()
                                            .position(restoPosition)
                                            .icon(BitmapDescriptorFactory.fromBitmap(iconRestaurantGreen))
                                            .title(restoNameForMarker));
                                }else {
                                    mGoogleMap.addMarker(new MarkerOptions()
                                            .position(restoPosition)
                                            .icon(BitmapDescriptorFactory.fromBitmap(iconRestaurantOrange))
                                            .title(restoNameForMarker));
                                }
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
