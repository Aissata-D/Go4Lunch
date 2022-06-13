package com.sitadigi.go4lunch.ui.mapView;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.arch.core.internal.SafeIterableMap;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.sitadigi.go4lunch.models.GoogleClass1;
import com.sitadigi.go4lunch.repository.GoogleMapApiCallsRepository;
import com.sitadigi.go4lunch.repository.GeoLocateRepository;

import java.util.List;

public class MapViewViewModel extends ViewModel implements GoogleMapApiCallsRepository.Callbacks{
    private final GoogleMapApiCallsRepository mGoogleMapApiCallsRepository;
    private final MutableLiveData<List<String>> resultSearchMutablelist;
    public MutableLiveData<Location> locationMutableLiveData;
    private final GeoLocateRepository mGeoLocateRepository ;
    private final MutableLiveData<List<GoogleClass1.Result>> listOfRestaurent;
    private final MutableLiveData<LatLng> resultSearchLatLng;
    private final MutableLiveData<String> resultSearchPlaceName;

    public MapViewViewModel() {
        mGoogleMapApiCallsRepository = GoogleMapApiCallsRepository.getInstance();
        mGeoLocateRepository = new GeoLocateRepository();
        locationMutableLiveData = new MutableLiveData<>();
        resultSearchMutablelist = new MutableLiveData<>();
        resultSearchLatLng = new MutableLiveData<>();
        resultSearchPlaceName = new MutableLiveData<>();
        listOfRestaurent = new MutableLiveData<>();
    }

    public MutableLiveData<List<GoogleClass1.Result>> getRestaurent() {

        return listOfRestaurent;
    }

    public void loadRestaurentData(String location) {

        listOfRestaurent.setValue(mGoogleMapApiCallsRepository.fetchResultFollowing(this, location,1500,
                "restaurant","AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08"));
    }

    public void loadLocationMutableLiveData(Context context, Activity activity, MapViewViewModel mapViewViewModel){

        locationMutableLiveData = mGeoLocateRepository.getDeviceLocation( context,activity,mapViewViewModel);
    }

    public MutableLiveData<Location> getLocationMutableLiveData() {

        return locationMutableLiveData;
    }

    public void setSearchLatLngMutableLiveData(LatLng latLng) {

        resultSearchLatLng.setValue(latLng);
    }

    public MutableLiveData<LatLng> getResultSearchLatLng(){

        return resultSearchLatLng;
    }

    public void setSearchPlaceNameMutableLiveData(String placeName) {

        resultSearchPlaceName.setValue(placeName);
    }

    public MutableLiveData<String> getResultSearchPlaceName(){
            return resultSearchPlaceName;
    }

    @Override
    public void onResponse(@Nullable GoogleClass1 results) {
        listOfRestaurent.setValue(results.getResults());
        Log.e("TAG", "onResponse: listeresto recupere"+listOfRestaurent.getValue() );
    }

    @Override
    public void onFailure() {
        Log.e("TAG", "onFailure: ");

    }
}