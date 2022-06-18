package com.sitadigi.go4lunch.utils;


import static com.sitadigi.go4lunch.DetailActivity.RESTO_ADRESSES;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_ID;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_NAME;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_OPENINGHOURS;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_PHONE_NUMBER;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_PHOTO_URL;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_RATING;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_TYPE;
import static com.sitadigi.go4lunch.DetailActivity.RESTO_WEBSITE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.sitadigi.go4lunch.DetailActivity;
import com.sitadigi.go4lunch.LoginActivity;
import com.sitadigi.go4lunch.R;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.viewModel.MainViewViewModel;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

import java.util.ArrayList;
import java.util.List;


public class ShowSignOutDialogueAlertAndDetailActivity {

    private UserViewModel mUserViewModel;

    public ShowSignOutDialogueAlertAndDetailActivity() {
        mUserViewModel = UserViewModel.getInstance();
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

    public void showDetailActivity(MainViewViewModel mainViewViewModel, Activity activity, LifecycleOwner lifecycleOwner) {
        Log.e("TAG", "showDetailActivity: ATTEINT");
        final String[] userLastRestoId = {""};
        final String[] userRestoName = {""};
        List<GoogleMapApiClass.Result> resultList = new ArrayList<>();
        List<GoogleMapApiClass.Result> listOfRestaurent = new ArrayList<>();

        mainViewViewModel.getRestaurant().observe(lifecycleOwner, RestaurentResponse -> {
            listOfRestaurent.clear();
            listOfRestaurent.addAll(RestaurentResponse);

            //  When getting response, we update UI
            if (listOfRestaurent != null) {
                for (GoogleMapApiClass.Result restaurant : listOfRestaurent) {
                    if (!resultList.contains(restaurant)) {
                        resultList.add(restaurant);
                    }
                }
                String userUid = mUserViewModel.getCurrentUser().getUid();
                // Get userRestoId on firebaseFirestore
                DocumentReference userDocumentRef = mUserViewModel.getUsersCollection().document(userUid);
                userDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                userLastRestoId[0] = document.getString("userRestoId");
                                userRestoName[0] = document.getString("userRestoName");

                                if ((!userLastRestoId[0].equals("restoIdCreated"))
                                        && (!userRestoName[0].equals("restoNameCreated"))) {


                                    for (GoogleMapApiClass.Result restaurant : resultList) {
                                        // LatLng restoPosition = new LatLng(restaurant.getGeometry().getLocation().getLat(),
                                        //    restaurant.getGeometry().getLocation().getLng());
                                        String restoName = restaurant.getName();
                                        String restoId = restaurant.getPlaceId();
                                        if ((restoName.equals(userRestoName[0])) && (restoId.equals(userLastRestoId[0]))) {
                                            Intent intentDetail = new Intent(activity, DetailActivity.class);
                                            intentDetail.putExtra(RESTO_ID, restaurant.getPlaceId());
                                            intentDetail.putExtra(RESTO_NAME, restoName);
                                            if ((restaurant.getOpeningHours() != null) && (restaurant.getOpeningHours().getOpenNow()) != null) {
                                                intentDetail.putExtra(RESTO_OPENINGHOURS, restaurant.getOpeningHours().getOpenNow());
                                            }
                                            if (restaurant.getPhotos() != null) {
                                                if (restaurant.getPhotos().get(0).getPhotoReference() != null) {
                                                    intentDetail.putExtra(RESTO_PHOTO_URL, restaurant.getPhotos().get(0).getPhotoReference());
                                                }
                                            }
                                            if ((restaurant.getTypes()) != null && (restaurant.getTypes().get(0)) != null) {
                                                String restoAdresses = restaurant.getVicinity();
                                                intentDetail.putExtra(RESTO_ADRESSES, restoAdresses);
                                                String restoType = restaurant.getTypes().get(0);
                                                intentDetail.putExtra(RESTO_TYPE, restoType);
                                            }
                                            if(restaurant.getRating()!=null){
                                                float rating =  restaurant.getRating().floatValue();
                                                intentDetail.putExtra(RESTO_RATING, rating);
                                            }
                                            List<String> restaurantNumberAndWebSite =mainViewViewModel
                                                    .loadRestaurantPhoneNumberAndWebSite(restaurant);
                                            if(restaurantNumberAndWebSite.size() >=1){
                                                if(restaurantNumberAndWebSite.get(0) != null){
                                                    String restaurantPhoneNumber = restaurantNumberAndWebSite.get(0);
                                                    intentDetail.putExtra(RESTO_PHONE_NUMBER, restaurantPhoneNumber);
                                                    Log.e("DETAIL", "onMarkerClick: Phone "+restaurantPhoneNumber );
                                                }
                                                if(restaurantNumberAndWebSite.size() >=2) {
                                                    if (restaurantNumberAndWebSite.get(1) != null) {
                                                        String restaurantWebSite = restaurantNumberAndWebSite.get(1);
                                                        intentDetail.putExtra(RESTO_WEBSITE, restaurantWebSite);
                                                    }
                                                }else{
                                                    Log.e("DETAIL", "onMarkerClick: phone size<2 " );
                                                }
                                            }   else{
                                                Log.e("DETAIL", "onMarkerClick: website size<1 " );

                                            }

                                            intentDetail.putExtra(RESTO_ID, restaurant.getPlaceId());
                                            activity.startActivity(intentDetail);
                                        }
                                    }
                                } else {
                                    Log.e("TAG", "onComplete: Vous n'avez choisit aucun restaurent");
                                    Toast.makeText(activity.getApplicationContext(), "Vous n'avez choisit aucun restaurent", Toast.LENGTH_SHORT).show();
                                    // textView.setText("Vous n'avez choisit aucun restaurent");

                                }
                            }
                        }
                    }
                });
            }
        });

    }


}
