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
import com.sitadigi.go4lunch.viewModel.MainViewViewModel;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

public class UtilsDetailActivity {

    private static final String COLLECTION_RESTAURANT_LIKE_NAME = "restaurantLike";
    Activity mActivity;
    String mUserLastRestaurantId;
    String restaurantId;

    public UtilsDetailActivity(Activity activity, String restaurantId, String userLastRestaurantId) {
        mActivity = activity;
        this.restaurantId = restaurantId;
        mUserLastRestaurantId = userLastRestaurantId;

    }

    public void setRatingIcon(RatingBar restaurantRatingBar, float rating) {
        double rating1 = (rating * 0.6);
        float rating2 = (float) rating1;
        restaurantRatingBar.setRating(rating2);
        Log.e("RATING", "setRatingIcon: " + rating2);
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

    public void setFabColor(FloatingActionButton fbaRestaurantChoice, UserViewModel mUserViewModel) {
        // Get uid of user on logged
        String userUid = mUserViewModel.getCurrentUser().getUid();
        // Get userRestaurantId on firebaseFirestore
        DocumentReference userDocumentRef = mUserViewModel.getUsersCollection().document(userUid);
        userDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        mUserLastRestaurantId = document.getString("userRestaurantId");
                        if (mUserLastRestaurantId.equals(restaurantId))// It is a same restaurant
                        {//Set fab icon color gray
                            fbaRestaurantChoice.setImageTintList(ColorStateList.valueOf(mActivity.getApplicationContext()
                                    .getResources().getColor(R.color.fab_green)));
                        } else {//It is not a same restaurant
                            //Set fab icon color green
                            fbaRestaurantChoice.setImageTintList(ColorStateList.valueOf(mActivity.getApplicationContext()
                                    .getResources().getColor(R.color.fab_gray)));
                        }
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });
    }

    public void clickOnButtonLike(String userUid, TextView tvRestaurantLike, String restaurantName
            , UserViewModel mUserViewModel) {
        // Get userRestaurantLike on firebaseFirestore
        DocumentReference userRestaurantLikeRef = mUserViewModel.getUsersCollection().document(userUid)
                .collection(COLLECTION_RESTAURANT_LIKE_NAME).document(restaurantId);
        userRestaurantLikeRef.get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) {
                RestaurantLike restaurantLike = new RestaurantLike(restaurantName);
                mUserViewModel.getUsersCollection().document(userUid)
                        .collection(COLLECTION_RESTAURANT_LIKE_NAME).document(restaurantId).set(restaurantLike);
                // set color of like button and set ratingBar
                tvRestaurantLike.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_star_24_yellow, 0, 0);
            } else {
                //  delete document
                userRestaurantLikeRef.delete();
                // set color of like button and set ratingBar
                tvRestaurantLike.setCompoundDrawablesWithIntrinsicBounds(0,
                        R.drawable.ic_baseline_star_24_gray, 0, 0);
            }
        });
    }

    public void clickOnButtonFab(UserViewModel mUserViewModel, String userUid, FloatingActionButton fbaRestaurantChoice
            , String restaurantName, String restaurantType) {
        // Get userRestaurantId on firebaseFirestore
        DocumentReference userDocumentRef = mUserViewModel.getUsersCollection().document(userUid);
        userDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        mUserLastRestaurantId = document.getString("userRestaurantId");
                        if (mUserLastRestaurantId == null) {
                            //Set fab icon color green
                            fbaRestaurantChoice.setImageTintList(ColorStateList.valueOf(mActivity.getApplicationContext()
                                    .getResources().getColor(R.color.fab_green)));
                            //Set restaurantId with actual restaurantIdChoice
                            userDocumentRef.update("userRestaurantId", restaurantId);
                            userDocumentRef.update("userRestaurantName", restaurantName);
                            userDocumentRef.update("userRestaurantType", restaurantType);
                        } else {//restaurantId != null
                            if (mUserLastRestaurantId.equals(restaurantId)) { // It is a same restaurant)
                                //Set fab icon color gray
                                fbaRestaurantChoice.setImageTintList(ColorStateList.valueOf(mActivity.getApplicationContext()
                                        .getResources().getColor(R.color.fab_gray)));
                                userDocumentRef.update("userRestaurantId", "restaurantIdCreated");
                                userDocumentRef.update("userRestaurantName", "restaurantNameCreated");
                                userDocumentRef.update("userRestaurantType", "restaurantTypeCreated");
                            } else {//It is not a same restaurant
                                //Set fab icon color green
                                fbaRestaurantChoice.setImageTintList(ColorStateList.valueOf(mActivity.getApplicationContext()
                                        .getResources().getColor(R.color.fab_green)));
                                userDocumentRef.update("userRestaurantId", restaurantId);
                                userDocumentRef.update("userRestaurantName", restaurantName);
                                userDocumentRef.update("userRestaurantType", restaurantType);
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
