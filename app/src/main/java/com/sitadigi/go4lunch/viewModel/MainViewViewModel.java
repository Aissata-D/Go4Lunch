package com.sitadigi.go4lunch.viewModel;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
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
    private String restaurantWebSiteUrl;
    private String restaurantPhoneNumber;
    List<String> listOfPhoneAndWebSite = new ArrayList<>();


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

    public List <String>loadRestaurantPhoneNumberAndWebSite(GoogleMapApiClass.Result restaurant){

         disposablePlaceDetail = GoogleMapApiCallsRepository.streamFetchRestaurantDetail(
                 restaurant, API_KEY).subscribeWith(new DisposableObserver<GooglePlaceDetailApiClass>() {

             @Override
             public void onNext(GooglePlaceDetailApiClass googlePlaceDetailApiClass) {
                restaurantPhoneNumber = googlePlaceDetailApiClass.getResult().getInternationalPhoneNumber();
                restaurantWebSiteUrl = googlePlaceDetailApiClass.getResult().getWebsite();
               listOfPhoneAndWebSite.add(0,restaurantPhoneNumber);
               listOfPhoneAndWebSite.add(1,restaurantWebSiteUrl);

             }

             @Override
             public void onError(Throwable e) {
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

        // 1.2 - Execute the stream subscribing to Observable defined inside GithubStream
       // String apiKey = "AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08";
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



        /* listOfRestaurent.setValue(mGoogleMapApiCallsRepository.fetchResultFollowing(/*this,*//*
        location, 1500,
                "restaurant", "AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08"));*/
    }

    public void disposeWhenDestroy(){
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
  /*


        // 1 - Execute our Stream
    private void executeHttpRequestWithRetrofit(){
        // 1.1 - Update UI
        this.updateUIWhenStartingHTTPRequest();
        // 1.2 - Execute the stream subscribing to Observable defined inside GithubStream
        this.disposable = GithubStreams.streamFetchUserFollowing("JakeWharton").subscribeWith(new DisposableObserver<List<GithubUser>>() {
            @Override
            public void onNext(List<GithubUser> users) {
                Log.e("TAG","On Next");
                // 1.3 - Update UI with list of users
                updateUIWithListOfUsers(users);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG","On Error"+Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {
                Log.e("TAG","On Complete !!");
            }
        });
    }

    private void disposeWhenDestroy(){
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    // -------------------
    // UPDATE UI
    // -------------------

    private void updateUIWhenStartingHTTPRequest(){
        this.textView.setText("Downloading...");
    }

    private void updateUIWhenStopingHTTPRequest(String response){
        this.textView.setText(response);
    }


    @Override
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