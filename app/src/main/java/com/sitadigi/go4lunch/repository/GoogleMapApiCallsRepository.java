package com.sitadigi.go4lunch.repository;

import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.utils.GoogleMapApiService;

import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleMapApiCallsRepository {
    public static GoogleMapApiCallsRepository sNewsGoogleMapApiCallsRepository;

    public static GoogleMapApiCallsRepository getInstance() {
        if (sNewsGoogleMapApiCallsRepository == null) {
            sNewsGoogleMapApiCallsRepository = new GoogleMapApiCallsRepository();
        }
        return sNewsGoogleMapApiCallsRepository;
    }

    // Public method to start fetching users following by Jake Wharton
    public static List<GoogleMapApiClass.Result> fetchResultFollowing(/*Callbacks callbacks,*/ String location, int radius,
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
    }

    //  Creating a callback
   /* public interface Callbacks {
        void onResponse(@Nullable GoogleMapApiClass results);

        void onFailure();
    }*/
}
