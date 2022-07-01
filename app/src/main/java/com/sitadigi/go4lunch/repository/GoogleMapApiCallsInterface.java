package com.sitadigi.go4lunch.repository;

import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.GooglePlaceDetailApiClass;

import io.reactivex.Observable;

public interface GoogleMapApiCallsInterface {
    Observable<GoogleMapApiClass> streamFetchListOfNearRestaurant(String location, int radius,
                                                                  String type, String key);

    Observable<GooglePlaceDetailApiClass> streamFetchRestaurantDetail(
            String restaurantId, String apiKey);

}
