package com.sitadigi.go4lunch.utils;


import com.sitadigi.go4lunch.models.GoogleClass1;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapApiService {
      /*  @GET("users/{username}/following")
        Call<List<GithubUser>> getFollowing(@Path("username") String username);

          public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
            */


       // @GET("users/{username}/following")
       //@GET("maps/api/place/nearbysearch/json?location=45.771944%2C4.8901709&radius=150&type=restaurant&key=AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08")

        @GET("maps/api/place/nearbysearch/json")
       // Call<List<Result>> getFollowing(@Path("username") String username);
        Call<GoogleClass1> getFollowing(@Query("location") String location,
                                                          @Query("radius") int radius,
                                                          @Query("type") String type,
                                                          @Query("key") String key);
      //  @Query("company_name") String name



        public static final Retrofit retrofit = new Retrofit.Builder()
               // .baseUrl("https://api.github.com/")
                .baseUrl("https://maps.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

}
