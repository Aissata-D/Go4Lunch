package com.sitadigi.go4lunch.repository;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.internal.zzwe;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.MultiFactor;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sitadigi.go4lunch.models.User;

import java.util.ArrayList;
import java.util.List;

public class FakeUserRepository implements UserRepositoryInterface{
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore mFirebaseFirestore;
    AuthUI mAuthUI;
    String userRestaurantId = "restaurantIdCreated";
    String userRestaurantName = "restaurantNameCreated";
    String userRestaurantType = "restaurantTypeCreated";
    User userToCreate;

    public FakeUserRepository(FirebaseAuth firebaseAuth, FirebaseFirestore firebaseFirestore, AuthUI authUI){
        mFirebaseAuth = firebaseAuth;
        mFirebaseFirestore = firebaseFirestore;
        mAuthUI = authUI;

    }


    @Nullable
    @Override
    public FirebaseAuth getCurrentInstance() {
        return mFirebaseAuth ;
    }

    @Nullable
    @Override
    public FirebaseUser getCurrentUser() {
        User user1 = new User("userid","username","usermail@mail.com","userPictureUrl");
        //return  user1;
        return mFirebaseAuth.getCurrentUser();
    }

    @Override
    public Task<Void> signOut(Context context) {
        return mAuthUI.signOut(context);
    }

    @Override
    public Task<Void> deleteUser(Context context) {
        return mAuthUI.delete(context);
    }

    @Override
    public CollectionReference getUsersCollection() {

        return mFirebaseFirestore.collection("users");

    }

    @Override
    public void createUser() {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String username = user.getDisplayName();
            String useremail = user.getEmail();
            String uid = user.getUid();
            userToCreate = new User(uid, username, useremail, urlPicture, userRestaurantId
                    , userRestaurantName, userRestaurantType);
            DocumentReference userDocumentRef = getUsersCollection().document(uid);
            userDocumentRef.get().addOnSuccessListener(documentSnapshot -> {
                if(!documentSnapshot.exists()){
                    this.getUsersCollection().document(uid).set(userToCreate);
                }
            });

        }
        else {
        }

    }

    @Override
    public String getCurrentUserUID() {

        String currentUserId = "currentUserId";
        return currentUserId;
    }

    @Override
    public Task<Void> updateUsername(String username) {
       // return this.getUsersCollection().document(uid).update(USERNAME_FIELD, username);
        return null;

    }

    @Override
    public void deleteUserFromFirestore() {

    }

    @Override
    public MutableLiveData<List<User>> getAllUser() {
        return null;
    }

    @Override
    public List<User> getAllUserForNotificationPush() {
        List<User> users = new ArrayList<User>();
        User user1 = new User("uid1","user1","email1","urlPicture1",
                "restaurantId1","restaurantName1","restaurantType1");
        User user2 = new User("uid2","user2","email2","urlPicture2",
                "restaurantId2","restaurantName2","restaurantType2");
        users.add(user1);
        users.add(user2);
        return users;
    }

}
