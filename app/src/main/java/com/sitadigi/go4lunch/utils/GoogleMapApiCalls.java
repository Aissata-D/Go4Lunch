package com.sitadigi.go4lunch.utils;


import com.sitadigi.go4lunch.models.GoogleClass1;

import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleMapApiCalls {

    // 1 - Creating a callback
    public interface Callbacks {
        void onResponse(@Nullable GoogleClass1 results);
        void onFailure();
    }

    // 2 - Public method to start fetching users following by Jake Wharton
    public static void fetchResultFollowing(Callbacks callbacks, String location,int radius,
                                          String type, String key){
        // 2.1 - Create a weak reference to callback (avoid memory leaks)
        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<Callbacks>(callbacks);
        // 2.2 - Get a Retrofit instance and the related endpoints
        GoogleMapApiService googleMapApiService = GoogleMapApiService.retrofit.create(GoogleMapApiService.class);

        // 2.3 - Create the call on Github API
        Call<GoogleClass1> call = googleMapApiService.getRestaurent(location,radius,type,key);
        // 2.4 - Start the call
        call.enqueue(new Callback<GoogleClass1>() {

            @Override
            public void onResponse(Call<GoogleClass1> call, Response<GoogleClass1> response) {
                // 2.5 - Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onResponse(response.body());
            }

            @Override
            public void onFailure(Call<GoogleClass1> call, Throwable t) {
                // 2.5 - Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }
}
