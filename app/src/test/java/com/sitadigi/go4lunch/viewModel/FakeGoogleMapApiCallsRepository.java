package com.sitadigi.go4lunch.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.sitadigi.go4lunch.models.GoogleDistanceMatrixClass;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.GooglePlaceDetailApiClass;
import com.sitadigi.go4lunch.repository.GoogleMapApiCallsInterface;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class FakeGoogleMapApiCallsRepository implements GoogleMapApiCallsInterface {
    @Override
    public Observable<GoogleMapApiClass> streamFetchListOfNearRestaurant(String location, int radius, String type, String key) {
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
    public Observable<GooglePlaceDetailApiClass> streamFetchRestaurantDetail(GoogleMapApiClass.Result restaurant, String apiKey) {
        GooglePlaceDetailApiClass fakeGooglePlaceDetailApiClass = new GooglePlaceDetailApiClass();
        return Observable.just(fakeGooglePlaceDetailApiClass);
    }

    @Override
    public Observable<GoogleDistanceMatrixClass> streamFetchRestaurantDistance(String destinations, String origins, String apiKey) {
        GoogleDistanceMatrixClass googleDistanceMatrixClass = new GoogleDistanceMatrixClass();
        return Observable.just(googleDistanceMatrixClass);
    }
}
