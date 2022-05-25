package com.sitadigi.go4lunch.utils;


import com.sitadigi.go4lunch.models.GoogleClass1;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapApiService {

        @GET("maps/api/place/nearbysearch/json")
        Call<GoogleClass1> getRestaurent(@Query("location") String location,
                                         @Query("radius") int radius,
                                         @Query("type") String type,
                                         @Query("key") String key);

        @GET("/maps/api/place/photo")
        Call<GoogleClass1> getRestaurentPhoto(@Query("photo_reference") String referencePhoto,
                                              @Query("maxwidth") int maxWidth,
                                              @Query("maxheight") int maxHeigth,
                                              @Query("key") String key);



        public static final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

}



 /*  https://maps.googleapis.com/maps/api/place/photo
  ?maxwidth=400
  &photo_reference=Aap_uEA7vb0DDYVJWEaX3O-AtYp77AaswQKSGtDaimt3gt7QCNpdjp1BkdM6acJ96xTec3tsV_ZJNL_JP-lqsVxydG3nh739RE_hepOOL05tfJh2_ranjMadb3VoBYFvF0ma6S24qZ6QJUuV6sSRrhCskSBP5C1myCzsebztMfGvm7ij3gZT
  &key=YOUR_API_KEY


 @GET("users/{username}/following")
        Call<List<GithubUser>> getFollowing(@Path("username") String username);

          public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
            */


// @GET("users/{username}/following")
//@GET("maps/api/place/nearbysearch/json?location=45.771944%2C4.8901709&radius=150&type=restaurant&key=AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08")
