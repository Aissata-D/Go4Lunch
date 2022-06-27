package com.sitadigi.go4lunch.utils;


import com.sitadigi.go4lunch.models.GoogleDistanceMatrixClass;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.GooglePlaceDetailApiClass;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapApiService {

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    @GET("maps/api/place/nearbysearch/json")
    Observable<GoogleMapApiClass> getRestaurant(@Query("location") String location,
                                                @Query("radius") int radius,
                                                @Query("type") String type,
                                                @Query("key") String key,
                                                @Query("rankBy") String rankBy);

    @GET("/maps/api/distancematrix/json")
    Observable<GoogleDistanceMatrixClass> getRestaurantDistance(@Query("destinations") String destinations,
                                                                @Query("origins") String origins,
                                                                @Query("key") String key);

    @GET("/maps/api/place/details/json")
    Observable<GooglePlaceDetailApiClass> getRestaurantPhoneAndWebsite(@Query("place_id") String place_id,
                                                                       @Query("key") String key);


}

//requette google map
//https://maps.googleapis.com/maps/api/place/nearbysearch/json?&location=45.76667%2C4.88333&radius=1500&type=restaurant&key=AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08
//Requette google place
//https://maps.googleapis.com/maps/api/place/details/json ?& place_id=ChIJ2YnaRyDA9EcRxV9Np980tws&key=AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08
//Requette matrix
//https://maps.googleapis.com/maps/api/distancematrix/json?destinations=45.764043%2C4.835659&origins=45.771944%2C4.8901709&key=AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08
//Requette avec