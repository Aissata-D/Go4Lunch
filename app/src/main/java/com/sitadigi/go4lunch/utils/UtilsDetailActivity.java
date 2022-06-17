package com.sitadigi.go4lunch.utils;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.sitadigi.go4lunch.R;
import com.sitadigi.go4lunch.models.RestaurantLike;
import com.sitadigi.go4lunch.ui.workmaters.WorkmateViewModel;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

public class UtilsDetailActivity {

    private static final String COLLECTION_RESTAURANT_LIKE_NAME = "restaurantLike";
    Activity mActivity;
    String mUserLastRestaurantId;
    String restaurantId;
    LifecycleOwner lifecycleOwner;
    RatingBar restaurantRatingBar;
    WorkmateViewModel mWorkmateViewModel;
    UserViewModel mUserViewModel;


    public UtilsDetailActivity(Activity activity, String restaurantId, String userLastRestaurantId,
                               LifecycleOwner lifecycleOwner, RatingBar restaurantRatingBar,
                               WorkmateViewModel mWorkmateViewModel, UserViewModel mUserViewModel) {
        mActivity = activity;
        this.restaurantId = restaurantId;
        mUserLastRestaurantId = userLastRestaurantId;
        this.lifecycleOwner = lifecycleOwner;
        this.restaurantRatingBar = restaurantRatingBar;
        this.mWorkmateViewModel = mWorkmateViewModel;
        this.mUserViewModel = mUserViewModel;
    }
    public void setRatingIcon1(RatingBar restaurantRatingBar, float rating) {
        restaurantRatingBar.setRating(rating);
    }

    public void setIconStarColor(TextView tvRestaurantLike, UserViewModel mUserViewModel) {

        String userUid = mUserViewModel.getCurrentUser().getUid();
        DocumentReference userRestaurantLikeRef = mUserViewModel.getUsersCollection().document(userUid)
                .collection(COLLECTION_RESTAURANT_LIKE_NAME).document(restaurantId);
        userRestaurantLikeRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        tvRestaurantLike.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_star_24_yellow, 0, 0);
                    } else {
                        tvRestaurantLike.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_star_24_gray, 0, 0);
                    }
                }
            }
        });
    }

    public void setfabColor(FloatingActionButton fbaRestoChoice, UserViewModel mUserViewModel) {
        // Get uid of user on logged
        String userUid = mUserViewModel.getCurrentUser().getUid();
        // Get userRestoId on firebaseFirestore
        DocumentReference userDocumentRef = mUserViewModel.getUsersCollection().document(userUid);
        userDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        mUserLastRestaurantId = document.getString("userRestoId");
                        if (mUserLastRestaurantId.equals(restaurantId))// It is a same restaurant
                        {//Set fab icon color gray
                            fbaRestoChoice.setImageTintList(ColorStateList.valueOf(mActivity.getApplicationContext()
                                    .getResources().getColor(R.color.fab_green)));
                        } else {//It is not a same restaurant
                            //Set fab icon color green
                            fbaRestoChoice.setImageTintList(ColorStateList.valueOf(mActivity.getApplicationContext()
                                    .getResources().getColor(R.color.fab_gray)));
                        }
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });
    }

    public void clickOnButtonLike(String userUid, TextView tvRestoLike, String restoName
            , UserViewModel mUserViewModel) {
        // Get userRestoLike on firebaseFirestore
        DocumentReference userRestaurantLikeRef = mUserViewModel.getUsersCollection().document(userUid)
                .collection(COLLECTION_RESTAURANT_LIKE_NAME).document(restaurantId);
        userRestaurantLikeRef.get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) {
                RestaurantLike restaurantLike = new RestaurantLike(restoName);
                mUserViewModel.getUsersCollection().document(userUid)
                        .collection(COLLECTION_RESTAURANT_LIKE_NAME).document(restaurantId).set(restaurantLike);
                // set color of like button and set ratingBar
                tvRestoLike.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_star_24_yellow, 0, 0);
            } else {
                //  delete document
                userRestaurantLikeRef.delete();
                // set color of like button and set ratingBar
                tvRestoLike.setCompoundDrawablesWithIntrinsicBounds(0,
                        R.drawable.ic_baseline_star_24_gray, 0, 0);
            }
        });
    }

    public void clickOnButtonFab(UserViewModel mUserViewModel, String userUid, FloatingActionButton fbaRestoChoice
            , String restoName, String restoType) {
        // Get userRestoId on firebaseFirestore
        DocumentReference userDocumentRef = mUserViewModel.getUsersCollection().document(userUid);
        userDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        mUserLastRestaurantId = document.getString("userRestoId");
                        if (mUserLastRestaurantId == null) {
                            //Set fab icon color green
                            fbaRestoChoice.setImageTintList(ColorStateList.valueOf(mActivity.getApplicationContext()
                                    .getResources().getColor(R.color.fab_green)));
                            //Set restoId with actual restoIdChoice
                            userDocumentRef.update("userRestoId", restaurantId);
                            userDocumentRef.update("userRestoName", restoName);
                            userDocumentRef.update("userRestoType", restoType);
                        } else {//restoId != null
                            if (mUserLastRestaurantId.equals(restaurantId)) { // It is a same restaurant)
                                //Set fab icon color gray
                                fbaRestoChoice.setImageTintList(ColorStateList.valueOf(mActivity.getApplicationContext()
                                        .getResources().getColor(R.color.fab_gray)));
                                // usersInter.remove(firebaseUser);
                                userDocumentRef.update("userRestoId", "NoRestoChoice");
                                userDocumentRef.update("userRestoName", "restoNameCreated");
                                userDocumentRef.update("userRestoType", "restoTypeCreated");
                            } else {//It is not a same restaurant
                                //Set fab icon color green
                                fbaRestoChoice.setImageTintList(ColorStateList.valueOf(mActivity.getApplicationContext()
                                        .getResources().getColor(R.color.fab_green)));
                                userDocumentRef.update("userRestoId", restaurantId);
                                userDocumentRef.update("userRestoName", restoName);
                                userDocumentRef.update("userRestoType", restoType);
                            }
                        }
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });
    }
}
