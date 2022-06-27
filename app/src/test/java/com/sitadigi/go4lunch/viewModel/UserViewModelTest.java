package com.sitadigi.go4lunch.viewModel;

import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.repository.FakeUserRepository;
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

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Rule
    @JvmField
    public RxImmediateSchedulerRule testSchedulerRule = new RxImmediateSchedulerRule();//For Retrofit
    UserViewModel mUserViewModel;
    @Mock
    FakeUserRepository mockFakeUserRepository;
    UserViewModel mockUserViewModel = org.mockito.Mockito.mock(UserViewModel.class);

    FirebaseAuth mockFirebaseAuth = Mockito.mock(FirebaseAuth.class);
    @Mock
    FirebaseUser mockFirebaseUser;

    @Mock
    AuthUI mockAuthUi;
    CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
    FirebaseFirestore mockFirebaseFirestore = Mockito.mock(FirebaseFirestore.class);
    FakeUserRepository mFakeUserRepository = new FakeUserRepository(mockFirebaseAuth, mockFirebaseFirestore
    ,mockAuthUi);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        FakeUserRepository fakeUserRepository = new FakeUserRepository(mockFirebaseAuth
                , mockFirebaseFirestore, mockAuthUi);
        mUserViewModel = new UserViewModel(fakeUserRepository);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getUsersCollection() {
        when(mockFirebaseFirestore.collection("users")).thenReturn(
                mockCollectionReference);
        //assertion
        assertEquals(mFakeUserRepository.getUsersCollection()
                , mUserViewModel.getUsersCollection());

    }

    @Test
    public void getCurrentUser() {
        //Result wanted
        when(mockFirebaseAuth.getCurrentUser()).thenReturn(mockFirebaseUser);
        //assertion
        assertEquals(mFakeUserRepository.getCurrentUser(), mUserViewModel.getCurrentUser());
    }

    @Test
    public void isCurrentUserLogged() {
        // return no user logged
        when(mockFirebaseAuth.getCurrentUser()).thenReturn(null);
        assertFalse(mUserViewModel.isCurrentUserLogged());
        //return mockFirebaseUser are logged
        when(mockFirebaseAuth.getCurrentUser()).thenReturn(mockFirebaseUser);
        assertTrue(mUserViewModel.isCurrentUserLogged());
    }

    @Test
    public void signOut() {
    }

    @Test
    public void deleteUser() {
    }

    @Test
    public void updateUsername() {
    }

    @Test
    public void createUser() {
    }

    @Test
    public void updateUIWithUserData() {
    }

    @Test
    public void getCurrentUserUID() {
        //  return a String currentUserId
        String currentUserId = "currentUserId";
        String currentUserIdActual = mFakeUserRepository.getCurrentUserUID();
        assertEquals(currentUserId, currentUserIdActual);
    }

    @Test
    public void getAllUserForNotificationPush() {
        List<User> users = new ArrayList<User>();
        User user1 = new User("uid1", "user1", "email1", "urlPicture1",
                "restaurantId1", "restaurantName1", "restaurantType1");
        User user2 = new User("uid2", "user2", "email2", "urlPicture2",
                "restaurantId2", "restaurantName2", "restaurantType2");
        users.add(user1);
        users.add(user2);
        // assertion user 1
        assertEquals(users.get(0).getEmail(), mUserViewModel.getAllUserForNotificationPush().get(0).getEmail());
        assertEquals(users.get(0).getUid(), mUserViewModel.getAllUserForNotificationPush().get(0).getUid());
        //assertion user 2
        assertEquals(users.get(1).getEmail(), mUserViewModel.getAllUserForNotificationPush().get(1).getEmail());
        assertEquals(users.get(1).getUid(), mUserViewModel.getAllUserForNotificationPush().get(1).getUid());
    }
}
// Total method = 10
//test 5 methode = 50%