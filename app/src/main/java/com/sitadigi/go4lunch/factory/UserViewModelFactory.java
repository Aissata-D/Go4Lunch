package com.sitadigi.go4lunch.factory;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sitadigi.go4lunch.repository.UserRepositoryInterface;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

public class UserViewModelFactory implements ViewModelProvider.Factory {

    UserRepositoryInterface mUserRepositoryInterface;

    public UserViewModelFactory(UserRepositoryInterface userRepositoryInterface) {
        mUserRepositoryInterface = userRepositoryInterface;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new UserViewModel(mUserRepositoryInterface);
    }
}
