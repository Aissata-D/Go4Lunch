package com.sitadigi.go4lunch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private static final String COLLECTION_RESTAURANT_LIKE_NAME = "restaurantLike";

    String userUid;
    ImageView mImageViewResto;
    TextView tvRestoName;
    TextView tvRestoTypeAndAdresses;
    TextView tvRestoOpeningHours;
    TextView tvRestoLike;
    TextView tvRestoNumberLike;
    RatingBar restaurantRatingBar;
    FloatingActionButton fbaRestoChoice;
    String userLastRestoId = "";
    String restoId;
    String restoName;
    String restoPhotoUrl;
    String restoAdresses;
    String urlConcat;
    UtilsDetailActivity utilsDetailActivity;
    UserViewModel mUserViewModel;
    MainViewViewModel mMainViewViewModel;
    RecyclerView mRecyclerView;
    WorkmateViewModel mWorkmateViewModel;
    DetailActivityAdapter detailActivityAdapter;
    LinearLayoutManager linearLayoutManager;
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
        fbaRestoChoice = findViewById(R.id.fab_choice_resto);
        tvRestoLike = (TextView) findViewById(R.id.restaurant_details_name_text_adresse);
        restaurantRatingBar = (RatingBar) findViewById(R.id.rating_bar_star);

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
        utilsDetailActivity.setRatingIcon(/*DetailActivity.this,restaurantRatingBar,mWorkmateViewModel,
        mUserViewModel*/);
        utilsDetailActivity.setIconStarColor(tvRestoLike, mUserViewModel);
        utilsDetailActivity.setfabColor(fbaRestoChoice, mUserViewModel);


        tvRestoLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //utilsDetailActivity.setRatingIcon(DetailActivity.this,restaurantRatingBar,mWorkmateViewModel,mUserViewModel);

                utilsDetailActivity.clickOnButtonLike(userUid, tvRestoLike, restoName, mUserViewModel);
                utilsDetailActivity.setRatingIcon();

            }
        });
        fbaRestoChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utilsDetailActivity.clickOnButtonFab(mUserViewModel, userUid, fbaRestoChoice, restoName, restoType);

                initRecyclerView();
                
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

    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
    }
}
