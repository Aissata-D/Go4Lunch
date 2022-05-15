package com.sitadigi.go4lunch.ui;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseUser;
import com.sitadigi.go4lunch.ProfileActivity;
import com.sitadigi.go4lunch.R;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NavHeaderMainActivity extends AppCompatActivity {
    TextView userName;
    TextView userEmail;
    ImageView userPhoto;

    private final UserViewModel mUserViewModel = UserViewModel.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_main);

        userPhoto = (ImageView) findViewById(R.id.img_user);
        userName = (TextView) findViewById(R.id.name_user);
        userEmail = (TextView) findViewById(R.id.email_user);
        updateUIWithUserData();

       // signOut();

    }

    /*private void signOut() {
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserViewModel.signOut(ProfileActivity.this);
            }
        });
    }*/


    private void updateUIWithUserData(){
        if(mUserViewModel.isCurrentUserLogged()){
            FirebaseUser user = mUserViewModel.getCurrentUser();

            if(user.getPhotoUrl() != null){
                setProfilePicture(user.getPhotoUrl());
            }
            setTextUserData(user);
        }
    }

    private void setProfilePicture(Uri profilePictureUrl) {
        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(userPhoto);
    }


    private void setTextUserData(FirebaseUser user){

        //Get email & username from User
        String email = TextUtils.isEmpty(user.getEmail()) ? getString(R.string.info_no_email_found) : user.getEmail();
        String username = TextUtils.isEmpty(user.getDisplayName()) ? getString(R.string.info_no_username_found) : user.getDisplayName();
  /*      Uri urlPhoto;
        if(user.getPhotoUrl() !=null) {
            urlPhoto = user.getPhotoUrl();
            tvURI.setText (urlPhoto.toString());
        }else tvURI.setText ("pas de url photo");
*/
        //Update views with data
        userName.setText(username);
        userEmail.setText(email);
        // tvURI.setText(urlPhoto);
    }
}