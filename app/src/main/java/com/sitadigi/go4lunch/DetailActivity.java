package com.sitadigi.go4lunch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sitadigi.go4lunch.databinding.ActivityDetailBinding;
import com.sitadigi.go4lunch.factory.MainViewModelFactory;
import com.sitadigi.go4lunch.factory.UserViewModelFactory;
import com.sitadigi.go4lunch.models.GooglePlaceDetailApiClass;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.repository.GoogleMapApiCallsRepository;
import com.sitadigi.go4lunch.repository.UserRepository;
import com.sitadigi.go4lunch.utils.UtilsDetailActivity;
import com.sitadigi.go4lunch.viewModel.MainViewViewModel;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String RESTAURANT_NAME = "RESTAURANT_NAME";
    public static final String RESTAURANT_PHOTO_URL = "RESTAURANT_PHOTO_URL";
    public static final String RESTAURANT_OPENINGHOURS = "RESTAURANT_OPENINGHOURS";
    public static final String RESTAURANT_ADDRESS = "RESTAURANT_ADDRESSES";
    public static final String RESTAURANT_TYPE = "RESTAURANT_TYPE";
    public static final String RESTAURANT_ID = "RESTAURANT_ID";
    public static final String RESTAURANT_RATING = "RESTAURANT_RATING";
    public static final String RESTAURANT_PHONE_NUMBER = "RESTAURANT_PHONE_NUMBER";
    public static final String RESTAURANT_WEBSITE = "RESTAURANT_WEBSITE";
    private static final int MY_PERMISSION_REQUEST_CODE_CALL_PHONE = 555;

    ImageView mImageViewRestaurant;
    TextView tvRestaurantName;
    TextView tvRestaurantTypeAndAddresses;
    TextView tvRestaurantLike;
    TextView tvRestaurantWebsite;
    TextView tvRestaurantPhoneNumber;
    RatingBar restaurantRatingBar;
    FloatingActionButton fbaRestaurantChoice;
    String userUid = "";
    String userLastRestaurantId = "";
    String restaurantId = "";
    String restaurantName = "";
    String restaurantPhotoUrl = "";
    String restaurantAddresses = "";
    String restaurantWebSite = "";
    String restaurantPhoneNumber = "";
    String urlConcat = "";
    float restaurantRating;
    String restaurantType = "";
    UtilsDetailActivity utilsDetailActivity;
    UserViewModel mUserViewModel;
    RecyclerView mRecyclerView;
    DetailActivityAdapter detailActivityAdapter;
    LinearLayoutManager linearLayoutManager;
    MainViewViewModel mMainViewViewModel;
    GooglePlaceDetailApiClass restaurantDetail = new GooglePlaceDetailApiClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.sitadigi.go4lunch.databinding.ActivityDetailBinding binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Get uid of user on logged
        GoogleMapApiCallsRepository googleMapApiCallsRepository = new GoogleMapApiCallsRepository();
        mMainViewViewModel = new ViewModelProvider(this, new MainViewModelFactory(googleMapApiCallsRepository)).get(MainViewViewModel.class);
        UserRepository userRepository = new UserRepository();
        mUserViewModel = new ViewModelProvider(this, new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        //Get restaurant information
        userUid = mUserViewModel.getCurrentUser().getUid();
        restaurantId = getIntent().getStringExtra(RESTAURANT_ID);
        restaurantName = getIntent().getStringExtra(RESTAURANT_NAME);
        restaurantPhotoUrl = getIntent().getStringExtra(RESTAURANT_PHOTO_URL);
        restaurantAddresses = getIntent().getStringExtra(RESTAURANT_ADDRESS);
        restaurantType = getIntent().getStringExtra(RESTAURANT_TYPE);
        restaurantRating = getIntent().getFloatExtra(RESTAURANT_RATING, 0);
        restaurantWebSite = getIntent().getStringExtra(RESTAURANT_WEBSITE);
        restaurantPhoneNumber = getIntent().getStringExtra(RESTAURANT_PHONE_NUMBER);
        // Binding views
        fbaRestaurantChoice = binding.fabChoiceRestaurant;
        tvRestaurantLike = binding.restaurantLike;
        restaurantRatingBar = binding.ratingBarStar;
        tvRestaurantWebsite = binding.restaurantWebsite;
        tvRestaurantPhoneNumber = binding.restaurantPhoneNumber;
        mImageViewRestaurant = binding.restoImg;
        tvRestaurantName = binding.restoNameDetailActivity;
        tvRestaurantTypeAndAddresses = binding.restoTypeDetailDetailActivity;
        mRecyclerView = binding.recyclerviewDetailActivity;
        // Decorate recyclerView
        linearLayoutManager = new LinearLayoutManager(DetailActivity.this);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        // Build part of url to get photo of restaurant
        String urlPart1 = getString(R.string.url_google_place_photo_part1);
        String urlPart2 = restaurantPhotoUrl;
        String urlPart3 = getString(R.string.url_google_place_photo_part3);
        urlConcat = urlPart1 + urlPart2 + urlPart3;

        mMainViewViewModel.loadRestaurantPhoneNumberAndWebSite(restaurantId)
                .observe(this, RestaurantDetailMutableLiveData -> {
                   restaurantDetail = RestaurantDetailMutableLiveData;
                });

        utilsDetailActivity = new UtilsDetailActivity(this, restaurantId, userLastRestaurantId);
        utilsDetailActivity.setIconStarColor(tvRestaurantLike, mUserViewModel);
        utilsDetailActivity.setFabColor(fbaRestaurantChoice, mUserViewModel);
        utilsDetailActivity.setRatingIcon(restaurantRatingBar, (float) restaurantRating);
        // Manage click on buttons
        tvRestaurantLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utilsDetailActivity.clickOnButtonLike(userUid, tvRestaurantLike, restaurantName, mUserViewModel);
            }
        });

        fbaRestaurantChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utilsDetailActivity.clickOnButtonFab(mUserViewModel, userUid, fbaRestaurantChoice, restaurantName, restaurantType);
            }
        });

        tvRestaurantPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (restaurantDetail !=null && restaurantDetail.getResult()!=null
                        && restaurantDetail.getResult().getInternationalPhoneNumber() !=null) {
                    restaurantPhoneNumber = restaurantDetail.getResult().getInternationalPhoneNumber();
                    askPermissionAndCall();
                } else {
                    Toast.makeText(DetailActivity.this, getString(R.string.no_phone_number), Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvRestaurantWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (restaurantDetail != null && restaurantDetail.getResult()!=null
                && restaurantDetail.getResult().getWebsite() !=null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW
                            , Uri.parse(restaurantDetail.getResult().getWebsite()));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(DetailActivity.this, getString(R.string.no_web_site_link), Toast.LENGTH_SHORT).show();
                }
            }
        });

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        configureDetailView();
        initRecyclerView();
    }

    public void configureDetailView() {
        String restaurantTypeAndAddresses = restaurantType + " - " + restaurantAddresses;
        tvRestaurantName.setText(restaurantName);
        tvRestaurantTypeAndAddresses.setText(restaurantTypeAndAddresses);
        //GLIDE TO SHOW IMAGE OF THE RESTAURANT
        Glide.with(this)
                .load(getUrl(urlConcat))
                .apply(RequestOptions.noTransformation())
                .centerCrop()
                .placeholder(R.drawable.img_resto_placeholder)
                .placeholder(R.drawable.img_resto_placeholder)
                .into(mImageViewRestaurant);
    }

    // Build a url to get restaurant image
    Uri getUrl(String base) {
        return Uri.parse(base);
    }

    public void initRecyclerView() {

        mUserViewModel.getAllUser().observe(this, usersLiveData -> {
            mRecyclerView.getRecycledViewPool().clear();
            mRecyclerView.setAdapter(null);
            mRecyclerView.setLayoutManager(null);
            List<User> usersInter = new ArrayList<>();
            for (User user : usersLiveData) {
                if (user != null && user.getUserRestaurantId() != null) {
                    if (user.getUserRestaurantId().equals(restaurantId)) {
                        if (!usersInter.contains(user)) {
                            usersInter.add(user);
                        }
                    } else {
                        usersInter.remove(user);
                    }
                }
            }
            List<User> userList = new ArrayList<>();
            detailActivityAdapter = new DetailActivityAdapter(userList);
            detailActivityAdapter = new DetailActivityAdapter(usersInter);
            mRecyclerView.getRecycledViewPool().clear();
            mRecyclerView.setAdapter(detailActivityAdapter);
            mRecyclerView.setLayoutManager(linearLayoutManager);
        });
    }

    private void askPermissionAndCall() {
        // With Android Level >= 23, you have to ask the user for permission to Call.
        boolean success = UtilsDetailActivity.askForSmsPermission(this);
        if (success) {
            this.callNow();
        }
    }

    @SuppressLint("MissingPermission")
    private void callNow() {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + restaurantPhoneNumber));
        try {
            //callMobile(this, restaurantPhoneNumber){}
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
