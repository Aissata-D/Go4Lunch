package com.sitadigi.go4lunch.repository;

import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.GooglePlaceDetailApiClass;
import com.sitadigi.go4lunch.service.GoogleMapApiService;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GoogleMapApiCallsRepository implements GoogleMapApiCallsInterface {
    @Override
    public Observable<GoogleMapApiClass> streamFetchListOfNearRestaurant(String location, int radius
            , String type, String key) {
        GoogleMapApiService googleMapApiService = GoogleMapApiService.retrofit
                .create(GoogleMapApiService.class);
        return googleMapApiService.getRestaurant(location, radius, type, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    @Override
    public Observable<GooglePlaceDetailApiClass> streamFetchRestaurantDetail(String restaurantId, String apiKey) {
        GoogleMapApiService googleMapApiService = GoogleMapApiService.retrofit
                .create(GoogleMapApiService.class);
        return googleMapApiService.getRestaurantPhoneAndWebsite(restaurantId, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

}
