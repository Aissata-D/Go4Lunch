package com.sitadigi.go4lunch.ui.workmaters;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class WorkmatersViewModel extends ViewModel {
    private final UserRepository userRepository;



    private final MutableLiveData<String> mText;

    public WorkmatersViewModel() {
        userRepository = UserRepository.getInstance();
        mText = new MutableLiveData<>();
    }

    /*
     *getAllUser method return users using this app in real time
     */

    public MutableLiveData<List<User>> getAllUser(){
       return userRepository.getAllUser();
    }


}