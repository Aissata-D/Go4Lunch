package com.sitadigi.go4lunch.viewModel;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.sitadigi.go4lunch.R;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.repository.UserRepositoryInterface;

import java.util.List;

public class UserViewModel extends ViewModel {
    private static volatile UserViewModel instance;
    UserRepositoryInterface mUserRepositoryInterface;
    //private final UserRepository userRepository;
    private MutableLiveData<List<GoogleMapApiClass.Result>> listOfRestaurant;

    public UserViewModel(UserRepositoryInterface userRepositoryInterface) {
        mUserRepositoryInterface = userRepositoryInterface;
    }

    public CollectionReference getUsersCollection() {
        return mUserRepositoryInterface.getUsersCollection();

    }

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
        mUserRepositoryInterface.deleteUserFromFirestore(); // Delete the user account from the Firestore
        return mUserRepositoryInterface.deleteUser(context); // Delete the user account from the Auth
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
