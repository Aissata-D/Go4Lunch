package com.sitadigi.go4lunch.ui.mapView;

import static com.sitadigi.go4lunch.DetailActivity.RESTO_ID;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_NAME;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_OPENINGHOURS;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_PHOTO_URL;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_TYPE_ADRESSES;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.sitadigi.go4lunch.DetailActivity;
import com.sitadigi.go4lunch.R;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.maps.SupportMapFragment;
import com.sitadigi.go4lunch.databinding.FragmentMapViewBinding;
import com.sitadigi.go4lunch.models.GoogleClass1;
import com.sitadigi.go4lunch.utils.MapViewUtils;
import com.sitadigi.go4lunch.repository.GeoLocateRepository;

import java.util.ArrayList;
import java.util.List;

public class MapViewFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener  {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234;
    boolean locationPermissionGranted;
    GoogleMap googleMap;
    List<GoogleClass1.Result> resultList;
    FusedLocationProviderClient fusedLocationProviderClient;
    MapViewViewModel mapViewViewModel;
    MapViewUtils mMapViewUtils;
    private FragmentMapViewBinding binding;
    private String markerId;
    private String markerName;
    private LatLng markerPosition;
    GeoLocateRepository mGeoLocateRepository;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mapViewViewModel = new ViewModelProvider(requireActivity()).get(MapViewViewModel.class);
        binding = FragmentMapViewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        resultList = new ArrayList<>();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mGeoLocateRepository = new GeoLocateRepository();
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
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        mMapViewUtils = new MapViewUtils(this.mapViewViewModel, this.getContext(),
                this.locationPermissionGranted, this.getActivity());

        mMapViewUtils.getLocationPermission();
        // Turn on the My Location layer and the related control on the map.
        mMapViewUtils.updateLocationUI(this.googleMap);
        // Get the current location of the device and set the position of the map.
        mMapViewUtils.getDeviceLocation(this.googleMap, this.getViewLifecycleOwner());
      //  mMapViewUtils.moveCamera(this.googleMap, this.getViewLifecycleOwner());
        googleMap.setOnMarkerClickListener(this);
       // mMapViewUtils.getLastLocation();
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        resultList = mMapViewUtils.getNearRestaurantList();
        markerId = marker.getId();
        markerName = marker.getTitle();
        markerPosition = marker.getPosition();
        for (GoogleClass1.Result restaurant : resultList) {
            LatLng restoPosition = new LatLng(restaurant.getGeometry().getLocation().getLat(),
                    restaurant.getGeometry().getLocation().getLng());
            String restoName = restaurant.getName();
            if ((restoName.equals(markerName)) && (restoPosition.equals(markerPosition))) {
                Intent intentDetail = new Intent(this.getActivity(), DetailActivity.class);
                intentDetail.putExtra(RESTO_ID, restaurant.getPlaceId());
                intentDetail.putExtra(RESTO_NAME, restoName);
                if((restaurant.getOpeningHours() != null) && (restaurant.getOpeningHours().getOpenNow()) != null) {
                    intentDetail.putExtra(RESTO_OPENINGHOURS, restaurant.getOpeningHours().getOpenNow());
                }
                if (restaurant.getPhotos() != null) {
                    if (restaurant.getPhotos().get(0).getPhotoReference() != null) {
                        intentDetail.putExtra(RESTO_PHOTO_URL, restaurant.getPhotos().get(0).getPhotoReference());
                    }
                }
                if((restaurant.getTypes()) != null && (restaurant.getTypes().get(0))!=null) {
                    String restoAdressesAndTypeForDetailActivity = restaurant.getTypes().get(0) + " - "
                            + restaurant.getVicinity();
                    intentDetail.putExtra(RESTO_TYPE_ADRESSES, restoAdressesAndTypeForDetailActivity);
                }
                intentDetail.putExtra(RESTO_ID, restaurant.getPlaceId());
                startActivity(intentDetail);
                Log.e("TAG", "onMarkerClick:" +
                        " restoid: " + restaurant.getPlaceId() + "resto name " + restaurant.getName());
                Toast.makeText(this.getActivity().getApplicationContext(), "resto Name : " + restoName +
                                "  resto Id : " + restaurant.getPlaceId(), Toast.LENGTH_SHORT).show();
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
        mMapViewUtils.updateLocationUI(this.googleMap);
    }
}