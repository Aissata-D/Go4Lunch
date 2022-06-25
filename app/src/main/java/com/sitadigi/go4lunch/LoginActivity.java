package com.sitadigi.go4lunch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.sitadigi.go4lunch.factory.UserViewModelFactory;
import com.sitadigi.go4lunch.repository.UserRepository;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;

    private UserViewModel mUserViewModel;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UserRepository userRepository = new UserRepository();
        mUserViewModel = new ViewModelProvider(this,
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        mFirebaseAuth = FirebaseAuth.getInstance();

        setupListeners();
    }

    private void setupListeners() {

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            // we are calling method for on authentication state changed ==> if user changed
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Get current user which is authenticated previously. // checking if the user is null or not.
                if (mUserViewModel.isCurrentUserLogged()) {
                    // if the user is already authenticated then he will redirecting to new screen via an intent(His own profileActivity).
                    startProfileActivity();
                    // we are calling finish method to kill on LoginActivity which is displaying our login ui.
                    finish();
                } else {
                    // this method is called when our user is not authenticated previously.
                    startSignInActivity();
                }
            }
        };
    }

    private void startProfileActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void startSignInActivity() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers =
                Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build()
                        , new AuthUI.IdpConfig.TwitterBuilder().build()
                        , new AuthUI.IdpConfig.FacebookBuilder().build()
                        , new AuthUI.IdpConfig.GoogleBuilder().build());
        // Launch the activity
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.fond_d_ecran)
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // we are calling our auth listener method on app resume.
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // here we are calling remove auth listener method on stop.
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {

        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == RC_SIGN_IN) {
            Log.e("TAG", "handleResponseAfterSignIn: RC_SIGN_IN");
            // SUCCESS
            if (resultCode == RESULT_OK) {
                Log.e("TAG", "handleResponseAfterSignIn: result OK");
                // Create a new document users on Firestore if user does not exist
                mUserViewModel.createUser();
                showToast(getString(R.string.connection_succeed));
            } else {
                // ERRORS
                if (response == null) {
                    showToast(getString(R.string.error_authentication_canceled));
                    Log.e("TAG", "handleResponseAfterSignIn: canceled");
                } else if (response.getError() != null) {
                    if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                        showToast(getString(R.string.error_no_internet));
                        Log.e("TAG", "handleResponseAfterSignIn: not internet");
                    } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        showToast(getString(R.string.error_unknown_error));
                        Log.e("TAG", "handleResponseAfterSignIn: erreur unknow");
                    }
                }
            }
        }
    }
}