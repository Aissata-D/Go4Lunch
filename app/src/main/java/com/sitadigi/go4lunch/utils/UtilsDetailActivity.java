package com.sitadigi.go4lunch.utils;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.sitadigi.go4lunch.DetailActivity;
import com.sitadigi.go4lunch.R;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.RestaurantLike;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.repository.UserRepository;
import com.sitadigi.go4lunch.ui.workmaters.WorkmateViewModel;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class UtilsDetailActivity {

    private static final String COLLECTION_RESTAURANT_LIKE_NAME = "restaurantLike";
    Activity mActivity;
    List<DocumentReference> eventUnique = new ArrayList<>();
    String mUserLastRestoId;
    String restoId;
    float numberOfUser;
    float numberOfLike;
    float rating;
    List<User> users;
    LifecycleOwner lifecycleOwner;
    RatingBar restaurantRatingBar;
    WorkmateViewModel mWorkmateViewModel;
    UserViewModel mUserViewModel;


    public UtilsDetailActivity(Activity activity, String restoId, String userLastRestoId,
                               LifecycleOwner lifecycleOwner, RatingBar restaurantRatingBar,
                               WorkmateViewModel mWorkmateViewModel, UserViewModel mUserViewModel) {
        mActivity = activity;
        this.restoId = restoId;
        mUserLastRestoId = userLastRestoId;
        this.lifecycleOwner = lifecycleOwner;
        this.restaurantRatingBar = restaurantRatingBar;
        this.mWorkmateViewModel = mWorkmateViewModel;
        this.mUserViewModel = mUserViewModel;
    }

    public void setRatingIcon() {
        Log.e("NEW", "setRatingIcon: ENTRE");
        numberOfUser = 0;
        numberOfLike = 0;
        rating = 0;

        mWorkmateViewModel.getAllUser().observe(lifecycleOwner, usersLiveData -> {
            users = new ArrayList<>();
            Log.e("NEW", "setRatingIcon: OBSERVE");
            //numberOfUser = usersLiveData.size();
            users.clear();
            for (User user : usersLiveData) {
                if (!users.contains(user)) {
                    users.add(user);
                }
            }

            numberOfUser = 0;
            numberOfLike = 0;
            rating = 0;
            // });
            Log.e("NEW", "setRatingIcon: userLiveData"/* +usersLiveData.size()*/);
            numberOfUser = users.size();
            Log.e("NEW", "setRatingIcon: numberofUser" + numberOfUser);
            for (int i = 0; i < users.size(); i++) {
                Log.e("NEW", "setRatingIcon: FORR" + i);
                String Uid = users.get(i).getUid();
                DocumentReference userRestaurantLikeRef = mUserViewModel.getUsersCollection()
                        .document(Uid).collection(COLLECTION_RESTAURANT_LIKE_NAME).document(restoId);
                userRestaurantLikeRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        if (!eventUnique.contains(userRestaurantLikeRef)) {
                            Log.e("NEW", "SUCCES userRestaurantLikeRef "
                                    + userRestaurantLikeRef);

                            eventUnique.add(userRestaurantLikeRef);
                            numberOfLike = numberOfLike + 1;
                            rating = (float) ((numberOfLike / (numberOfUser + 0.10)) * 5);
                            if (rating == 0) {
                                Toast.makeText(mActivity, mActivity.getString(R.string.no_like_for_this_restaurant), Toast.LENGTH_SHORT).show();
                            }
                            restaurantRatingBar.setRating(rating);

                            Log.e("NEW", "SUCCES setIconStarColor: rating " + rating + " numberofuser " + numberOfUser
                                    + " numberLike " + numberOfLike + " restoId " + restoId
                                    + " userId " + Uid);
                        }
                    } else {
                        restaurantRatingBar.setRating(rating);
                        Log.e("NEW", " ELSE setIconStarColor: N EXISTE PAS rating " + rating
                                + " restoId " + restoId
                                + " userId " + Uid);
                    }
                });
            }
        });
    }

    public void setIconStarColor(TextView tvRestoLike, UserViewModel mUserViewModel) {

        String userUid = mUserViewModel.getCurrentUser().getUid();

        DocumentReference userRestaurantLikeRef = mUserViewModel.getUsersCollection().document(userUid)
                .collection(COLLECTION_RESTAURANT_LIKE_NAME).document(restoId);
        userRestaurantLikeRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        tvRestoLike.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_star_24_yellow, 0, 0);

                    } else {
                        tvRestoLike.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_star_24_gray, 0, 0);
                        if (eventUnique.contains(userRestaurantLikeRef)) {
                            eventUnique.remove(userRestaurantLikeRef);
                        }
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
                        mUserLastRestoId = document.getString("userRestoId");
                        if (mUserLastRestoId.equals(restoId))// It is a same restaurant
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
                .collection(COLLECTION_RESTAURANT_LIKE_NAME).document(restoId);
        userRestaurantLikeRef.get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) {
                RestaurantLike restaurantLike = new RestaurantLike(restoName);
                mUserViewModel.getUsersCollection().document(userUid)
                        .collection(COLLECTION_RESTAURANT_LIKE_NAME).document(restoId).set(restaurantLike);
                // set color of like button and set ratingBar
                tvRestoLike.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_star_24_yellow, 0, 0);

                // if(eventUnique.contains(userRestaurantLikeRef)) {
                eventUnique.remove(userRestaurantLikeRef);
                Log.e("NEW", "CLICK IF userRestaurantLikeRef "
                        + userRestaurantLikeRef);
                setRatingIcon();
                // }
            } else {
                //  delete document
                userRestaurantLikeRef.delete();
                // set color of like button and set ratingBar
                tvRestoLike.setCompoundDrawablesWithIntrinsicBounds(0,
                        R.drawable.ic_baseline_star_24_gray, 0, 0);

                setRatingIcon();
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
                        mUserLastRestoId = document.getString("userRestoId");
                        if (mUserLastRestoId == null) {
                            //Set fab icon color green
                            fbaRestoChoice.setImageTintList(ColorStateList.valueOf(mActivity.getApplicationContext()
                                    .getResources().getColor(R.color.fab_green)));
                            //Set restoId with actual restoIdChoice
                            userDocumentRef.update("userRestoId", restoId);
                            userDocumentRef.update("userRestoName", restoName);
                            userDocumentRef.update("userRestoType", restoType);
                        } else {//restoId != null
                            if (mUserLastRestoId.equals(restoId)) { // It is a same restaurant)
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
                                userDocumentRef.update("userRestoId", restoId);
                                userDocumentRef.update("userRestoName", restoName);
                                userDocumentRef.update("userRestoType", restoType);
                            }
                        }
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
                //initRecyclerView();
            }
        });
    }
}
