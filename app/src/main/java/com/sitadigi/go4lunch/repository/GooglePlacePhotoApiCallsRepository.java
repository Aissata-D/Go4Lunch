package com.sitadigi.go4lunch.repository;

import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.utils.GoogleMapApiService;

import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GooglePlacePhotoApiCallsRepository {
        //  Creating a callback
        public interface Callbacks {
            void onResponse(@Nullable GoogleMapApiClass results);
            void onFailure();
        }

        //  Public method to start fetching users following by Jake Wharton
        public static void fetchRestaurantPhoto(GooglePlacePhotoApiCallsRepository.Callbacks callbacks,
                                                String photoReference, int maxWidth, int maxHeigth,
                                                String key){
            //  Create a weak reference to callback (avoid memory leaks)
            final WeakReference<GooglePlacePhotoApiCallsRepository.Callbacks> callbacksWeakReference = new WeakReference<GooglePlacePhotoApiCallsRepository.Callbacks>(callbacks);
            //  Get a Retrofit instance and the related endpoints
            GoogleMapApiService googleMapApiService = GoogleMapApiService.retrofit.create(GoogleMapApiService.class);

            //  Create the call on Github API
            Call<GoogleMapApiClass> call = googleMapApiService.getRestaurentPhoto(photoReference,maxWidth,
                    maxHeigth,key);
            //  Start the call
            call.enqueue(new Callback<GoogleMapApiClass>() {

                @Override
                public void onResponse(Call<GoogleMapApiClass> call, Response<GoogleMapApiClass> response) {
                    // Call the proper callback used in controller (MainFragment)
                    if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onResponse(response.body());
                }

                @Override
                public void onFailure(Call<GoogleMapApiClass> call, Throwable t) {
                    //  Call the proper callback used in controller (MainFragment)
                    if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
                }
            });
        }
}
