package com.sitadigi.go4lunch.utils;


import com.sitadigi.go4lunch.models.GoogleMapApiClass;

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



        public static final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
}

