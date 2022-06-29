package com.sitadigi.go4lunch.viewModel;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.sitadigi.go4lunch.models.GoogleDistanceMatrixClass;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.GooglePlaceDetailApiClass;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.repository.GeoLocateRepository;
import com.sitadigi.go4lunch.repository.GoogleMapApiCallsInterface;
import com.sitadigi.go4lunch.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class MainViewViewModel extends ViewModel {
    private final String API_KEY = "AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08";
    private final UserRepository userRepository;
    private final GeoLocateRepository mGeoLocateRepository;
    private final MutableLiveData<List<GoogleMapApiClass.Result>> listOfRestaurant;
    private final MutableLiveData<LatLng> resultSearchLatLng;
    private final MutableLiveData<String> resultSearchPlaceName;

    //////////////FOR UNIT TESTING //////////////////////////////
    private final MutableLiveData<GoogleMapApiClass> searchData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingData = new MutableLiveData<>();
    private final MutableLiveData<Throwable> errorData = new MutableLiveData<>();
    //////////////END FOR UNIT TESTING//////////////////////////////

    public MutableLiveData<Location> locationMutableLiveData;
    Disposable disposable;
    Disposable disposablePlaceDetail;
    Disposable disposableDistance;
    List<String> listOfPhoneAndWebSite = new ArrayList<>();
    String distance;
    int distanceValue = 0;
    MutableLiveData<GoogleMapApiClass> googleMapApiClassReturn;
    GoogleMapApiCallsInterface mGoogleMapApiCallsInterface;
    GoogleMapApiClass googleMapApiClass1 = new GoogleMapApiClass();
    private MutableLiveData<String> searchDataString = new MutableLiveData<>();
    private String restaurantWebSiteUrl;
    private String restaurantPhoneNumber;

    public MainViewViewModel(GoogleMapApiCallsInterface googleMapApiCallsInterface) {
        userRepository = UserRepository.getInstance();
        mGoogleMapApiCallsInterface = googleMapApiCallsInterface;
        mGeoLocateRepository = new GeoLocateRepository();
        locationMutableLiveData = new MutableLiveData<>();
        resultSearchLatLng = new MutableLiveData<>();
        resultSearchPlaceName = new MutableLiveData<>();
        listOfRestaurant = new MutableLiveData<>();
        googleMapApiClassReturn = new MutableLiveData<GoogleMapApiClass>();
    }

    //////////////METHOD FOR UNIT TESTING //////////////////////////////
    LiveData<String> getSearchDataString() {
        return searchDataString;
    }

    LiveData<GoogleMapApiClass> getSearchData() {
        return searchData;
    }

    LiveData<Boolean> getIsLoadingData() {
        return isLoadingData;
    }

    LiveData<Throwable> getErrorData() {
        return errorData;
    }
    /////////////END //////////////////////////////////////////////

    public int getRestaurantDistance1(String origins, String destinations) {
        disposableDistance = mGoogleMapApiCallsInterface.streamFetchRestaurantDistance(origins,
                destinations, API_KEY).subscribeWith(new DisposableObserver<GoogleDistanceMatrixClass>() {

            @Override
            public void onNext(GoogleDistanceMatrixClass googleDistanceMatrixClass) {
                if (googleDistanceMatrixClass.getRows() == null) {
                    distance = "Not Response";
                }
                if (googleDistanceMatrixClass.getRows().size() > 0) {
                    distance = "Not Response1";
                }
                if (googleDistanceMatrixClass.getRows().get(0) == null) {
                    distance = "Not Response2";
                }
                if (googleDistanceMatrixClass.getRows().get(0).getElements() == null) {
                    distance = "Not Response3";
                }
                if (googleDistanceMatrixClass.getRows().get(0).getElements().size() == 0) {
                    distance = "Not Response4";
                }
                if (googleDistanceMatrixClass.getRows().get(0).getElements().get(0) == null) {
                    distance = "Not Response5";
                }
                if (googleDistanceMatrixClass.getRows().get(0).getElements().get(0).getDistance() == null) {
                    distance = "Not Response6";
                }

                if (googleDistanceMatrixClass.getRows() != null && googleDistanceMatrixClass.getRows().size() > 0
                        && googleDistanceMatrixClass.getRows()
                        .get(0) != null && googleDistanceMatrixClass.getRows().get(0).getElements() != null
                        && googleDistanceMatrixClass.getRows().get(0).getElements().size() > 0
                        && googleDistanceMatrixClass.getRows().get(0).getElements().get(0).getDistance() != null
                        && googleDistanceMatrixClass.getRows().get(0).getElements()
                        .get(0).getDistance().getValue() != null) {
                    distanceValue = googleDistanceMatrixClass.getRows().get(0).getElements()
                            .get(0).getDistance().getValue();
                } else {
                    distanceValue = 0;
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG", "onError: " + e);
            }

            @Override
            public void onComplete() {
            }
        });
        return distanceValue;
    }

    public List<String> loadRestaurantPhoneNumberAndWebSite(GoogleMapApiClass.Result restaurant) {

        disposablePlaceDetail = mGoogleMapApiCallsInterface.streamFetchRestaurantDetail(
                restaurant, API_KEY).subscribeWith(new DisposableObserver<GooglePlaceDetailApiClass>() {

            @Override
            public void onNext(GooglePlaceDetailApiClass googlePlaceDetailApiClass) {
                restaurantPhoneNumber = googlePlaceDetailApiClass.getResult().getInternationalPhoneNumber();
                restaurantWebSiteUrl = googlePlaceDetailApiClass.getResult().getWebsite();
                listOfPhoneAndWebSite.add(0, restaurantPhoneNumber);
                listOfPhoneAndWebSite.add(1, restaurantWebSiteUrl);
            }

            @Override
            public void onError(Throwable e) {
                listOfPhoneAndWebSite = new ArrayList<>();
                Log.e("TAG", "On Error getRestaurantPhoneNumberAndWebSite" + Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {
                Log.e("TAG", "On Complete !! getRestaurantPhoneNumberAndWebSite");
            }
        });
        return listOfPhoneAndWebSite;
    }

    public void loadRestaurantData(String location) {
        //FOR DATA Disposable allow to avoid Memory Leaks
        this.disposable = mGoogleMapApiCallsInterface.streamFetchListOfNearRestaurant(
                        location, 1500, "restaurant", API_KEY, "google.maps.places.RankBy.DISTANCE")
                .subscribeWith(new DisposableObserver<GoogleMapApiClass>() {
                    @Override
                    public void onNext(GoogleMapApiClass googleMapApiClass) {
                        googleMapApiClassReturn.setValue(googleMapApiClass);
                        listOfRestaurant.setValue(googleMapApiClass.getResults());
                        //For Unit testing
                        isLoadingData.postValue(true);
                        searchData.setValue(googleMapApiClass);
                        searchDataString.setValue(googleMapApiClass.getResults().get(0).getName());
                        googleMapApiClass1 = googleMapApiClass;
                        Log.e("TAG", "onNext: " + googleMapApiClass.getResults());
                    }

                    @Override
                    public void onError(Throwable e) {
                        //For Unit testing
                        isLoadingData.postValue(false);
                        errorData.postValue(e);
                        Log.e("TAG", "On Error loadRestaurantData" + Log.getStackTraceString(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG", "On Complete !! loadRestaurantData");
                        //For Unit testing
                        isLoadingData.postValue(false);
                    }
                });
    }

    public MutableLiveData<List<GoogleMapApiClass.Result>> getRestaurant() {
        return listOfRestaurant;
    }

    public void disposeWhenDestroy() {
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }


    public void loadLocationMutableLiveData(Context context, Activity activity, MainViewViewModel mainViewViewModel) {

        locationMutableLiveData = mGeoLocateRepository.getDeviceLocation(context, activity, mainViewViewModel);
    }

    public MutableLiveData<Location> getLocationMutableLiveData() {

        return locationMutableLiveData;
    }

    public void setSearchLatLngMutableLiveData(LatLng latLng) {

        resultSearchLatLng.setValue(latLng);
    }

    public MutableLiveData<LatLng> getResultSearchLatLng() {

        return resultSearchLatLng;
    }

    public void setSearchPlaceNameMutableLiveData(String placeName) {

        resultSearchPlaceName.setValue(placeName);
    }

    public MutableLiveData<String> getResultSearchPlaceName() {
        return resultSearchPlaceName;
    }

    public MutableLiveData<List<User>> getAllUser() {
        return userRepository.getAllUser();
    }

    public boolean isRestaurantSelectedByOneWorkmate(String restaurantId, List<User> users) {
        boolean b = false;
        for (User user : users) {
            if (user.getUserRestaurantId().equals(restaurantId)) {
                b = true;
                break;
            }
        }
        return b;
    }

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * @returns Distance in Meters
     */
    public static double getRestaurantDistance(double lat1, double lat2, double lon1
                                  ,double lon2 , double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}