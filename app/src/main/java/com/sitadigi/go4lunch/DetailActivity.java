package com.sitadigi.go4lunch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.sitadigi.go4lunch.models.GoogleClass1;
import com.sitadigi.go4lunch.models.User;
import com.sitadigi.go4lunch.ui.workmaters.WorkmatersViewModel;
import com.sitadigi.go4lunch.repository.GooglePlacePhotoApiCallsRepository;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity /*implements GooglePlacePhotoApiCallsRepository.Callbacks*/ {
    public static final String RESTO_NAME = "RESTO_NAME";
    public static final String RESTO_PHOTO_URL = "RESTO_PHOTO_URL";
    public static final String RESTO_OPENINGHOURS = "RESTO_OPENINGHOURS";
    public static final String RESTO_ADRESSES = "RESTO_ADRESSES";
    public static final String RESTO_TYPE = "RESTO_TYPE";
    public static final String RESTO_ID = null;
    ImageView mImageViewResto;
    TextView tvRestoName;
    TextView tvRestoTypeAndAdresses;
    TextView tvRestoOpeningHours;
    FloatingActionButton fbaRestoChoice;
    String userLastRestoId = "";
    String restoId;
    String restoName;
    String restoPhotoUrl;
    String restoAdresses;
    private String restoType;
    String urlConcat;
    UserViewModel mUserViewModel;
    RecyclerView mRecyclerView;
    WorkmatersViewModel workmatersViewModel;
    DetailActivityAdapter detailActivityAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Objects.requireNonNull(this.getSupportActionBar()).hide();
        workmatersViewModel = new ViewModelProvider(this).get(WorkmatersViewModel.class);

        mUserViewModel = new UserViewModel();
        restoId = getIntent().getStringExtra(RESTO_ID);
        restoName = getIntent().getStringExtra(RESTO_NAME);
        restoPhotoUrl = getIntent().getStringExtra(RESTO_PHOTO_URL);
        restoAdresses = getIntent().getStringExtra(RESTO_ADRESSES);
        restoType = getIntent().getStringExtra(RESTO_TYPE);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_detail_activity);
        linearLayoutManager = new LinearLayoutManager(DetailActivity.this);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));


        mImageViewResto = (ImageView) findViewById(R.id.resto_img);
        tvRestoName = (TextView) findViewById(R.id.resto_name_detail_activity);
        tvRestoTypeAndAdresses = (TextView) findViewById(R.id.resto_type_detail_detail_activity);
        String urlPart1 = "https://maps.googleapis.com/maps/api/place/photo?maxheigth=500&maxwidth=800&photo_reference=";
        String urlPart2 = restoPhotoUrl;
        String urlPart3 = "&key=AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08";
        urlConcat = urlPart1 + urlPart2 + urlPart3;

        setfabColor();

        fbaRestoChoice = (FloatingActionButton) findViewById(R.id.fab_choice_resto);
        fbaRestoChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser = mUserViewModel.getCurrentUser();
                // Get uid of user on logged
                String userUid = mUserViewModel.getCurrentUser().getUid();
                // Get userRestoId on firebaseFirestore
                DocumentReference userDocumentRef = mUserViewModel.getUsersCollection().document(userUid);
                userDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                userLastRestoId = document.getString("userRestoId");

                                if (userLastRestoId == null) {
                                    //Set fab icon color green
                                    fbaRestoChoice.setImageTintList(ColorStateList.valueOf(getApplicationContext()
                                            .getResources().getColor(R.color.fab_green))); //setImageDrawable(
                                    //Ajouter user.setRestoId(restoId)
                                    //Set restoId with actual restoIdChoice
                                    userDocumentRef.update("userRestoId", restoId);
                                    userDocumentRef.update("userRestoName", restoName);
                                    userDocumentRef.update("userRestoType", restoType);
                                } else {//restoId != null
                                    if (userLastRestoId.equals(restoId)){ // It is a same restaurant)
                                        //Set fab icon color gray
                                        fbaRestoChoice.setImageTintList(ColorStateList.valueOf(getApplicationContext()
                                                .getResources().getColor(R.color.fab_gray)));
                                        // usersInter.remove(firebaseUser);
                                        userDocumentRef.update("userRestoId", "NoRestoChoice");
                                        userDocumentRef.update("userRestoName", "restoNameCreated");
                                        userDocumentRef.update("userRestoType", "restoTypeCreated");
                                    } else {//It is not a same restaurant
                                        //Set fab icon color green
                                        fbaRestoChoice.setImageTintList(ColorStateList.valueOf(getApplicationContext()
                                                .getResources().getColor(R.color.fab_green)));
                                        userDocumentRef.update("userRestoId", restoId);
                                        userDocumentRef.update("userRestoName", restoName);
                                        userDocumentRef.update("userRestoType", restoType);
                                    }
                                }
                            }
                        } else {
                            Log.d("LOGGER", "get failed with ", task.getException());
                        }
                        initRecyclerView();
                    }
                });
            }
        });

        configureDetailView();
       // executeHttpRequestWithRetrofit();
        initRecyclerView();

    }

    public void setfabColor() {
        // Get uid of user on logged
        String userUid = mUserViewModel.getCurrentUser().getUid();
        // Get userRestoId on firebaseFirestore
        DocumentReference userDocumentRef = mUserViewModel.getUsersCollection().document(userUid);
        userDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        userLastRestoId = document.getString("userRestoId");
                        if (userLastRestoId.equals(restoId))// It is a same restaurant
                        {//Set fab icon color gray
                            fbaRestoChoice.setImageTintList(ColorStateList.valueOf(getApplicationContext()
                                    .getResources().getColor(R.color.fab_green)));
                        } else {//It is not a same restaurant
                            //Set fab icon color green
                            fbaRestoChoice.setImageTintList(ColorStateList.valueOf(getApplicationContext()
                                    .getResources().getColor(R.color.fab_gray)));
                        }
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });
    }

    public void configureDetailView() {
        String restoTypeAndAdresses = restoType +" - " + restoAdresses;
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

    // ------------------------------
    //  HTTP REQUEST (Retrofit Way)
    // ------------------------------

    // 4 - Execute HTTP request and update UI
  /*  private void executeHttpRequestWithRetrofit() {
// A DEPLACER DANS VIEWMODEL
        GooglePlacePhotoApiCallsRepository.fetchRestaurantPhoto(this, RESTO_PHOTO_URL, 400,
                300, "AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08");
    }

    // 2 - Override callback methods


    @Override
    public void onResponse(@Nullable GoogleClass1 results) {
        Log.e("TAG", "onResponse: " + results);

    }

    @Override
    public void onFailure() {

    }*/

    public void initRecyclerView() {

        workmatersViewModel.getAllUser().observe(this, usersLiveData -> {
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