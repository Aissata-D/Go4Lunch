package com.sitadigi.go4lunch.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sitadigi.go4lunch.models.User;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class UserRepository implements UserRepositoryInterface {

    //----FIRESTORE FIELD-------------
    private static final String COLLECTION_NAME = "users";
    private static final String USERNAME_FIELD = "username";
    private static volatile UserRepository instance;
    String userRestaurantId = "restaurantIdCreated";
    String userRestaurantName = "restaurantNameCreated";
    String userRestaurantType = "restaurantTypeCreated";
    User userToCreate;

    public UserRepository() {
    }

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance == null) {
                instance = new UserRepository();
            }
            return instance;
        }
    }

    @Override
    @Nullable
    public FirebaseAuth getCurrentInstance() {
        return FirebaseAuth.getInstance();
    }

    @Override
    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public Task<Void> signOut(Context context) {
        return AuthUI.getInstance().signOut(context);
    }

    @Override
    public Task<Void> deleteUser(Context context) {
        return AuthUI.getInstance().delete(context);
    }


    //----------------------FIRESTORE---------------------------------
    // Get the Collection Reference
    @Override
    public CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Create User in Firestore
    @Override
    public void createUser() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String username = user.getDisplayName();
            String useremail = user.getEmail();
            String uid = user.getUid();
            userToCreate = new User(uid, username, useremail, urlPicture, userRestaurantId
                    , userRestaurantName, userRestaurantType);
            DocumentReference userDocumentRef = getUsersCollection().document(uid);
            userDocumentRef.get().addOnSuccessListener(documentSnapshot -> {
                if (!documentSnapshot.exists()) {
                    this.getUsersCollection().document(uid).set(userToCreate);
                }
            });

        } else {
            // --> (user == null) ==> do nothing
        }
    }

    // Get UID of current user
    @Override
    public String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    // Update User Username
    @Override
    public Task<Void> updateUsername(String username) {
        FirebaseUser user = getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "User profile updated.");
                        }
                    }
                });
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            return this.getUsersCollection().document(uid).update(USERNAME_FIELD, username);
        } else {
            return null;
        }
    }

    // Delete the User from Firestore
    @Override
    public void deleteUserFromFirestore() {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            this.getUsersCollection().document(uid).delete();
        }
    }

    /*
     * get AllUser method return users using this app in real time
     */
    @Override
    public MutableLiveData<List<User>> getAllUser() {

        MutableLiveData<List<User>> listOfUserLiveData = new MutableLiveData<>();
        List<User> usersUsingApp = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").addSnapshotListener(
                new EventListener<QuerySnapshot>() {

                    @Override
                    public void onEvent(
                            @androidx.annotation.Nullable QuerySnapshot value, @androidx.annotation.Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            System.err.println("Listen failed:" + error);
                            return;
                        }
                        if (value != null) {
                            usersUsingApp.clear();
                            for (DocumentSnapshot document : value) {
                                String username = document.getString("username");
                                String email = document.getString("email");
                                String urlPicture = document.getString("urlPicture");
                                String uid = document.getId();
                                String restaurantId = document.getString("userRestaurantId");
                                String restaurantName = document.getString("userRestaurantName");
                                String restaurantType = document.getString("userRestaurantType");
                                // Get user
                                User userToGet = new User(uid, username, email, urlPicture, restaurantId, restaurantName, restaurantType);
                                usersUsingApp.add(userToGet);
                                listOfUserLiveData.postValue(usersUsingApp);

                            }
                        }
                    }
                });
        Log.e("NEW", "getAllUser:dans repository " + listOfUserLiveData);
        return listOfUserLiveData;
    }

    @Override
    public List<User> getAllUserForNotificationPush() {

        List<User> usersUsingApp1 = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").addSnapshotListener(
                new EventListener<QuerySnapshot>() {

                    @Override
                    public void onEvent(
                            @androidx.annotation.Nullable QuerySnapshot value, @androidx.annotation.Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            System.err.println("Listen failed:" + error);
                            return;
                        }
                        if (value != null) {
                            List<User> usersUsingApp = new ArrayList<>();
                            for (DocumentSnapshot document : value) {
                                String username = document.getString("username");
                                String email = document.getString("email");
                                String urlPicture = document.getString("urlPicture");
                                String uid = document.getId();
                                String restaurantId = document.getString("userRestaurantId");
                                String restaurantName = document.getString("userRestaurantName");
                                String restaurantType = document.getString("userRestaurantType");

                                User userToGet = new User(uid, username, email, urlPicture, restaurantId, restaurantName, restaurantType);
                                if (!usersUsingApp.contains(userToGet)) {
                                    usersUsingApp.add(userToGet);
                                }
                                usersUsingApp1.clear();
                                usersUsingApp1.addAll(usersUsingApp);
                            }
                        }
                    }
                });
        return usersUsingApp1;
    }
}

