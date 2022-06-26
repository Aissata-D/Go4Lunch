package com.sitadigi.go4lunch.utils;


import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_ADDRESS;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_ID;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_NAME;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_OPENINGHOURS;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_PHONE_NUMBER;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_PHOTO_URL;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_RATING;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_TYPE;
import static com.sitadigi.go4lunch.DetailActivity.RESTAURANT_WEBSITE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.sitadigi.go4lunch.DetailActivity;
import com.sitadigi.go4lunch.LoginActivity;
import com.sitadigi.go4lunch.R;
import com.sitadigi.go4lunch.factory.UserViewModelFactory;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.repository.UserRepository;
import com.sitadigi.go4lunch.viewModel.MainViewViewModel;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

import java.util.ArrayList;
import java.util.List;


public class ShowSignOutDialogueAlertAndDetailActivity {

    private UserViewModel mUserViewModel;

    public ShowSignOutDialogueAlertAndDetailActivity() {

        UserRepository userRepository = new UserRepository();
        mUserViewModel = new UserViewModel(userRepository);
    }

    public void showAlertDialogSinOut(Context mContext) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("SIGN OUT")
                .setMessage(mContext.getString(R.string.sign_out_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mUserViewModel.signOut(mContext)
                                // after sign out is executed we are redirecting on LoginActivity
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // below method is used after logout from device.
                                        Toast.makeText(mContext, "User Signed Out", Toast.LENGTH_SHORT).show();
                                        // Return to LoginActivity via an intent.
                                        Intent i = new Intent(mContext, LoginActivity.class);
                                        mContext.startActivity(i);
                                    }
                                });
                    }

                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    public void showAlertDialogDeleteAcount(Context mContext) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Delete Account")
                .setMessage(mContext.getString(R.string.sign_out_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mUserViewModel.deleteUser(mContext)
                                // after sign out is executed we are redirecting on LoginActivity
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // below method is used after logout from device.
                                        Toast.makeText(mContext, "Your Account is delete", Toast.LENGTH_SHORT).show();
                                        // Return to LoginActivity via an intent.
                                        Intent i = new Intent(mContext, LoginActivity.class);
                                        mContext.startActivity(i);
                                    }
                                });
                    }

                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    public void showDetailActivity(MainViewViewModel mainViewViewModel, Activity activity, LifecycleOwner lifecycleOwner) {
        Log.e("TAG", "showDetailActivity: ATTEINT");
        final String[] userLastRestaurantId = {""};
        final String[] userRestaurantName = {""};
        List<GoogleMapApiClass.Result> resultList = new ArrayList<>();
        List<GoogleMapApiClass.Result> listOfRestaurant = new ArrayList<>();

        mainViewViewModel.getRestaurant().observe(lifecycleOwner, RestaurantResponse -> {
            listOfRestaurant.clear();
            listOfRestaurant.addAll(RestaurantResponse);
            //  When getting response, we update UI
            for (GoogleMapApiClass.Result restaurant : listOfRestaurant) {
                if (!resultList.contains(restaurant)) {
                    resultList.add(restaurant);
                }
            }
            String userUid = mUserViewModel.getCurrentUser().getUid();
            // Get userRestaurantId on firebaseFirestore
            DocumentReference userDocumentRef = mUserViewModel.getUsersCollection().document(userUid);
            userDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            userLastRestaurantId[0] = document.getString("userRestaurantId");
                            userRestaurantName[0] = document.getString("userRestaurantName");

                            if ((!userLastRestaurantId[0].equals("restaurantIdCreated"))
                                    && (!userRestaurantName[0].equals("restaurantNameCreated"))) {
                                for (GoogleMapApiClass.Result restaurant : resultList) {
                                    String restaurantName = restaurant.getName();
                                    String restaurantId = restaurant.getPlaceId();
                                    if ((restaurantName.equals(userRestaurantName[0])) && (restaurantId.equals(userLastRestaurantId[0]))) {
                                        Intent intentDetail = new Intent(activity, DetailActivity.class);
                                        intentDetail.putExtra(RESTAURANT_ID, restaurant.getPlaceId());
                                        intentDetail.putExtra(RESTAURANT_NAME, restaurantName);
                                        if ((restaurant.getOpeningHours() != null) && (restaurant.getOpeningHours().getOpenNow()) != null) {
                                            intentDetail.putExtra(RESTAURANT_OPENINGHOURS, restaurant.getOpeningHours().getOpenNow());
                                        }
                                        if (restaurant.getPhotos() != null) {
                                            if (restaurant.getPhotos().get(0).getPhotoReference() != null) {
                                                intentDetail.putExtra(RESTAURANT_PHOTO_URL,
                                                        restaurant.getPhotos().get(0).getPhotoReference());
                                            }
                                        }
                                        if ((restaurant.getTypes()) != null && (restaurant.getTypes().get(0)) != null) {
                                            String restaurantAddress = restaurant.getVicinity();
                                            intentDetail.putExtra(RESTAURANT_ADDRESS, restaurantAddress);
                                            String restaurantType = restaurant.getTypes().get(0);
                                            intentDetail.putExtra(RESTAURANT_TYPE, restaurantType);
                                        }
                                        if (restaurant.getRating() != null) {
                                            float rating = restaurant.getRating().floatValue();
                                            intentDetail.putExtra(RESTAURANT_RATING, rating);
                                        }
                                        List<String> restaurantNumberAndWebSite = mainViewViewModel
                                                .loadRestaurantPhoneNumberAndWebSite(restaurant);
                                        if (restaurantNumberAndWebSite.size() >= 1) {
                                            if (restaurantNumberAndWebSite.get(0) != null) {
                                                String restaurantPhoneNumber = restaurantNumberAndWebSite.get(0);
                                                intentDetail.putExtra(RESTAURANT_PHONE_NUMBER, restaurantPhoneNumber);
                                            }
                                            if (restaurantNumberAndWebSite.size() >= 2) {
                                                if (restaurantNumberAndWebSite.get(1) != null) {
                                                    String restaurantWebSite = restaurantNumberAndWebSite.get(1);
                                                    intentDetail.putExtra(RESTAURANT_WEBSITE, restaurantWebSite);
                                                }
                                            } else {
                                                Log.e("DETAIL", "onMarkerClick: phone size<2 ");
                                            }
                                        } else {
                                            Log.e("DETAIL", "onMarkerClick: website size<1 ");

                                        }

                                        intentDetail.putExtra(RESTAURANT_ID, restaurant.getPlaceId());
                                        activity.startActivity(intentDetail);
                                    }
                                }
                            } else {
                                Log.e("TAG", "onComplete: Vous n'avez choisit aucun restaurant");
                                Toast.makeText(activity.getApplicationContext(),
                                        "Vous n'avez choisit aucun restaurant", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        });
    }


}
