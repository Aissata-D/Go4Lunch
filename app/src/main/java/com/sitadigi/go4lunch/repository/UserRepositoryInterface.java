package com.sitadigi.go4lunch.repository;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.sitadigi.go4lunch.models.User;

import org.jetbrains.annotations.Nullable;
import java.util.List;

public interface UserRepositoryInterface {

    @Nullable
    public FirebaseAuth getCurrentInstance();

    @Nullable
    public FirebaseUser getCurrentUser();

    public Task<Void> signOut(Context context);

    public Task<Void> deleteUser(Context context);

    //----------------------FIRESTORE---------------------------------
    // Get the Collection Reference
    public CollectionReference getUsersCollection();

    // Create User in Firestore
    public void createUser();

    // Get UID of current user
    public String getCurrentUserUID();

    // Update User Username
    public Task<Void> updateUsername(String username);

    // Delete the User from Firestore
    public void deleteUserFromFirestore();

    /*
     *getAllUser method return users using this app in real time
     */
    public MutableLiveData<List<User>> getAllUser() ;


    public List<User> getAllUserForNotificationPush() ;
}
