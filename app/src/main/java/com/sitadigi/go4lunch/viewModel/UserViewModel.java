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
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.repository.UserRepository;
import com.sitadigi.go4lunch.repository.UserRepositoryInterface;

import java.util.List;

public class UserViewModel extends ViewModel {
    private static volatile UserViewModel instance;
    //private final UserRepository userRepository;
    private MutableLiveData<List<GoogleMapApiClass.Result>> listOfRestaurant;
    UserRepositoryInterface mUserRepositoryInterface;

    public UserViewModel(UserRepositoryInterface userRepositoryInterface) {
        mUserRepositoryInterface = userRepositoryInterface;
    }

    /*public static UserViewModel getInstance() {
        UserViewModel result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance == null) {
                instance = new UserViewModel();
            }
            return instance;
        }
    }*/

    public CollectionReference getUsersCollection() {
        return mUserRepositoryInterface.getUsersCollection();

    }

    /*public FirebaseAuth getCurrentInstance() {
        return userRepository.getCurrentInstance();
    }*/

    public FirebaseUser getCurrentUser() {
        return mUserRepositoryInterface.getCurrentUser();
    }

    public Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    public Task<Void> signOut(Context context) {
        return mUserRepositoryInterface.signOut(context);
    }

    public Task<Void> deleteUser(Context context) {
        FirebaseAuth.getInstance().getCurrentUser().delete();
        // Delete the user account from the Auth
        return mUserRepositoryInterface.deleteUser(context).addOnCompleteListener(task -> {
            // Once done, delete the user data from Firestore
            mUserRepositoryInterface.deleteUserFromFirestore();
        });
    }


    public void createUser() {
        mUserRepositoryInterface.createUser();
    }


    public Task<Void> updateUsername(String username) {
        return mUserRepositoryInterface.updateUsername(username);
    }

    public void updateUIWithUserData(Context context, TextView userName,
                                     TextView userEmail, ImageView userPhoto) {
        if (isCurrentUserLogged()) {
            FirebaseUser user = getCurrentUser();
            if (user.getPhotoUrl() != null) {
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

    public List<User> getAllUserForNotificationPush() {
        return mUserRepositoryInterface.getAllUserForNotificationPush();
    }
}
