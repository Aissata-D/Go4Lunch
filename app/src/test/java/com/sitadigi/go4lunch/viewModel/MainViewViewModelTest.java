package com.sitadigi.go4lunch.viewModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.maps.model.LatLng;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.GooglePlaceDetailApiClass;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.repository.FakeGoogleMapApiCallsRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kotlin.jvm.JvmField;

@RunWith(MockitoJUnitRunner.class)

public class MainViewViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Rule
    @JvmField
    public RxImmediateSchedulerRule testSchedulerRule = new RxImmediateSchedulerRule();//For Retrofit
    @Mock
    Observer<GoogleMapApiClass> dataObserver;
    @Mock
    Observer<Boolean> loadingObserver;
    @Mock
    Observer<Throwable> throwableObserver;
    @Mock
    Observer<String> dataObserverString;

    MainViewViewModel mockViewModel = org.mockito.Mockito.mock(MainViewViewModel.class);
    MainViewViewModel mMainViewViewModel;
    String location;
    String location1;
    String destinations;
    String origins;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        FakeGoogleMapApiCallsRepository fakeGoogleMapApiCallsRepository
                = new FakeGoogleMapApiCallsRepository();
        mMainViewViewModel = new MainViewViewModel(fakeGoogleMapApiCallsRepository);
        location = "45.76667,4.88333";
        location1 = "46.76667,4.98333";
        destinations = "46.76667,5.88333";
        origins = "47.76667,6.88333";
    }

    @Test
    public void testGetRestaurantDistance() {

        double restaurantDistance = 0.0;
        double distanceBetweenTwoDifferentPlace = 366098;
        double deltaForTheSamePlace = 0.0;
        double deltaForTwoDifferentPlace = 0.9;
        //assertion for the same place
        assertEquals(restaurantDistance, MainViewViewModel.getRestaurantDistance(45.76667
                , 45.76667, 4.88333, 4.88333
                , 0.0, 0.0), deltaForTheSamePlace);
        //assertion for two different place
        assertEquals(distanceBetweenTwoDifferentPlace, MainViewViewModel.getRestaurantDistance(
                45.76667, 48.76667, 4.88333, 6.88333
                , 0.0, 0.0), deltaForTwoDifferentPlace);
    }

    @Test
    public void testLoadRestaurantPhoneNumberAndWebSite() {

        GooglePlaceDetailApiClass.Result result1 = new GooglePlaceDetailApiClass.Result();
        String restaurantId = "restaurantId";
        String restaurantWebSite = "www.website.fr";
        String restaurantPhoneNumber = "0606060606";
        result1.setName("nameOfResult1");
        result1.setInternationalPhoneNumber(restaurantPhoneNumber);
        result1.setWebsite(restaurantWebSite);
        GooglePlaceDetailApiClass googlePlaceDetailApiClass = new GooglePlaceDetailApiClass();
        googlePlaceDetailApiClass.setResult(result1);
        MutableLiveData<GooglePlaceDetailApiClass> googlePlaceDetailApiClassMutableLiveData = new MutableLiveData<>();
        googlePlaceDetailApiClassMutableLiveData.setValue(googlePlaceDetailApiClass);
        //Result wanted
        when(mockViewModel.loadRestaurantPhoneNumberAndWebSite(restaurantId))
                .thenReturn(googlePlaceDetailApiClassMutableLiveData);
        //assertion
        assertEquals(restaurantWebSite, mockViewModel.loadRestaurantPhoneNumberAndWebSite(restaurantId)
                .getValue().getResult().getWebsite());
        assertEquals(restaurantWebSite, mockViewModel.loadRestaurantPhoneNumberAndWebSite(restaurantId)
                .getValue().getResult().getWebsite());
    }

    @Test
    public void testGetRestaurant() {
        GoogleMapApiClass.Result result1 = new GoogleMapApiClass.Result();
        result1.setName("nameOfResult1");
        List<GoogleMapApiClass.Result> results = Arrays.asList(result1);
        MutableLiveData<List<GoogleMapApiClass.Result>> mutableLiveDataResults = new MutableLiveData<>();
        mutableLiveDataResults.setValue(results);
        //Result wanted
        when(mockViewModel.getRestaurant()).thenReturn(mutableLiveDataResults);
        //assertion
        assertEquals(mutableLiveDataResults, mockViewModel.getRestaurant());
        assertEquals(mutableLiveDataResults.getValue().get(0).getName()
                , mockViewModel.getRestaurant().getValue().get(0).getName());
    }

    @Test
    public void testLoadRestaurantData() {

        GoogleMapApiClass.Result result1 = new GoogleMapApiClass.Result();
        result1.setName("nameOfResult1");
        List<GoogleMapApiClass.Result> results = Arrays.asList(result1);
        GoogleMapApiClass googleMapApiClass = new GoogleMapApiClass();
        googleMapApiClass.setResults(results);
        MutableLiveData<GoogleMapApiClass> googleMapApiClassMutable = new MutableLiveData<>();
        googleMapApiClassMutable.setValue(googleMapApiClass);
        //Act
        mMainViewViewModel.getSearchData().observeForever(dataObserver);
        mMainViewViewModel.getIsLoadingData().observeForever(loadingObserver);
        mMainViewViewModel.getErrorData().observeForever(throwableObserver);
        mMainViewViewModel.getSearchDataString().observeForever(dataObserverString);
        mMainViewViewModel.loadRestaurantData(location);
        //Verify
        verify(dataObserverString).onChanged(googleMapApiClass.getResults().get(0).getName());
        verify(loadingObserver).onChanged(true);
    }

    @Test
    public void testGetLocationMutableLiveData() {
        MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
        Location location = new Location("");
        location.setLatitude(45.76667);
        location.setLongitude(46.76667);
        locationMutableLiveData.setValue(location);
        //Result wanted
        when(mockViewModel.getLocationMutableLiveData()).thenReturn(locationMutableLiveData);
        //assertion
        assertEquals(locationMutableLiveData, mockViewModel.getLocationMutableLiveData());
    }

    @Test
    public void testGetResultSearchLatLng() {
        LatLng latLng = new LatLng(47.76667, 49.76667);
        MutableLiveData<LatLng> mutableLiveDataLatLng = new MutableLiveData<>();
        mutableLiveDataLatLng.setValue(latLng);
        //Result wanted
        when(mockViewModel.getResultSearchLatLng()).thenReturn(mutableLiveDataLatLng);
        //assertion
        assertEquals(mutableLiveDataLatLng, mockViewModel.getResultSearchLatLng());
    }

    @Test
    public void testGetResultSearchPlaceName() {
        MutableLiveData<String> resultSearchPlaceNameMutableLiveData = new MutableLiveData<>();
        String resultSearchPlaceName = "resultSearchPlaceName";
        resultSearchPlaceNameMutableLiveData.setValue(resultSearchPlaceName);
        //Result wanted
        when(mockViewModel.getResultSearchPlaceName())
                .thenReturn(resultSearchPlaceNameMutableLiveData);
        //assertion
        assertEquals(resultSearchPlaceNameMutableLiveData, mockViewModel.getResultSearchPlaceName());
    }

    @Test
    public void testIsRestaurantSelectedByOneWorkmate() {
        List<User> users = new ArrayList<User>();
        User user1 = new User("uid1", "user1", "email1", "urlPicture1",
                "restaurantId1", "restaurantName1", "restaurantType1");
        User user2 = new User("uid2", "user2", "email2", "urlPicture2",
                "restaurantId2", "restaurantName2", "restaurantType2");
        users.add(user1);
        users.add(user2);
        boolean result1 = mMainViewViewModel.isRestaurantSelectedByOneWorkmate("restaurantId1", users);
        boolean result2 = mMainViewViewModel.isRestaurantSelectedByOneWorkmate("restaurantId2", users);
        boolean resultNotSelected = mMainViewViewModel.isRestaurantSelectedByOneWorkmate("restaurantIdNotFount", users);
        assertTrue(result1);
        assertTrue(result2);
        assertFalse(resultNotSelected);
    }
}

//Total methode 12
//Test 8 ==> 66%
