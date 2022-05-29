package com.sitadigi.go4lunch.repository;


import androidx.lifecycle.MutableLiveData;

import com.sitadigi.go4lunch.models.GoogleClass1;
import com.sitadigi.go4lunch.utils.GoogleMapApiService;

import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleMapApiCallsRepository {
int i;
    // 1 - Creating a callback
    public interface Callbacks {
        void onResponse(@Nullable GoogleClass1 results);
        void onFailure();
    }
    public static GoogleMapApiCallsRepository sNewsGoogleMapApiCallsRepository;
    public static GoogleMapApiCallsRepository getInstance(){
        if (sNewsGoogleMapApiCallsRepository == null){
            sNewsGoogleMapApiCallsRepository = new GoogleMapApiCallsRepository();
        }
        return sNewsGoogleMapApiCallsRepository;
    }

    // 2 - Public method to start fetching users following by Jake Wharton
    public static  MutableLiveData<List<GoogleClass1.Result>>  fetchResultFollowing(Callbacks callbacks, String location, int radius,
                                                                                String type, String key){

       MutableLiveData<List<GoogleClass1.Result>> listOfRestaurent = new MutableLiveData<>();

        // 2.1 - Create a weak reference to callback (avoid memory leaks)
        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<Callbacks>(callbacks);
        // 2.2 - Get a Retrofit instance and the related endpoints
        GoogleMapApiService googleMapApiService = GoogleMapApiService.retrofit.create(GoogleMapApiService.class);

        // 2.3 - Create the call on Github API
        Call<GoogleClass1> call = googleMapApiService.getRestaurent(location,radius,type,key);
        // 2.4 - Start the cal

        call.enqueue(new Callback<GoogleClass1>() {


            @Override
            public void onResponse(Call<GoogleClass1> call, Response<GoogleClass1> response) {
                if (response.isSuccessful()){

                    listOfRestaurent.setValue(response.body().getResults());
            }

                // 2.5 - Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onResponse(response.body());
            }

            @Override
            public void onFailure(Call<GoogleClass1> call, Throwable t) {
                listOfRestaurent.setValue(null);

                // 2.5 - Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
        return listOfRestaurent;
    }
}
