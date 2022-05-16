package com.sitadigi.go4lunch.ui.workmaters;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WorkmatersViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public WorkmatersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is workmaters fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}