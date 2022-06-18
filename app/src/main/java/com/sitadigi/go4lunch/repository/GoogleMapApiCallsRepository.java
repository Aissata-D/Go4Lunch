package com.sitadigi.go4lunch.repository;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.sitadigi.go4lunch.models.GoogleDistanceMatrixClass;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.GooglePlaceDetailApiClass;
import com.sitadigi.go4lunch.utils.GoogleMapApiService;

import java.util.List;
import java.util.concurrent.TimeUnit;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class GoogleMapApiCallsRepository {

    public static Observable<GoogleMapApiClass> streamFetchListOfNearRestaurant(String location, int radius,
                                                                                String type, String key) {
        GoogleMapApiService googleMapApiService = GoogleMapApiService.retrofit
                .create(GoogleMapApiService.class);
        return googleMapApiService.getRestaurent(location, radius, type, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    // 1 - Create a stream that will get user infos on Github API
    public static Observable<GooglePlaceDetailApiClass> streamFetchRestaurantDetail(
            GoogleMapApiClass.Result restaurant, String apiKey){
        GoogleMapApiService googleMapApiService = GoogleMapApiService.retrofit
                .create(GoogleMapApiService.class);
        return googleMapApiService.getRestaurantPhoneAndWebsite(restaurant.getPlaceId(),apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
    // 1 - Create a stream that will get user infos on Github API
    public static Observable<GoogleDistanceMatrixClass> streamFetchRestaurantDistance(
            String destinations,String origins, String apiKey){
        GoogleMapApiService googleMapApiService = GoogleMapApiService.retrofit
                .create(GoogleMapApiService.class);
        return googleMapApiService.getRestaurantDistance(destinations,origins,apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

       /* // 2 - Create a stream that will :
        //     A. Fetch all users followed by "username"
        //     B. Return the first user of the list
        //     C. Fetch details of the first user
        @RequiresApi(api = Build.VERSION_CODES.N)
        public static Observable<GooglePlaceDetailApiClass> streamFetchListOfNearRestaurantAndRestaurantDetail(
                String location, int radius,String type, String key, GoogleMapApiClass.Result restaurant){
            return streamFetchListOfNearRestaurant(location,  radius, type,  key ) // A.

                    .map(new Function<GoogleMapApiClass,List<GoogleMapApiClass.Result> >() {

                        @Override
                        public List<GoogleMapApiClass.Result> apply(GoogleMapApiClass googleMapApiClass) {
                            return googleMapApiClass.getResults();
                        }
                    })
                    .flatMap(new Function<List<GoogleMapApiClass.Result>, Observable<GooglePlaceDetailApiClass>>() {
                        @Override
                        public Observable<GooglePlaceDetailApiClass> apply(List<GoogleMapApiClass.Result> results)
                                throws Exception {
                            for(GoogleMapApiClass.Result result : results){
                                streamFetchRestaurantDetail(result.getPlaceId(),key);
                            }
                            return null;
                        }

                        @Override
                        public Observable<GithubUserInfo> apply(GithubUser user) throws Exception {
                            // C.
                            return streamFetchUserInfos(user.getLogin());
                        }
                    }) ;
        }*/


  /*
    public static GoogleMapApiCallsRepository sNewsGoogleMapApiCallsRepository;


    public static GoogleMapApiCallsRepository getInstance() {
        if (sNewsGoogleMapApiCallsRepository == null) {
            sNewsGoogleMapApiCallsRepository = new GoogleMapApiCallsRepository();
        }
        return sNewsGoogleMapApiCallsRepository;
    }

    // Public method to start fetching users following by Jake Wharton
    public static List<GoogleMapApiClass.Result> fetchResultFollowing(/*Callbacks callbacks,*/ /*String location, int radius,
                                                                      String type, String key) {

        final List<GoogleMapApiClass.Result> listOfRestaurent = new ArrayList<>();
        //  Create a weak reference to callback (avoid memory leaks)
      //  final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<Callbacks>(callbacks);
        //  Get a Retrofit instance and the related endpoints
        GoogleMapApiService googleMapApiService = GoogleMapApiService.retrofit.create(GoogleMapApiService.class);
        //  Create the call on Github API
        Call<GoogleMapApiClass> call = googleMapApiService.getRestaurent(location, radius, type, key);
        //  Start the cal
        call.enqueue(new Callback<GoogleMapApiClass>() {

            @Override
            public void onResponse(Call<GoogleMapApiClass> call, Response<GoogleMapApiClass> response) {
                if (response.isSuccessful()) {
                    listOfRestaurent.clear();
                    listOfRestaurent.addAll(response.body().getResults());
                }
                // Call the proper callback used in controller (MainFragment)
               // if (callbacksWeakReference.get() != null)
                  //  callbacksWeakReference.get().onResponse(response.body());
            }

            @Override
            public void onFailure(Call<GoogleMapApiClass> call, Throwable t) {
                listOfRestaurent.clear();
                //  Call the proper callback used in controller (MainFragment)
              //  if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
        return listOfRestaurent;
    }*/

        //  Creating a callback
   /* public interface Callbacks {
        void onResponse(@Nullable GoogleMapApiClass results);

        void onFailure();
    }*/

}
