package com.sitadigi.go4lunch.viewModel;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sitadigi.go4lunch.R;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.repository.UserRepository;

import java.text.BreakIterator;
import java.util.List;

public class UserViewModel extends ViewModel {
    private static volatile UserViewModel instance;
    private final UserRepository userRepository;

    private UserViewModel() {
        userRepository = UserRepository.getInstance();
    }

    public static UserViewModel getInstance() {
        UserViewModel result = instance;
        if (result != null) {
            return result;
        }
        synchronized(UserRepository.class) {
            if (instance == null) {
                instance = new UserViewModel();
            }
            return instance;
        }
    }


    public FirebaseAuth getCurrentInstance(){
        return userRepository.getCurrentInstance();
    }

    public FirebaseUser getCurrentUser(){
        return userRepository.getCurrentUser();
    }

    public Boolean isCurrentUserLogged(){
        return (this.getCurrentUser() != null);
    }
    public Task<Void> signOut(Context context){
        return userRepository.signOut(context);
    }

    public Task<Void> deleteUser(Context context){

        // Delete the user account from the Auth
        return userRepository.deleteUser(context).addOnCompleteListener(task -> {
            // Once done, delete the user datas from Firestore
            userRepository.deleteUserFromFirestore();
        });
    }


    public void createUser(){
        userRepository.createUser();
    }


    public Task<Void> updateUsername(String username){
        return userRepository.updateUsername(username);
    }


    public void updateUIWithUserData(Context context, TextView userName,
                                     TextView userEmail, ImageView userPhoto){
        if(isCurrentUserLogged()){
            FirebaseUser user = getCurrentUser();

            if(user.getPhotoUrl() != null){
                //Set user profilphoto
                Glide.with(context)
                        .load(user.getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(userPhoto);
            }
            //Get email & username from User
            String email = TextUtils.isEmpty(user.getEmail()) ? context.getString(R.string.info_no_email_found) : user.getEmail();
            String username = TextUtils.isEmpty(user.getDisplayName()) ? context.getString(R.string.info_no_username_found) : user.getDisplayName();
            //Update views with data
            userName.setText(username);
            userEmail.setText(email);
        }
    }

}
