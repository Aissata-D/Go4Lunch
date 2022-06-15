package com.sitadigi.go4lunch.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public final class UserRepository {

    //----FIRESTORE FIELD-------------
    private static final String COLLECTION_NAME = "users";
    private static final String USERNAME_FIELD = "username";
    private static volatile UserRepository instance;
    String userRestoId = "restoIdCreated";
    String userRestoName = "restoNameCreated";
    String userRestoType = "restoTypeCreated";
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

    @Nullable
    public FirebaseAuth getCurrentInstance() {
        return FirebaseAuth.getInstance();
    }

    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public Task<Void> signOut(Context context) {
        return AuthUI.getInstance().signOut(context);
    }

    public Task<Void> deleteUser(Context context) {
        return AuthUI.getInstance().delete(context);
    }


    //----------------------FIRESTORE---------------------------------
// Get the Collection Reference
    public CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Create User in Firestore
    public void createUser() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String username = user.getDisplayName();
            String useremail = user.getEmail();
            String uid = user.getUid();
            userToCreate = new User(uid, username, useremail, urlPicture, userRestoId, userRestoName, userRestoType);
            DocumentReference userDocumentRef = getUsersCollection().document(uid);
            userDocumentRef.get().addOnSuccessListener(documentSnapshot -> {
                if (!documentSnapshot.exists()) {
                    this.getUsersCollection().document(uid).set(userToCreate);
                }
            });
        }// TODO Gerer les cas d'erreur
        else {
        }
    }

    // Get UID of current user
    private String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    // Update User Username
    public Task<Void> updateUsername(String username) {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            return this.getUsersCollection().document(uid).update(USERNAME_FIELD, username);
        } else {
            return null;
        }
    }

    // Delete the User from Firestore
    public void deleteUserFromFirestore() {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            this.getUsersCollection().document(uid).delete();
        }
    }

    /*
     *getAllUser method return users using this app in real time
     */
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
                            for (DocumentSnapshot document : value) {
                                String username = document.getString("username");
                                String email = document.getString("email");
                                String urlPicture = document.getString("urlPicture");
                                String uid = document.getId();
                                String restoId = document.getString("userRestoId");
                                String restoName = document.getString("userRestoName");
                                String restoType = document.getString("userRestoType");

                                User userToGet = new User(uid, username, email, urlPicture, restoId, restoName, restoType);
                                if (!usersUsingApp.contains(userToGet)) {
                                    usersUsingApp.add(userToGet);
                                }
                                listOfUserLiveData.setValue(usersUsingApp);
                                Log.e("TAG", "onEvent: " + userToGet.getUsername());
                            }
                        }
                    }
                });
        return listOfUserLiveData;
    }

    public List<User> getAllUserForNotificationPush() {

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
                            for (DocumentSnapshot document : value) {
                                String username = document.getString("username");
                                String email = document.getString("email");
                                String urlPicture = document.getString("urlPicture");
                                String uid = document.getId();
                                String restoId = document.getString("userRestoId");
                                String restoName = document.getString("userRestoName");
                                String restoType = document.getString("userRestoType");

                                User userToGet = new User(uid, username, email, urlPicture, restoId, restoName, restoType);
                                if (!usersUsingApp.contains(userToGet)) {
                                    usersUsingApp.add(userToGet);
                                }
                               // listOfUserLiveData.setValue(usersUsingApp);
                                Log.e("TAG", "onEvent: " + userToGet.getUsername());
                            }
                        }
                    }
                });
        return usersUsingApp;
    }
}

