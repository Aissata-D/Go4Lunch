package com.sitadigi.go4lunch.ui.mapView;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sitadigi.go4lunch.R;
import com.sitadigi.go4lunch.databinding.FragmentMapViewBinding;
import com.sitadigi.go4lunch.factory.MainViewModelFactory;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.repository.GoogleMapApiCallsRepository;
import com.sitadigi.go4lunch.utils.OpenDetailActivityUtils;
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
                OpenDetailActivityUtils openDetailActivityUtils = new OpenDetailActivityUtils();
                openDetailActivityUtils.clickOnOpenDetailActivityInMapviewFragment(restaurant,
                        getContext(), mMainViewViewModel, restaurantName);
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