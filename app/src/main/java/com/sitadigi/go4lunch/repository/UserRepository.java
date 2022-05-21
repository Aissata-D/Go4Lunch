package com.sitadigi.go4lunch.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sitadigi.go4lunch.models.User;
//import com.google.firebase.firestore.CollectionReference;

import org.jetbrains.annotations.Nullable;

public final class UserRepository {

    private static volatile UserRepository instance;

    //----FIRESTORE FIELD-------------
    private static final String COLLECTION_NAME = "users";
    private static final String USERNAME_FIELD = "username";

    private UserRepository() { }

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized(UserRepository.class) {
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
    public FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser();
    }

    public Task<Void> signOut(Context context){
        return AuthUI.getInstance().signOut(context);
    }

    public Task<Void> deleteUser(Context context){ return AuthUI.getInstance().delete(context);}


//----------------------FIRESTORE---------------------------------
// Get the Collection Reference
    private CollectionReference getUsersCollection(){
    return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
}

    // Create User in Firestore
    public void createUser() {
        FirebaseUser user = getCurrentUser();
        if(user != null){
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String username = user.getDisplayName();
            String useremail = user.getEmail();
            String uid = user.getUid();

            User userToCreate = new User(uid, username,useremail, urlPicture);

            this.getUsersCollection().document(uid).set(userToCreate);

        }// Gerer les cas d'erreur
        else{ }
    }
    // Get UID of current user
    private String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null)? user.getUid():null;
    }

    // Update User Username
    public Task<Void> updateUsername(String username) {
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid).update(USERNAME_FIELD, username);
        }else{
            return null;
        }
    }
    // Delete the User from Firestore
    public void deleteUserFromFirestore() {
        String uid = this.getCurrentUserUID();
        if(uid != null){
            this.getUsersCollection().document(uid).delete();
        }
    }

    // Get User Data from Firestore// Return the document  of user
   /* public Task<DocumentSnapshot> getUserData(){
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid).get();
        }else{
            return null;
        }
    }*/

}

