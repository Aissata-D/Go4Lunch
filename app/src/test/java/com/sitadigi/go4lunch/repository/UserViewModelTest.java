package com.sitadigi.go4lunch.repository;

import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.viewModel.RxImmediateSchedulerRule;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.JvmField;
@RunWith(MockitoJUnitRunner.class)
public class UserViewModelTest extends TestCase {

    UserViewModel mUserViewModel;
    UserViewModel mockUserViewModel = org.mockito.Mockito.mock(UserViewModel.class);
    //FakeUserRepository mockFakeUserRepository = org.mockito.Mockito.mock(FakeUserRepository.class);
   // @Mock
   // FirebaseAuth mockFirebaseAuth;
    FirebaseAuth mockFirebaseAuth = Mockito.mock(FirebaseAuth.class);
    FakeUserRepository mFakeUserRepository = new FakeUserRepository(mockFirebaseAuth);
    @Mock FirebaseUser mockFirebaseUser;
    @Mock FakeUserRepository mockFakeUserRepository;
    @Mock
    FirebaseFirestore mockFirebaseFirestore;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Rule
    @JvmField
    public RxImmediateSchedulerRule testSchedulerRule = new RxImmediateSchedulerRule() ;//For Retrofit

    InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();//For LiveData
    //private UserViewModel mUserViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        FakeUserRepository fakeUserRepository = new FakeUserRepository(mockFirebaseAuth);
        mUserViewModel = new UserViewModel(fakeUserRepository);
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
        users.add(user2);
        //Result wanted
      //  when(mockFirebaseFirestore.collection("users")
              //  .document("cpUJTULzkkhsNqwv4BfJJNoQMUS2").get()).thenReturn();
       when(mockFirebaseAuth.getCurrentUser()).thenReturn(
               mockFirebaseUser );
 /*
        when(mockFirebaseAuth.getCurrentUser()).thenReturn(
                mockFirebaseAuth.getInstance().getCurrentUser());*/
       // mockFirebaseUser = mockFirebaseAuth.getCurrentUser();
        //assertNull(mockFirebaseUser);
        //assertion
       assertEquals(mFakeUserRepository.getCurrentUser()
               ,mUserViewModel.getCurrentUser());
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
    public void getCurrentUserUID() {
        //  return (user != null) ? user.getUid() : null;
        String currentUserId = "currentUserId";
        String currentUserIdActual = mFakeUserRepository.getCurrentUserUID();
        assertEquals(currentUserId,currentUserIdActual);


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
        when( mockFakeUserRepository.getAllUserForNotificationPush()).thenReturn(users);
       // assertion
        assertEquals(users.get(0).getEmail(),mUserViewModel.getAllUserForNotificationPush().get(0).getEmail());
        assertEquals(users.get(0).getUid(),mUserViewModel.getAllUserForNotificationPush().get(0).getUid());
    }
}