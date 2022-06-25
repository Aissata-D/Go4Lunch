package com.sitadigi.go4lunch.ui.mapView;

import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_ID;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_NAME;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_OPENINGHOURS;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_PHONE_NUMBER;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_PHOTO_URL;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_RATING;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_TYPE;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_ADDRESS;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_WEBSITE;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sitadigi.go4lunch.DetailActivity;
import com.sitadigi.go4lunch.R;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.SupportMapFragment;
import com.sitadigi.go4lunch.databinding.FragmentMapViewBinding;
import com.sitadigi.go4lunch.factory.MainViewModelFactory;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.repository.GoogleMapApiCallsRepository;
import com.sitadigi.go4lunch.utils.UtilsMapView;
import com.sitadigi.go4lunch.viewModel.MainViewViewModel;

import java.util.ArrayList;
import java.util.List;

public class MapViewFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234;
    boolean locationPermissionGranted;
    private GoogleMap googleMap;
    private List<GoogleMapApiClass.Result> resultList;
    private MainViewViewModel mMainViewViewModel;
    private UtilsMapView mUtilsMapView;
    private FragmentMapViewBinding binding;
    private LatLng latLngPlaceSelected;
    private ImageView mapViewPlaceHolder;
    private List<User> mUsers = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GoogleMapApiCallsRepository googleMapApiCallsRepository = new GoogleMapApiCallsRepository();

        mMainViewViewModel = new ViewModelProvider(requireActivity()
                , new MainViewModelFactory(googleMapApiCallsRepository)).get(MainViewViewModel.class);
        binding = FragmentMapViewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mapViewPlaceHolder = binding.mapViewPlaceholder;
        resultList = new ArrayList<>();
        googleMapView();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void googleMapView() {
        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_view_fragment);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        mUtilsMapView = new UtilsMapView(this.mMainViewViewModel, this.getContext(),
                this.locationPermissionGranted, this.getActivity());

        mUtilsMapView.getLocationPermission();
        // Turn on the My Location layer and the related control on the map.
        mUtilsMapView.updateLocationUI(this.googleMap);
        // Get the current location of the device and set the position of the map.
        mMainViewViewModel.getAllUser().observe(getViewLifecycleOwner(), AllUsers -> {
            mUsers.clear();
            mUsers = AllUsers;
            mUtilsMapView.GeolocationOfDeviceAndUpdateGoogleMapView(this.googleMap
                    , this.getViewLifecycleOwner(), mUsers);

        });

        googleMap.setOnMarkerClickListener(this);
        mMainViewViewModel.getResultSearchLatLng().observe(getViewLifecycleOwner(), LatLngPlaceResponse -> {
            latLngPlaceSelected = LatLngPlaceResponse;
            if (latLngPlaceSelected != null) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        latLngPlaceSelected, 20f));
                googleMap.addMarker(new MarkerOptions()
                        .position(latLngPlaceSelected)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                );
            } else {
                mMainViewViewModel.getAllUser().observe(getViewLifecycleOwner(), AllUsers -> {
                    mUsers.clear();
                    mUsers = AllUsers;
                    mUtilsMapView.GeolocationOfDeviceAndUpdateGoogleMapView(this.googleMap
                            , this.getViewLifecycleOwner(), mUsers);
                });
            }
        });
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        resultList = mUtilsMapView.getNearRestaurantList();
        String markerName = marker.getTitle();
        LatLng markerPosition = marker.getPosition();
        for (GoogleMapApiClass.Result restaurant : resultList) {

            LatLng restaurantPosition = new LatLng(restaurant.getGeometry().getLocation().getLat(),
                    restaurant.getGeometry().getLocation().getLng());
            String restaurantName = restaurant.getName();
            if ((restaurantName.equals(markerName)) && (restaurantPosition.equals(markerPosition))) {
                Intent intentDetail = new Intent(this.getActivity(), DetailActivity.class);
                intentDetail.putExtra(RESTAURANT_ID, restaurant.getPlaceId());
                intentDetail.putExtra(RESTAURANT_NAME, restaurantName);
                if ((restaurant.getOpeningHours() != null) && (restaurant.getOpeningHours().getOpenNow()) != null) {
                    intentDetail.putExtra(RESTAURANT_OPENINGHOURS, restaurant.getOpeningHours().getOpenNow());
                }
                if (restaurant.getPhotos() != null) {
                    if (restaurant.getPhotos().get(0).getPhotoReference() != null) {
                        intentDetail.putExtra(RESTAURANT_PHOTO_URL, restaurant.getPhotos().get(0).getPhotoReference());
                    }
                }
                if ((restaurant.getTypes()) != null && (restaurant.getTypes().get(0)) != null) {
                    String restaurantAddresses = restaurant.getVicinity();
                    intentDetail.putExtra(RESTAURANT_ADDRESS, restaurantAddresses);
                    String restaurantType = restaurant.getTypes().get(0);
                    intentDetail.putExtra(RESTAURANT_TYPE, restaurantType);
                }
                if (restaurant.getRating() != null) {
                    float rating = restaurant.getRating().floatValue();
                    intentDetail.putExtra(RESTAURANT_RATING, rating);
                }

                List<String> restaurantNumberAndWebSite = mMainViewViewModel
                        .loadRestaurantPhoneNumberAndWebSite(restaurant);
                if (restaurantNumberAndWebSite.size() >= 1) {
                    if (restaurantNumberAndWebSite.get(0) != null) {
                        String restaurantPhoneNumber = restaurantNumberAndWebSite.get(0);
                        intentDetail.putExtra(RESTAURANT_PHONE_NUMBER, restaurantPhoneNumber);
                    }
                    if (restaurantNumberAndWebSite.size() >= 2) {
                        if (restaurantNumberAndWebSite.get(1) != null) {
                            String restaurantWebSite = restaurantNumberAndWebSite.get(1);
                            intentDetail.putExtra(RESTAURANT_WEBSITE, restaurantWebSite);
                        }
                    } else {
                        Log.e("DETAIL", "onMarkerClick: phone size<2 ");
                    }
                } else {
                    Log.e("DETAIL", "onMarkerClick: website size<1 ");

                }
                intentDetail.putExtra(RESTAURANT_ID, restaurant.getPlaceId());
                startActivity(intentDetail);
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
        mUtilsMapView.updateLocationUI(this.googleMap);
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}