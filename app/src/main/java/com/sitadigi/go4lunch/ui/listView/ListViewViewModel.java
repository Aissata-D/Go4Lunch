package com.sitadigi.go4lunch.ui.listView;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sitadigi.go4lunch.models.GoogleClass1;
import com.sitadigi.go4lunch.utils.GoogleMapApiCalls;

import java.util.List;

public class ListViewViewModel extends ViewModel implements GoogleMapApiCalls.Callbacks{
    private final GoogleMapApiCalls mGoogleMapApiCalls;
    @SuppressWarnings({"FieldCanBeLocal"})
    private MutableLiveData<List<GoogleClass1.Result>> listOfRestaurent; // = new MutableLiveData<>();

    private final MutableLiveData<String> mText;

    public ListViewViewModel() {
        mGoogleMapApiCalls = GoogleMapApiCalls.getInstance();
        mText = new MutableLiveData<>();
        mText.setValue("This is listView fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }


    public MutableLiveData<List<GoogleClass1.Result>> getRestaurent() {

        Log.e("TAG", "getRestaurent: " +listOfRestaurent );
        return listOfRestaurent;
    }
    public void loadRestaurentData(String location) {

        listOfRestaurent = mGoogleMapApiCalls.fetchResultFollowing(this, location,1500,
                "restaurant","AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08");

    }

    @Override
    public void onResponse(@Nullable GoogleClass1 results) {
        listOfRestaurent.setValue(results.getResults());
        Log.e("TAG", "onResponse: listeresto recupere"+listOfRestaurent.getValue() );
        //listOfRestaurent.setValue(results.getResults());

    }

    @Override
    public void onFailure() {
        Log.e("TAG", "onFailure: ");

    }
}