package com.sitadigi.go4lunch;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseUser;
import com.sitadigi.go4lunch.R;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

public class ProfileActivity extends AppCompatActivity {
    TextView tvName;
    TextView tvMail;
    TextView tvURI;
    ImageView ivProfilphoto;
    Button btnLogOut;
    Button btnMainActivity;
    private UserViewModel mUserViewModel = UserViewModel.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        tvName = (TextView) findViewById(R.id.nameProfil);
        tvMail= (TextView) findViewById(R.id.mailProfil);
        ivProfilphoto = (ImageView) findViewById(R.id.photoProfil);
        btnLogOut = (Button) findViewById(R.id.btn_log_out);
        tvURI = (TextView) findViewById(R.id.urlPhotoProfil);
        btnMainActivity = (Button) findViewById(R.id.main_activity);

        updateUIWithUserData();

        signOut();
        btnMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void signOut() {
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mUserViewModel.signOut(ProfileActivity.this);
            }
        });
    }


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
                .into(ivProfilphoto);
    }


    private void setTextUserData(FirebaseUser user){

        //Get email & username from User
        String email = TextUtils.isEmpty(user.getEmail()) ? getString(R.string.info_no_email_found) : user.getEmail();
        String username = TextUtils.isEmpty(user.getDisplayName()) ? getString(R.string.info_no_username_found) : user.getDisplayName();
        Uri urlPhoto;
                if(user.getPhotoUrl() !=null) {
                    urlPhoto = user.getPhotoUrl();
                    tvURI.setText (urlPhoto.toString());
                }else tvURI.setText ("pas de url photo");

        //Update views with data
        tvName.setText(username);
        tvMail.setText(email);
       // tvURI.setText(urlPhoto);
    }
}