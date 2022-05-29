package com.sitadigi.go4lunch.viewModel;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.sitadigi.go4lunch.R;
import com.sitadigi.go4lunch.models.GoogleClass1;
import com.sitadigi.go4lunch.repository.UserRepository;

import java.util.List;

public class UserViewModel extends ViewModel {
    private static volatile UserViewModel instance;
    private final UserRepository userRepository;
    private MutableLiveData<List<GoogleClass1.Result>> listOfRestaurent;
    //private final GoogleMapApiCallsRepository mGoogleMapApiCalls;

    public UserViewModel() {
        userRepository = UserRepository.getInstance();
      // mGoogleMapApiCalls = GoogleMapApiCallsRepository.getInstance();

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

    public CollectionReference getUsersCollection(){
       return userRepository.getUsersCollection();

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
    public MutableLiveData<List<GoogleClass1.Result>> getRestaurent() {

        Log.e("TAG", "getRestaurent: " +listOfRestaurent );
        return listOfRestaurent;
    }
   /* public void loadRestaurentData(String location) {

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

    }*/

}
