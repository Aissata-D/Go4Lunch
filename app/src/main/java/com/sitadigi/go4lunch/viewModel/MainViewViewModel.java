package com.sitadigi.go4lunch.viewModel;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.repository.GoogleMapApiCallsRepository;
import com.sitadigi.go4lunch.repository.GeoLocateRepository;
import com.sitadigi.go4lunch.repository.UserRepository;

import java.util.List;

public class MainViewViewModel extends ViewModel /*implements GoogleMapApiCallsRepository.Callbacks*/{
    private final UserRepository userRepository;
    private final GoogleMapApiCallsRepository mGoogleMapApiCallsRepository;
    private final MutableLiveData<List<String>> resultSearchMutablelist;
    public MutableLiveData<Location> locationMutableLiveData;
    private final GeoLocateRepository mGeoLocateRepository ;
    private final MutableLiveData<List<GoogleMapApiClass.Result>> listOfRestaurent;
    private final MutableLiveData<LatLng> resultSearchLatLng;
    private final MutableLiveData<String> resultSearchPlaceName;

    public MainViewViewModel() {
        userRepository = UserRepository.getInstance();
        mGoogleMapApiCallsRepository = GoogleMapApiCallsRepository.getInstance();
        mGeoLocateRepository = new GeoLocateRepository();
        locationMutableLiveData = new MutableLiveData<>();
        resultSearchMutablelist = new MutableLiveData<>();
        resultSearchLatLng = new MutableLiveData<>();
        resultSearchPlaceName = new MutableLiveData<>();
        listOfRestaurent = new MutableLiveData<>();

    }

    public MutableLiveData<List<GoogleMapApiClass.Result>> getRestaurent() {

        return listOfRestaurent;
    }

    public void loadRestaurentData(String location) {

        listOfRestaurent.setValue(mGoogleMapApiCallsRepository.fetchResultFollowing(/*this,*/ location,1500,
                "restaurant","AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08"));
    }

    public void loadLocationMutableLiveData(Context context, Activity activity, MainViewViewModel mainViewViewModel){

        locationMutableLiveData = mGeoLocateRepository.getDeviceLocation( context,activity, mainViewViewModel);
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
    public MutableLiveData<List<User>> getAllUser(){
        return userRepository.getAllUser();
    }
  /*  @Override
    public void onResponse(@Nullable GoogleMapApiClass results) {
        listOfRestaurent.setValue(results.getResults());
        Log.e("TAG", "onResponse: listeresto recupere"+listOfRestaurent.getValue() );
    }

    @Override
    public void onFailure() {
        Log.e("TAG", "onFailure: ");

    }


   */
}