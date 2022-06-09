package com.sitadigi.go4lunch.ui.mapView;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.sitadigi.go4lunch.models.GoogleClass1;
import com.sitadigi.go4lunch.repository.GoogleMapApiCallsRepository;
import com.sitadigi.go4lunch.repository.GeoLocateRepository;

import java.util.List;

public class MapViewViewModel extends ViewModel implements GoogleMapApiCallsRepository.Callbacks{
    private final GoogleMapApiCallsRepository mGoogleMapApiCallsRepository;
    private MutableLiveData<List<String>> resultSearchMutablelist;
    public MutableLiveData<Location> locationMutableLiveData;

    GeoLocateRepository mGeoLocateRepository ;
    // private final MutableLiveData<String> mText;
    private MutableLiveData<List<GoogleClass1.Result>> listOfRestaurent;
    private MutableLiveData<LatLng> resultSearchLatLng;

    public MapViewViewModel() {
        mGoogleMapApiCallsRepository = GoogleMapApiCallsRepository.getInstance();
        mGeoLocateRepository = new GeoLocateRepository();
        locationMutableLiveData = new MutableLiveData<>();
        resultSearchMutablelist = new MutableLiveData<>();
        resultSearchLatLng = new MutableLiveData<>();
    }

    public MutableLiveData<List<GoogleClass1.Result>> getRestaurent() {

        Log.e("TAG", "getRestaurent: " +listOfRestaurent );
        return listOfRestaurent;
    }
    public void loadRestaurentData(String location) {
//        locationMutableLiveData.setValue(location);

        listOfRestaurent = mGoogleMapApiCallsRepository.fetchResultFollowing(this, location,1500,
                "restaurant","AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08");

    }
    public void loadLocationMutableLiveData(Context context, Activity activity, MapViewViewModel mapViewViewModel){
        locationMutableLiveData = mGeoLocateRepository.getDeviceLocation( context,activity,mapViewViewModel);


    }

    public MutableLiveData<Location> getLocationMutableLiveData() {

        return locationMutableLiveData;

    }

    public void getSearchMutableLiveData(List<String> list) {
        resultSearchMutablelist.setValue(list); ;

    }
    public MutableLiveData<List<String>> getResultSearch(){
        return resultSearchMutablelist;
    }

    public void getSearchLocationMutableLiveData(LatLng latLng) {
        resultSearchLatLng.setValue(latLng); ;

    }
    public MutableLiveData<LatLng> getResultSearchLatLng(){
        return resultSearchLatLng;
    }


    @Override
    public void onResponse(@Nullable GoogleClass1 results) {
        listOfRestaurent.setValue(results.getResults());
        Log.e("TAG", "onResponse: listeresto recupere"+listOfRestaurent.getValue() );
        //listOfRestaurent.setValue(results.getResults());

    }

    @Override
    public void onFailure() {
        Log.e("TAG", "onFailure: ");

    }
}