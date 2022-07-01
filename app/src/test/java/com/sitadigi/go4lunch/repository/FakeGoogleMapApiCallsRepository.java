package com.sitadigi.go4lunch.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.GooglePlaceDetailApiClass;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;

public class FakeGoogleMapApiCallsRepository implements GoogleMapApiCallsInterface {
    @Override
    public Observable<GoogleMapApiClass> streamFetchListOfNearRestaurant(String location, int radius
            , String type, String key) {
        Log.e("FAKE", "streamFetchListOfNearRestaurant: " );
        GoogleMapApiClass.Result result1 = new GoogleMapApiClass.Result();
        result1.setName("nameOfResult1");
        List<GoogleMapApiClass.Result> results = Arrays.asList(result1);
        final GoogleMapApiClass fakeGoogleMapApiClass = new GoogleMapApiClass();
        fakeGoogleMapApiClass.setResults(results);
        MutableLiveData<GoogleMapApiClass> googleMapApiClassMutable = new MutableLiveData<>();
        googleMapApiClassMutable.setValue(fakeGoogleMapApiClass);

        return Observable.just(fakeGoogleMapApiClass);
    }

    @Override
    public Observable<GooglePlaceDetailApiClass> streamFetchRestaurantDetail(String restaurantId, String apiKey) {
        GooglePlaceDetailApiClass fakeGooglePlaceDetailApiClass = new GooglePlaceDetailApiClass();
        return Observable.just(fakeGooglePlaceDetailApiClass);
    }

}
