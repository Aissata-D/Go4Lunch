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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sitadigi.go4lunch.models.GoogleClass1;
import com.sitadigi.go4lunch.models.User;
//import com.google.firebase.firestore.CollectionReference;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.grpc.internal.LogExceptionRunnable;

public final class UserRepository {

    private static volatile UserRepository instance;
    String userRestoId = "restoIdCreated";
    User userToCreate;

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
    public CollectionReference getUsersCollection(){
    return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
}

    // Create User in Firestore
    public void createUser() {
       // String userRestoId = null;
        FirebaseUser user = getCurrentUser();
        if(user != null){
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String username = user.getDisplayName();
            String useremail = user.getEmail();
            String uid = user.getUid();
            userToCreate = new User(uid, username, useremail, urlPicture, userRestoId);

            DocumentReference userDocumentRef = getUsersCollection().document(uid);
            userDocumentRef.get().addOnSuccessListener(documentSnapshot -> {
                if(!documentSnapshot.exists()){
                    this.getUsersCollection().document(uid).set(userToCreate);
                }
            });
           /* userDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            //userRestoId = document.getString("userRestoId");
                           // userToCreate = new User(uid, username,useremail, urlPicture,userRestoId);
                            //getUsersCollection().document(uid).set(userToCreate);


                        } else {
                           // userRestoId = null;
                            userToCreate = new User(uid, username,useremail, urlPicture,userRestoId);
                            getUsersCollection().document(uid).set(userToCreate);

                            }
                    } else {
                        Log.d("LOGGER", "get failed with ", task.getException());
                    }
                }
            });

            */
        }// TODO Gerer les cas d'erreur
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
    /*
    *getAllUser method return users using this app in real time
     */
    public MutableLiveData<List<User>> getAllUser() {
        List<User> users =new ArrayList<>();
        MutableLiveData<List<User>> listOfUserLiveData = new MutableLiveData<>();

        List<User> usersUsingApp = new ArrayList<>();
        User userToGet;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

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

                                        User userToGet = new User(uid, username, email, urlPicture,restoId);
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

   /* public MutableLiveData<String> getAllUserRestoId() {
        MutableLiveData<List<User>> users = getAllUser();
        for(User user : getAllUser()){

        }
    }*/
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

