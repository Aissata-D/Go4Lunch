package com.sitadigi.go4lunch.utils;


import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.GooglePlaceDetailApiClass;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapApiService {

        @GET("maps/api/place/nearbysearch/json")
        Call<GoogleMapApiClass> getRestaurent(@Query("location") String location,
                                              @Query("radius") int radius,
                                              @Query("type") String type,
                                              @Query("key") String key);

        @GET("/maps/api/place/photo")
        Call<GoogleMapApiClass> getRestaurentPhoto(@Query("photo_reference") String referencePhoto,
                                                   @Query("maxwidth") int maxWidth,
                                                   @Query("maxheight") int maxHeigth,
                                                   @Query("key") String key);

        @GET("/maps/api/place/details/json")
        Call<GooglePlaceDetailApiClass> getRestaurentPhoneAndWebsite(@Query("place_id") String place_id,
                                                                     @Query("key") String key);

        public static final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
}
//Requette google place
//https://maps.googleapis.com/maps/api/place/details/json ?& place_id=ChIJ2YnaRyDA9EcRxV9Np980tws&key=AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08