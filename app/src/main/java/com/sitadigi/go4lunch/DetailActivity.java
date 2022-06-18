package com.sitadigi.go4lunch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.sitadigi.go4lunch.models.RestaurantLike;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.ui.workmaters.WorkmateViewModel;
import com.sitadigi.go4lunch.utils.UtilsDetailActivity;
import com.sitadigi.go4lunch.viewModel.MainViewViewModel;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {
    public static final String RESTO_NAME = "RESTO_NAME";
    public static final String RESTO_PHOTO_URL = "RESTO_PHOTO_URL";
    public static final String RESTO_OPENINGHOURS = "RESTO_OPENINGHOURS";
    public static final String RESTO_ADRESSES = "RESTO_ADRESSES";
    public static final String RESTO_TYPE = "RESTO_TYPE";
    public static final String RESTO_ID = "RESTO_ID";
    public static final String RESTO_RATING = "RESTO_RATING";
    public static final String RESTO_PHONE_NUMBER = "RESTO_PHONE_NUMBER";
    public static final String RESTO_WEBSITE = "RESTO_WEBSITE";
    private static final int MY_PERMISSION_REQUEST_CODE_CALL_PHONE = 555;
    // private static final String COLLECTION_RESTAURANT_LIKE_NAME = "restaurantLike";

    String userUid;
    ImageView mImageViewResto;
    TextView tvRestoName;
    TextView tvRestoTypeAndAdresses;
    TextView tvRestoOpeningHours;
    TextView tvRestoLike;
    TextView tvRestaurantWebsite;
    TextView tvRestaurantPhoneNumber;
    RatingBar restaurantRatingBar;
    FloatingActionButton fbaRestoChoice;
    String userLastRestoId = "";
    String restoId;
    String restoName;
    String restoPhotoUrl;
    String restoAdresses;
    String restaurantWebSite;
    String restaurantPhoneNumber;
    String urlConcat;
    UtilsDetailActivity utilsDetailActivity;
    UserViewModel mUserViewModel;
    MainViewViewModel mMainViewViewModel;
    RecyclerView mRecyclerView;
    WorkmateViewModel mWorkmateViewModel;
    DetailActivityAdapter detailActivityAdapter;
    LinearLayoutManager linearLayoutManager;
    float restaurantRating;
    private String restoType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        // Get uid of user on logged
        mWorkmateViewModel = new ViewModelProvider(this).get(WorkmateViewModel.class);

        mUserViewModel = new UserViewModel();
        userUid = mUserViewModel.getCurrentUser().getUid();

        restoId = getIntent().getStringExtra(RESTO_ID);
        restoName = getIntent().getStringExtra(RESTO_NAME);
        restoPhotoUrl = getIntent().getStringExtra(RESTO_PHOTO_URL);
        restoAdresses = getIntent().getStringExtra(RESTO_ADRESSES);
        restoType = getIntent().getStringExtra(RESTO_TYPE);
        restaurantRating = getIntent().getFloatExtra(RESTO_RATING, 0);
        restaurantWebSite = getIntent().getStringExtra(RESTO_WEBSITE);
        restaurantPhoneNumber = getIntent().getStringExtra(RESTO_PHONE_NUMBER);
        ;
        fbaRestoChoice = findViewById(R.id.fab_choice_resto);
        tvRestoLike = (TextView) findViewById(R.id.restaurant_details_name_text_adresse);
        restaurantRatingBar = (RatingBar) findViewById(R.id.rating_bar_star);
        tvRestaurantWebsite = (TextView) findViewById(R.id.restaurant_website);
        tvRestaurantPhoneNumber = (TextView) findViewById(R.id.restaurant_phone_number);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_detail_activity);
        linearLayoutManager = new LinearLayoutManager(DetailActivity.this);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));


        mImageViewResto = (ImageView) findViewById(R.id.resto_img);
        tvRestoName = (TextView) findViewById(R.id.resto_name_detail_activity);
        tvRestoTypeAndAdresses = (TextView) findViewById(R.id.resto_type_detail_detail_activity);
        String urlPart1 = getString(R.string.url_google_place_photo_part1);
        String urlPart2 = restoPhotoUrl;
        String urlPart3 = getString(R.string.url_google_place_photo_part3);
        urlConcat = urlPart1 + urlPart2 + urlPart3;

        utilsDetailActivity = new UtilsDetailActivity(this, restoId, userLastRestoId,
                this, restaurantRatingBar, mWorkmateViewModel, mUserViewModel);
        // utilsDetailActivity.setRatingIcon(/*DetailActivity.this,restaurantRatingBar,mWorkmateViewModel,
        // mUserViewModel*/);
        utilsDetailActivity.setIconStarColor(tvRestoLike, mUserViewModel);
        utilsDetailActivity.setfabColor(fbaRestoChoice, mUserViewModel);
        float i = restaurantRating;
        utilsDetailActivity.setRatingIcon1(restaurantRatingBar, (float) restaurantRating);


        tvRestoLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //utilsDetailActivity.setRatingIcon(DetailActivity.this,restaurantRatingBar,mWorkmateViewModel,mUserViewModel);

                utilsDetailActivity.clickOnButtonLike(userUid, tvRestoLike, restoName, mUserViewModel);
                //   utilsDetailActivity.setRatingIcon();

            }
        });
        fbaRestoChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utilsDetailActivity.clickOnButtonFab(mUserViewModel, userUid, fbaRestoChoice, restoName, restoType);
               // initRecyclerView();
            }
        });
        tvRestaurantPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (restaurantPhoneNumber != null) {
                    askPermissionAndCall();
                    Toast.makeText(DetailActivity.this, restaurantPhoneNumber, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailActivity.this, getString(R.string.no_phone_number), Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvRestaurantWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (restaurantWebSite != null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurantWebSite));
                    startActivity(browserIntent);
                    Toast.makeText(DetailActivity.this, restaurantWebSite, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(DetailActivity.this, getString(R.string.no_web_site_link), Toast.LENGTH_SHORT).show();
                }

            }
        });

        configureDetailView();
        initRecyclerView();

    }

    public void configureDetailView() {
        String restoTypeAndAdresses = restoType + " - " + restoAdresses;
        tvRestoName.setText(restoName);
        tvRestoTypeAndAdresses.setText(restoTypeAndAdresses);
        //GLIDE TO SHOW PHOTO
        Glide.with(this)
                .load(getUrl(urlConcat))
                .apply(RequestOptions.noTransformation())
                .centerCrop()
                .placeholder(R.drawable.img_resto_placeholder)
                .placeholder(R.drawable.img_resto_placeholder)
                .into(mImageViewResto);
    }

    Uri getUrl(String base) {
        return Uri.parse(base);
    }


    public void initRecyclerView() {

        mWorkmateViewModel.getAllUser().observe(this, usersLiveData -> {
            mRecyclerView.getRecycledViewPool().clear();
            mRecyclerView.setAdapter(null);
            mRecyclerView.setLayoutManager(null);
            // MutableLiveData<String> liveDataRestoId = new MutableLiveData<>();
            List<User> usersInter = new ArrayList<>();
            // usersInter.clear();
            for (User user : usersLiveData) {
                // liveDataRestoId.setValue(user.getUserRestoId());
                //  liveDataRestoId.equals()
                if (user.getUserRestoId().equals(restoId)) {
                    if (!usersInter.contains(user)) {
                        usersInter.add(user);
                    }
                } else {
                    usersInter.remove(user);
                }
            }
            List<User> userList = new ArrayList<>();
            detailActivityAdapter = new DetailActivityAdapter(userList);
            detailActivityAdapter = new DetailActivityAdapter(usersInter);
            detailActivityAdapter.notifyDataSetChanged();
            mRecyclerView.getRecycledViewPool().clear();
            mRecyclerView.setAdapter(null);
            mRecyclerView.setLayoutManager(null);
            mRecyclerView.setAdapter(detailActivityAdapter);
            mRecyclerView.setLayoutManager(linearLayoutManager);
        });
    }


    private void askPermissionAndCall() {

        // With Android Level >= 23, you have to ask the user for permission to Call.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // 23
            // Check if we have Call permission
            int sendSmsPermisson = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE);
            if (sendSmsPermisson != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSION_REQUEST_CODE_CALL_PHONE
                );
                return;
            }
        }
        this.callNow();
    }

    @SuppressLint("MissingPermission")
    private void callNow() {
        // String phoneNumber = this.editTextPhoneNumber.getText().toString();

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + restaurantPhoneNumber));
        try {
            this.startActivity(callIntent);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Your call failed... " + ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


    // When you have the request results
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE_CALL_PHONE: {

                // Note: If request is cancelled, the result arrays are empty.
                // Permissions granted (CALL_PHONE).
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i("LOG_TAG", "Permission granted!");
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG).show();

                    this.callNow();
                }
                // Cancelled or denied.
                else {
                    Log.i("LOG_TAG", "Permission denied!");
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    // When results returned
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_PERMISSION_REQUEST_CODE_CALL_PHONE) {
            if (resultCode == RESULT_OK) {
                // Do something with data (Result returned).
                Toast.makeText(this, "Action OK", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
    }


}
