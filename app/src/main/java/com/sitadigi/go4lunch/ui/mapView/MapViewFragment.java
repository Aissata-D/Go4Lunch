package com.sitadigi.go4lunch.ui.mapView;

import static com.sitadigi.go4lunch.DetailActivity.RESTO_ID;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_NAME;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_OPENINGHOURS;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_PHOTO_URL;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.sitadigi.go4lunch.DetailActivity;
import com.sitadigi.go4lunch.MainActivity;
import com.sitadigi.go4lunch.R;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.SupportMapFragment;
import com.sitadigi.go4lunch.databinding.FragmentMapViewBinding;
import com.sitadigi.go4lunch.models.GoogleClass1;
import com.sitadigi.go4lunch.models.googleMapApi.Result;
import com.sitadigi.go4lunch.utils.GoogleMapApiCalls;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;


public class MapViewFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMapApiCalls.Callbacks{

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private FragmentMapViewBinding binding;
    GoogleMapOptions mGoogleMapOptions;
    public static Location mLocation;
    boolean locationPermissionGranted;

    String restoId;
    @NonNull
   GoogleMap googleMap;
    String location;
    List<GoogleClass1.Result> resultList;

    Location lastKnownLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    LatLng defaultLocation = new LatLng(5,8);
    private String markerId;
    private String markerName;
    private LatLng markerPosition;
    //List<GoogleClass1.Result> listOfRestaurent = new ArrayList<>();
    List<GoogleClass1.Result> listOfRestaurent = new ArrayList<>();
    MapViewViewModel mapViewViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         mapViewViewModel = new ViewModelProvider(this).get(MapViewViewModel.class);

        binding = FragmentMapViewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //final TextView textView = binding.textMapView;
       // mapViewViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        resultList = new ArrayList<>();
        googleMapView();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void googleMapView(){

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_view_fragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        getLocationPermission();


// Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        googleMap.setOnMarkerClickListener(this);

    }


    // ------------------------------
    //  HTTP REQUEST (Retrofit Way)
    // ------------------------------

    // 4 - Execute HTTP request and update UI
    private void executeHttpRequestWithRetrofit(){
        this.updateUIWhenStartingHTTPRequest();
        GoogleMapApiCalls.fetchResultFollowing(this, location,1500,
                "restaurant","AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08");
        //"45.771944%2C4.8901709"
    }

    // 2 - Override callback methods

    @Override
    public void onResponse(@Nullable GoogleClass1 results) {
        // 2.1 - When getting response, we update UI
        if (results != null) {
            //this.updateUIWithListOfUsers(results);
            for(GoogleClass1.Result restaurant : results.getResults()) {

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

        int b = resultList.size();
        googleMap.setOnMarkerClickListener(this);

    }

    @Override
    public void onFailure() {

        Log.e("TAG", "onFailure: " );
        // 2.2 - When getting error, we update UI
        this.updateUIWhenStopingHTTPRequest("An error happened !");
    }



    // ------------------
    //  UPDATE UI
    // ------------------



    // 3 - Update UI showing only name of users
    private void updateUIWithListOfUsers(){
        StringBuilder stringBuilder = new StringBuilder();
        //GoogleClass1 result;
        // stringBuilder.append("-"+ results.getResults().get(0).getName() +"\n");
        //  for (GoogleClass1 result : results.getResults()){
        for(int i = 0; i < listOfRestaurent.size(); i++) {
        // resultList.add(results.getResults().get(i));
            stringBuilder.append("-" + listOfRestaurent.get(i).getName() + "\n"
                    +"45.771944,4.8901709"+"\n"
                    /*+ "currentLocation; " +currentLocation +"\n"*/);
        }
        //}
        updateUIWhenStopingHTTPRequest(stringBuilder.toString());
    }
    private void updateUIWhenStartingHTTPRequest(){
        //this.textView.setText("Downloading...");
    }

    private void updateUIWhenStopingHTTPRequest(String response){
        //this.textView.setText(response);
    }


    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
    }
    private void updateLocationUI() {
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
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */

        try {
            if (locationPermissionGranted) {
                if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    return;
                }
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(/*(Executor) this*/getActivity(), new OnCompleteListener<Location>() {
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

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        markerId = marker.getId();
        markerName = marker.getTitle();
        markerPosition = marker.getPosition();
        LatLng restoPosition;

        for(GoogleClass1.Result restaurant : resultList){
            restoPosition = new LatLng(restaurant.getGeometry().getLocation().getLat(),
                    restaurant.getGeometry().getLocation().getLng());
            String restoName = restaurant.getName();
            if((restoName.equals(markerName)) && (restoPosition.equals( markerPosition))){
                Intent intentDetail = new Intent(this.getActivity(), DetailActivity.class);
                intentDetail.putExtra(RESTO_ID,restaurant.getPlaceId());
                intentDetail.putExtra(RESTO_NAME,restoName);
                intentDetail.putExtra(RESTO_OPENINGHOURS, restaurant.getOpeningHours().getOpenNow());
                if(restaurant.getPhotos() != null/*  .size() >= 1*/) {
                    if (restaurant.getPhotos().get(0).getPhotoReference() != null) {
                        intentDetail.putExtra(RESTO_PHOTO_URL, restaurant.getPhotos().get(0).getPhotoReference());
                    }
                }
                startActivity(intentDetail);

                Log.e("TAG", "onMarkerClick:" +
                        " restoid: "+restaurant.getPlaceId() +"resto name "+restaurant.getName());
                Toast.makeText(this.getActivity().getApplicationContext(),
                        "resto Name : "+restoName +
                                "  resto Id : "+restaurant.getPlaceId(), Toast.LENGTH_SHORT).show();

            }


        }
        //TODO Boucler sur resultList et si : marker.getTitle == resultList.getName &&
       //TODO  marker.getLat == resultList.getLat && marker.getLng == resultList.getLng


        return true;
    }
}