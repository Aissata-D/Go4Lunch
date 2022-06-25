package com.sitadigi.go4lunch.viewModel;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.repository.FakeUserRepository;
import com.sitadigi.go4lunch.repository.UserRepository;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import kotlin.jvm.JvmField;

public class UserViewModelTest extends TestCase {

    UserViewModel mockUserViewModel = org.mockito.Mockito.mock(UserViewModel.class);
    UserRepository mockUserRepository = org.mockito.Mockito.mock(UserRepository.class);
    FakeUserRepository mockFakeUserRepository = org.mockito.Mockito.mock(FakeUserRepository.class);
    @Mock
    FirebaseAuth mockFirebaseAuth;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Rule
    @JvmField
    public RxImmediateSchedulerRule testSchedulerRule = new RxImmediateSchedulerRule() ;//For Retrofit

    InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();//For LiveData
    private UserViewModel mUserViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        FakeGoogleMapApiCallsRepository fakeGoogleMapApiCallsRepository
                =new FakeGoogleMapApiCallsRepository();
        mUserViewModel = new UserViewModel(mockFakeUserRepository);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void onCleared() {
    }

    @Test
    public void clear() {
    }

    @Test
    public void setTagIfAbsent() {
    }

    @Test
    public void getTag() {
    }

    @Test
    public void getUsersCollection() {

    }

    @Test
    public void getCurrentUser() {
        List<User> users = new ArrayList<User>();
        User user1 = new User("uid1","user1","email1","urlPicture1",
                "restaurantId1","restaurantName1","restaurantType1");
        User user2 = new User("uid2","user2","email2","urlPicture2",
                "restaurantId2","restaurantName2","restaurantType2");
        users.add(user1);
        //Result wanted
        when(mockFakeUserRepository.getCurrentUser()).thenReturn(
                mockFirebaseAuth.getInstance().getCurrentUser());
        //assertion
        assertEquals(mockFirebaseAuth.getInstance().getCurrentUser(),mockUserViewModel.getCurrentUser());
    }

    @Test
    public void isCurrentUserLogged() {
    }

    @Test
    public void signOut() {
    }

    @Test
    public void deleteUser() {
    }

    @Test
    public void createUser() {
    }

    @Test
    public void updateUsername() {
    }

    @Test
    public void updateUIWithUserData() {
    }

    @Test
    public void getAllUserForNotificationPush() {
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
}