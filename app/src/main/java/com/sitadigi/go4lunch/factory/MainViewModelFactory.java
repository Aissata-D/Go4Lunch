package com.sitadigi.go4lunch.factory;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sitadigi.go4lunch.repository.GoogleMapApiCallsInterface;
import com.sitadigi.go4lunch.viewModel.MainViewViewModel;

public class MainViewModelFactory implements ViewModelProvider.Factory {

    GoogleMapApiCallsInterface mGoogleMapApiCallsInterface;

    public MainViewModelFactory(GoogleMapApiCallsInterface googleMapApiCallsInterface) {
        mGoogleMapApiCallsInterface = googleMapApiCallsInterface;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new MainViewViewModel(mGoogleMapApiCallsInterface);
    }
}
