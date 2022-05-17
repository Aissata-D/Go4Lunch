package com.sitadigi.go4lunch.utils;



import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.firebase.ui.auth.AuthUI;
import com.sitadigi.go4lunch.R;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

import java.util.Arrays;
import java.util.List;


public class DialogClass  {
UserViewModel mUserViewModel;
        private static final int RC_SIGN_IN = 123;

        public DialogClass() {
        }

        public void startSignInActivity(Activity activity){

                // Choose authentication providers
                List<AuthUI.IdpConfig> providers =
                        Arrays.asList( new AuthUI.IdpConfig.FacebookBuilder().build(),
                                new AuthUI.IdpConfig.GoogleBuilder().build());

                // Launch the activity
                activity.startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setTheme(R.style.LoginTheme)
                                .setAvailableProviders(providers)
                                .setIsSmartLockEnabled(false, true)
                                //.setLogo(R.drawable.fond_d_ecran)
                                .build(),
                        RC_SIGN_IN);
        }

        private void startSignInActivity(){


        }
}
