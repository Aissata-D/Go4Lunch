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
import androidx.test.platform.app.InstrumentationRegistry;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.sitadigi.go4lunch.models.GoogleDistanceMatrixClass;
import com.sitadigi.go4lunch.models.GoogleMapApiClass;
import com.sitadigi.go4lunch.models.GooglePlaceDetailApiClass;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.repository.GoogleMapApiCallsRepository;
import com.sitadigi.go4lunch.repository.UserRepository;

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


import io.reactivex.Observable;
import kotlin.jvm.JvmField;

@RunWith(MockitoJUnitRunner.class)

public class MainViewViewModelTest  {

    @Mock Observer<GoogleMapApiClass> dataObserver;
    @Mock Observer<Boolean> loadingObserver;
    @Mock Observer<Throwable> throwableObserver;
    @Mock Observer<String> dataObserverString;
    @Mock FakeGoogleMapApiCallsRepository fakeMock;


    MainViewViewModel mockViewModel = org.mockito.Mockito.mock(MainViewViewModel.class);
    UserRepository mockUserRepository = org.mockito.Mockito.mock(UserRepository.class);

    MainViewViewModel mMainViewViewModel;
    String location;
    String location1;
    String destinations;
    String origins;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Rule
    @JvmField
    public RxImmediateSchedulerRule testSchedulerRule = new RxImmediateSchedulerRule() ;//For Retrofit

    InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();//For LiveData

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        FakeGoogleMapApiCallsRepository fakeGoogleMapApiCallsRepository
                =new FakeGoogleMapApiCallsRepository();
        mMainViewViewModel = new MainViewViewModel(fakeGoogleMapApiCallsRepository);
        location = "45.76667,4.88333";
        location1 = "46.76667,4.98333";
        destinations = "46.76667,5.88333";
        origins = "47.76667,6.88333";
    }

    public void tearDown() throws Exception {
    }
    @Test
    public void testGetRestaurantDistance() {

        String restaurantDistance = "restaurantDistance";
        //Result wanted
        when(mockViewModel.getRestaurantDistance(origins,destinations))
                .thenReturn(restaurantDistance);
        //assertion
        assertEquals(restaurantDistance,mockViewModel.getRestaurantDistance(origins,destinations));
    }

    @Test
    public void testLoadRestaurantPhoneNumberAndWebSite() {

        GoogleMapApiClass.Result result1 = new GoogleMapApiClass.Result();
        result1.setName("nameOfResult1");
        List<String> phoneAndWebSite = new ArrayList<>();
        phoneAndWebSite.add("0606060606");
        phoneAndWebSite.add("www.website.fr");
        //Result wanted
        when(mockViewModel.loadRestaurantPhoneNumberAndWebSite(result1))
                .thenReturn(phoneAndWebSite);
        //assertion
        assertEquals(phoneAndWebSite,mockViewModel.loadRestaurantPhoneNumberAndWebSite(result1));
        assertEquals(phoneAndWebSite.get(0),mockViewModel.loadRestaurantPhoneNumberAndWebSite(result1)
                .get(0));
        assertEquals(phoneAndWebSite.get(1),mockViewModel.loadRestaurantPhoneNumberAndWebSite(result1)
                .get(1));

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
        assertEquals(mutableLiveDataResults,mockViewModel.getRestaurant());
        assertEquals(mutableLiveDataResults.getValue().get(0).getName()
                ,mockViewModel.getRestaurant().getValue().get(0).getName());
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
        //Assemble
        when(fakeMock.streamFetchListOfNearRestaurant(
                location, 1500, "restaurant","AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08")
        ).thenReturn(Observable.just(googleMapApiClass));
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
        location.setLatitude(46.76667);
        locationMutableLiveData.setValue(location);
        //Result wanted
        when(mockViewModel.getLocationMutableLiveData()).thenReturn(locationMutableLiveData);
        //assertion
        assertEquals(locationMutableLiveData,mockViewModel.getLocationMutableLiveData());
    }

    @Test
    public void testGetResultSearchLatLng() {
        LatLng latLng = new LatLng(47.76667,49.76667);
        MutableLiveData<LatLng> mutableLiveDataLatLng = new MutableLiveData<>();
        mutableLiveDataLatLng.setValue(latLng);
        //Result wanted
        when(mockViewModel.getResultSearchLatLng()).thenReturn(mutableLiveDataLatLng);
        //assertion
        assertEquals(mutableLiveDataLatLng,mockViewModel.getResultSearchLatLng());
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
        assertEquals(resultSearchPlaceNameMutableLiveData,mockViewModel.getResultSearchPlaceName());
    }
    @Test
    public void testGetAllUser() {

        //FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().getTargetContext());

        List<User> users = new ArrayList<User>();
        User user1 = new User("uid1","user1","email1","urlPicture1",
                "restaurantId1","restaurantName1","restaurantType1");
        User user2 = new User("uid2","user2","email2","urlPicture2",
                "restaurantId2","restaurantName2","restaurantType2");
        users.add(user1);
        users.add(user2);
        MutableLiveData<List<User>> mutableLiveDataUsersExpected = new MutableLiveData<>();
        mutableLiveDataUsersExpected.setValue(users);
        when(mockUserRepository.getAllUser()).thenReturn(mutableLiveDataUsersExpected);
        MutableLiveData<List<User>> mutableLiveDataUsersActual= mockUserRepository.getAllUser();
        assertEquals(mutableLiveDataUsersExpected,mutableLiveDataUsersActual);
    }

    @Test
    public void testIsRestaurantSelectedByOneWorkmate() {
        List<User> users = new ArrayList<User>();
        User user1 = new User("uid1","user1","email1","urlPicture1",
                "restaurantId1","restaurantName1","restaurantType1");
        User user2 = new User("uid2","user2","email2","urlPicture2",
                "restaurantId2","restaurantName2","restaurantType2");
        users.add(user1);
        users.add(user2);
        boolean result1 = mMainViewViewModel.isRestaurantSelectedByOneWorkmate("restaurantId1",users);
        boolean result2 = mMainViewViewModel.isRestaurantSelectedByOneWorkmate("restaurantId2",users);
        boolean resultNotSelected = mMainViewViewModel.isRestaurantSelectedByOneWorkmate("restaurantIdNotFount",users);
        assertTrue(result1);
        assertTrue(result2);
        assertFalse(resultNotSelected);
    }

    @Test
    public void testDisposeWhenDestroy() {
    }
    @Test
    public void testLoadLocationMutableLiveData() {
    }

    @Test
    public void testSetSearchLatLngMutableLiveData() {
    }

    @Test
    public void testSetSearchPlaceNameMutableLiveData() {
    }
}
/*
 @Test
    public void testLoadRestaurantPhoneNumberAndWebSite() {
        GooglePlaceDetailApiClass googlePlaceDetailApiClass = new GooglePlaceDetailApiClass();
         GoogleDistanceMatrixClass.Row row1= new GoogleDistanceMatrixClass().Row;
        //  row1.setElements();
        // List<GoogleDistanceMatrixClass.Row.El row1 = new GoogleDistanceMatrixClass.Row();

        // row1.getElements().get(0).
        // googleDistanceMatrixClass.setRows();

        GoogleMapApiClass.Result result1 = new GoogleMapApiClass.Result();
        result1.setName("nameOfResult1");
        List<GoogleMapApiClass.Result> results = Arrays.asList(result1);
        GoogleMapApiClass googleMapApiClass = new GoogleMapApiClass();
        googleMapApiClass.setResults(results);
        MutableLiveData<GoogleMapApiClass> googleMapApiClassMutable = new MutableLiveData<>();//= new GoogleMapApiClass();

        googleMapApiClassMutable.setValue(googleMapApiClass);
        //Assemble
        when(mockTest.streamFetchRestaurantDetail(result1, "AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08")
                )
                .thenReturn(Observable.just(googlePlaceDetailApiClass));
        //Act
        mMainViewViewModel.getSearchDataPhoneAndWebSite().observeForever(dataPhoneAndWebSiteObserver);
        mMainViewViewModel.getIsLoadingData().observeForever(loadingObserver);
        mMainViewViewModel.getErrorData().observeForever(throwableObserver);
        try{
            mMainViewViewModel.getRestaurantDistance(origins,destinations);
            Thread.sleep(2000);
        }catch(Exception e){

        }

        //Verify
        //  verify(throwableObserver).onChanged(null);
        //verify(loadingObserver).onChanged(true);
        //verify(dataPhoneAndWebSiteObserver).onChanged(googlePlaceDetailApiClass);
       verify(loadingObserver).onChanged(false);
     }
   */
    /*   mMainViewViewModel.getSearchData().observeForever(dataObserver);
        mMainViewViewModel.getIsLoadingData().observeForever(loadingObserver);
        mMainViewViewModel.getErrorData().observeForever(throwableObserver);
        mMainViewViewModel.loadRestaurantData(location);
        //Verify
      //  verify(loadingObserver).onChanged(true);
       // verify(dataObserver).onChanged(googleMapApiClass);
        verify(loadingObserver).onChanged(false);
         @Test
    public void testFetchSearchData_whenThrowsError() {

        //Assemble
        Throwable throwable = new Throwable("TimeoutException");

        when(mockTest.streamFetchListOfNearRestaurant(
                location,1500,"restaurant","AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08"))
                .thenReturn(Observable.error(throwable));

        //Act
        mMainViewViewModel.getSearchData().observeForever(dataObserver);
        mMainViewViewModel.getIsLoadingData().observeForever(loadingObserver);
        mMainViewViewModel.getErrorData().observeForever(throwableObserver);
        mMainViewViewModel.loadRestaurantData(location);
        //Verify
        // verify(loadingObserver).onChanged(true);
       // verify(throwableObserver).onChanged(throwable);
        verify(loadingObserver).onChanged(false);
    }
 */