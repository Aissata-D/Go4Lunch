package com.sitadigi.go4lunch.viewModel;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.sitadigi.go4lunch.models.GoogleDistanceMatrixClass;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.GooglePlaceDetailApiClass;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.repository.GoogleMapApiCallsRepository;
import com.sitadigi.go4lunch.repository.GeoLocateRepository;
import com.sitadigi.go4lunch.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class MainViewViewModel extends ViewModel /*implements GoogleMapApiCallsRepository.Callbacks*/ {
    private final String API_KEY = "AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08";
    private final UserRepository userRepository;
    private final GoogleMapApiCallsRepository mGoogleMapApiCallsRepository;
    private final MutableLiveData<List<String>> resultSearchMutablelist;
    private final GeoLocateRepository mGeoLocateRepository;
    private final MutableLiveData<List<GoogleMapApiClass.Result>> listOfRestaurant;
    private final MutableLiveData<LatLng> resultSearchLatLng;
    private final MutableLiveData<String> resultSearchPlaceName;
    public MutableLiveData<Location> locationMutableLiveData;
    Disposable disposable;
    Disposable disposablePlaceDetail;
    Disposable disposableDistance;
    List<String> listOfPhoneAndWebSite = new ArrayList<>();
    String distance;
    private String restaurantWebSiteUrl;
    private String restaurantPhoneNumber;


    public MainViewViewModel() {
        userRepository = UserRepository.getInstance();
        // mGoogleMapApiCallsRepository = GoogleMapApiCallsRepository.getInstance();
        mGoogleMapApiCallsRepository = new GoogleMapApiCallsRepository();

        mGeoLocateRepository = new GeoLocateRepository();
        locationMutableLiveData = new MutableLiveData<>();
        resultSearchMutablelist = new MutableLiveData<>();
        resultSearchLatLng = new MutableLiveData<>();
        resultSearchPlaceName = new MutableLiveData<>();
        listOfRestaurant = new MutableLiveData<>();

    }

    public String getRestaurantDistance(String origins, String destinations) {
        disposableDistance = GoogleMapApiCallsRepository.streamFetchRestaurantDistance(origins,
                destinations, API_KEY).subscribeWith(new DisposableObserver<GoogleDistanceMatrixClass>() {

            @Override
            public void onNext(GoogleDistanceMatrixClass googleDistanceMatrixClass) {
                if(googleDistanceMatrixClass.getRows() == null){
                    distance = "Not Response";
                }
                if(googleDistanceMatrixClass.getRows().size()>0){
                    distance = "Not Response1";
                }
                if(googleDistanceMatrixClass.getRows().get(0)==null){
                    distance = "Not Response2";
                }
                if(googleDistanceMatrixClass.getRows().get(0).getElements()== null){
                    distance = "Not Response3";
                }
                if(googleDistanceMatrixClass.getRows().get(0).getElements().size()==0){
                    distance = "Not Response4";
                }
                if(googleDistanceMatrixClass.getRows().get(0).getElements().get(0)==null){
                    distance = "Not Response5";
                }
                if(googleDistanceMatrixClass.getRows().get(0).getElements().get(0).getDistance()==null){
                    distance = "Not Response6";
                }
               // if(googleDistanceMatrixClass.getRows().get(0).getElements().get(0).getDistance().getText()==null){
                 //   distance = "Not Response7";
                //}

                if(googleDistanceMatrixClass != null && googleDistanceMatrixClass.getRows() != null
                && googleDistanceMatrixClass.getRows().size()>0 && googleDistanceMatrixClass.getRows()
                        .get(0) != null && googleDistanceMatrixClass.getRows().get(0).getElements() != null
                && googleDistanceMatrixClass.getRows().get(0).getElements().size() >0 &&
                        googleDistanceMatrixClass.getRows().get(0).getElements().get(0).getDistance() != null
                && googleDistanceMatrixClass.getRows().get(0).getElements().get(0).getDistance().getText() != null){

                    distance = googleDistanceMatrixClass.getRows().get(0).getElements().get(0).getDistance().getText();

                }else {distance = "not found NULL POINTER EX";}
            }

            @Override
            public void onError(Throwable e) {
                distance = "not found ERROR";

            }

            @Override
            public void onComplete() {

            }
        });
        return distance;

    }

    public List<String> loadRestaurantPhoneNumberAndWebSite(GoogleMapApiClass.Result restaurant) {

        disposablePlaceDetail = GoogleMapApiCallsRepository.streamFetchRestaurantDetail(
                restaurant, API_KEY).subscribeWith(new DisposableObserver<GooglePlaceDetailApiClass>() {

            @Override
            public void onNext(GooglePlaceDetailApiClass googlePlaceDetailApiClass) {
                restaurantPhoneNumber = googlePlaceDetailApiClass.getResult().getInternationalPhoneNumber();
                restaurantWebSiteUrl = googlePlaceDetailApiClass.getResult().getWebsite();
                listOfPhoneAndWebSite.add(0, restaurantPhoneNumber);
                listOfPhoneAndWebSite.add(1, restaurantWebSiteUrl);

            }

            @Override
            public void onError(Throwable e) {
                listOfPhoneAndWebSite = new ArrayList<>();
                Log.e("TAG", "On Error getRestaurantPhoneNumberAndWebSite" + Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {
                Log.e("TAG", "On Complete !! getRestaurantPhoneNumberAndWebSite");
            }
        });

        return listOfPhoneAndWebSite;
    }


    public MutableLiveData<List<GoogleMapApiClass.Result>> getRestaurant() {

        return listOfRestaurant;
    }

    public void loadRestaurantData(String location) {

        //FOR DATA Disposable allow to avoid Memory Leaks
        this.disposable = GoogleMapApiCallsRepository.streamFetchListOfNearRestaurant(
                        location, 1500, "restaurant", API_KEY)
                .subscribeWith(new DisposableObserver<GoogleMapApiClass>() {

                    @Override
                    public void onNext(GoogleMapApiClass googleMapApiClass) {

                        listOfRestaurant.setValue(googleMapApiClass.getResults());

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "On Error loadRestaurantData" + Log.getStackTraceString(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG", "On Complete !! loadRestaurantData");
                    }
                });
    }

    public void disposeWhenDestroy() {
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }


    public void loadLocationMutableLiveData(Context context, Activity activity, MainViewViewModel mainViewViewModel) {

        locationMutableLiveData = mGeoLocateRepository.getDeviceLocation(context, activity, mainViewViewModel);
    }

    public MutableLiveData<Location> getLocationMutableLiveData() {

        return locationMutableLiveData;
    }

    public void setSearchLatLngMutableLiveData(LatLng latLng) {

        resultSearchLatLng.setValue(latLng);
    }

    public MutableLiveData<LatLng> getResultSearchLatLng() {

        return resultSearchLatLng;
    }

    public void setSearchPlaceNameMutableLiveData(String placeName) {

        resultSearchPlaceName.setValue(placeName);
    }

    public MutableLiveData<String> getResultSearchPlaceName() {
        return resultSearchPlaceName;
    }

    public MutableLiveData<List<User>> getAllUser() {
        return userRepository.getAllUser();
    }
}