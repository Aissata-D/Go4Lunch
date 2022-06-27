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

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sitadigi.go4lunch.DetailActivity;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.viewModel.MainViewViewModel;

import java.util.List;

public class OpenDetailActivityUtils {

    public void clickOnOpenDetailActivityInLisViewAdapter(GoogleMapApiClass.Result restaurant, Context context
            , MainViewViewModel mMainViewViewModel) {
        Intent intentDetail = new Intent(context, DetailActivity.class);
        intentDetail.putExtra(RESTAURANT_ID, restaurant.getPlaceId());
        intentDetail.putExtra(RESTAURANT_NAME, restaurant.getName());
        if ((restaurant.getTypes()) != null && (restaurant.getTypes().get(0)) != null) {
            String restaurantAddress = restaurant.getVicinity();
            intentDetail.putExtra(RESTAURANT_ADDRESS, restaurantAddress);
            String restaurantType = restaurant.getTypes().get(0);
            intentDetail.putExtra(RESTAURANT_TYPE, restaurantType);
        }
        if (restaurant.getOpeningHours() != null && restaurant.getOpeningHours().getOpenNow() != null) {
            intentDetail.putExtra(RESTAURANT_OPENINGHOURS, restaurant.getOpeningHours().getOpenNow());
        }
        if (restaurant.getPhotos() != null) {
            if (restaurant.getPhotos().get(0).getPhotoReference() != null) {
                intentDetail.putExtra(RESTAURANT_PHOTO_URL, restaurant.getPhotos().get(0).getPhotoReference());
            }
        }
        if (restaurant.getRating() != null) {
            float rating = restaurant.getRating().floatValue();
            intentDetail.putExtra(RESTAURANT_RATING, rating);
        }
        List<String> restaurantNumberAndWebSite = mMainViewViewModel
                .loadRestaurantPhoneNumberAndWebSite(restaurant);
        if (restaurantNumberAndWebSite.size() >= 1) {
            if (restaurantNumberAndWebSite.get(0) != null) {
                String restaurantPhoneNumber = restaurantNumberAndWebSite.get(0);
                intentDetail.putExtra(RESTAURANT_PHONE_NUMBER, restaurantPhoneNumber);
                Log.e("DETAIL", "onMarkerClick: Phone " + restaurantPhoneNumber);
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
        context.startActivity(intentDetail);
    }

    public void clickOnOpenDetailActivityInMapviewFragment(GoogleMapApiClass.Result restaurant, Context context
            , MainViewViewModel mMainViewViewModel, String restaurantName) {
        Intent intentDetail = new Intent(context, DetailActivity.class);
        intentDetail.putExtra(RESTAURANT_ID, restaurant.getPlaceId());
        intentDetail.putExtra(RESTAURANT_NAME, restaurantName);
        if ((restaurant.getOpeningHours() != null) && (restaurant.getOpeningHours().getOpenNow()) != null) {
            intentDetail.putExtra(RESTAURANT_OPENINGHOURS, restaurant.getOpeningHours().getOpenNow());
        }
        if (restaurant.getPhotos() != null) {
            if (restaurant.getPhotos().get(0).getPhotoReference() != null) {
                intentDetail.putExtra(RESTAURANT_PHOTO_URL, restaurant.getPhotos().get(0).getPhotoReference());
            }
        }
        if ((restaurant.getTypes()) != null && (restaurant.getTypes().get(0)) != null) {
            String restaurantAddresses = restaurant.getVicinity();
            intentDetail.putExtra(RESTAURANT_ADDRESS, restaurantAddresses);
            String restaurantType = restaurant.getTypes().get(0);
            intentDetail.putExtra(RESTAURANT_TYPE, restaurantType);
        }
        if (restaurant.getRating() != null) {
            float rating = restaurant.getRating().floatValue();
            intentDetail.putExtra(RESTAURANT_RATING, rating);
        }

        List<String> restaurantNumberAndWebSite = mMainViewViewModel
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
        context.startActivity(intentDetail);
    }

    public void clickOnOpenDetailActivityInWorkmateFragment(Context context, List<GoogleMapApiClass.Result> mRestaurants,
                                                            List<User> mUsers, int mPosition,
                                                            MainViewViewModel mMainViewViewModel) {
        for (GoogleMapApiClass.Result result : mRestaurants) {
            if (result.getPlaceId().equals(mUsers.get(mPosition).getUserRestaurantId())
                    && result.getName().equals(mUsers.get(mPosition).getUserRestaurantName())) {
                GoogleMapApiClass.Result restaurant = result;
                Intent intentDetail = new Intent(context, DetailActivity.class);
                intentDetail.putExtra(RESTAURANT_ID, restaurant.getPlaceId());
                intentDetail.putExtra(RESTAURANT_NAME, restaurant.getName());
                if ((restaurant.getTypes()) != null && (restaurant.getTypes().get(0)) != null) {
                    String restaurantAddresses = restaurant.getVicinity();
                    intentDetail.putExtra(RESTAURANT_ADDRESS, restaurantAddresses);
                    String restaurantType = restaurant.getTypes().get(0);
                    intentDetail.putExtra(RESTAURANT_TYPE, restaurantType);
                }
                if (restaurant.getOpeningHours() != null && restaurant.getOpeningHours().getOpenNow() != null) {
                    intentDetail.putExtra(RESTAURANT_OPENINGHOURS, restaurant.getOpeningHours().getOpenNow());
                }
                if (restaurant.getPhotos() != null) {
                    if (restaurant.getPhotos().get(0).getPhotoReference() != null) {
                        intentDetail.putExtra(RESTAURANT_PHOTO_URL, restaurant.getPhotos().get(0).getPhotoReference());
                    }
                }
                if (restaurant.getRating() != null) {
                    float rating = restaurant.getRating().floatValue();
                    intentDetail.putExtra(RESTAURANT_RATING, rating);
                }
                List<String> restaurantNumberAndWebSite = mMainViewViewModel
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
                context.startActivity(intentDetail);
            }
        }
    }
}
