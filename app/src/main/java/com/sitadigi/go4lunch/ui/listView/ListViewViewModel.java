package com.sitadigi.go4lunch.ui.listView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sitadigi.go4lunch.models.GoogleClass1;
import com.sitadigi.go4lunch.repository.GoogleMapApiCallsRepository;

import java.util.List;

public class ListViewViewModel extends ViewModel /*implements GoogleMapApiCallsRepository.Callbacks*/{
    private final GoogleMapApiCallsRepository mGoogleMapApiCallsRepository;
    @SuppressWarnings({"FieldCanBeLocal"})
    private MutableLiveData<List<GoogleClass1.Result>> listOfRestaurent; // = new MutableLiveData<>();

    private final MutableLiveData<String> mText;

    public ListViewViewModel() {
        mGoogleMapApiCallsRepository = GoogleMapApiCallsRepository.getInstance();
        mText = new MutableLiveData<>();
        mText.setValue("This is listView fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}