package com.sitadigi.go4lunch.repository;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sitadigi.go4lunch.models.User;

import java.util.ArrayList;
import java.util.List;

public class FakeUserRepository implements UserRepositoryInterface{


    @Nullable
    @Override
    public FirebaseAuth getCurrentInstance() {
        return FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public FirebaseUser getCurrentUser() {
        User user1 = new User("userid","username","usermail@mail.com","userPictureUrl");
        //return  user1;
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

    @Override
    public CollectionReference getUsersCollection() {
       // return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
            return null;
    }

    @Override
    public void createUser() {

    }

    @Override
    public String getCurrentUserUID() {
      //  return (user != null) ? user.getUid() : null;
        return null;
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
        User user1 = new User("userid","username","usermail@mail.com","userPictureUrl");
                List<User> users = new ArrayList<>();
                users.add(user1);
        return users;
    }
}
