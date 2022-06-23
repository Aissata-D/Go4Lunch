package com.sitadigi.go4lunch.repository;

import com.sitadigi.go4lunch.models.GoogleDistanceMatrixClass;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.GooglePlaceDetailApiClass;
import com.sitadigi.go4lunch.utils.GoogleMapApiService;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GoogleMapApiCallsRepository implements GoogleMapApiCallsInterface {
    @Override
    public Observable<GoogleMapApiClass> streamFetchListOfNearRestaurant(String location, int radius, String type, String key) {
        GoogleMapApiService googleMapApiService = GoogleMapApiService.retrofit
                .create(GoogleMapApiService.class);
        return googleMapApiService.getRestaurent(location, radius, type, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    @Override
    public Observable<GooglePlaceDetailApiClass> streamFetchRestaurantDetail(GoogleMapApiClass.Result restaurant, String apiKey) {
        GoogleMapApiService googleMapApiService = GoogleMapApiService.retrofit
                .create(GoogleMapApiService.class);
        return googleMapApiService.getRestaurantPhoneAndWebsite(restaurant.getPlaceId(),apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    @Override
    public Observable<GoogleDistanceMatrixClass> streamFetchRestaurantDistance(String destinations, String origins, String apiKey) {
        GoogleMapApiService googleMapApiService = GoogleMapApiService.retrofit
                .create(GoogleMapApiService.class);
        return googleMapApiService.getRestaurantDistance(destinations,origins,apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

/*
    public  Observable<GoogleMapApiClass> streamFetchListOfNearRestaurant(String location, int radius,
                                                                                String type, String key) {
        GoogleMapApiService googleMapApiService = GoogleMapApiService.retrofit
                .create(GoogleMapApiService.class);
        return googleMapApiService.getRestaurent(location, radius, type, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    // 1 - Create a stream that will get user info on Github API
    public  Observable<GooglePlaceDetailApiClass> streamFetchRestaurantDetail(
            GoogleMapApiClass.Result restaurant, String apiKey){
        GoogleMapApiService googleMapApiService = GoogleMapApiService.retrofit
                .create(GoogleMapApiService.class);
        return googleMapApiService.getRestaurantPhoneAndWebsite(restaurant.getPlaceId(),apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
    // 1 - Create a stream that will get user infos on Github API
    public  Observable<GoogleDistanceMatrixClass> streamFetchRestaurantDistance(
            String destinations,String origins, String apiKey){
        GoogleMapApiService googleMapApiService = GoogleMapApiService.retrofit
                .create(GoogleMapApiService.class);
        return googleMapApiService.getRestaurantDistance(destinations,origins,apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
    */
}
