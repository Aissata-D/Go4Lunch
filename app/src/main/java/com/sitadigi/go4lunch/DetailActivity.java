package com.sitadigi.go4lunch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

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
import com.sitadigi.go4lunch.utils.GooglePlacePhotoApiCalls;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

import java.net.URL;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity implements GooglePlacePhotoApiCalls.Callbacks{
    ImageView mImageViewResto;
    TextView tvRestoId;
    TextView tvRestoName;
    TextView tvRestoTypeAndAdresses;
    TextView tvRestoOpeningHours;
    FloatingActionButton fbaRestoChoice;
    public static final String RESTO_NAME = "RESTO_NAME";
  //  public static final String RESTO_ID = "RESTO_ID";
    public static final String RESTO_PHOTO_URL = "RESTO_PHOTO_URL";
    public static final String RESTO_OPENINGHOURS = "RESTO_OPENINGHOURS";
    public static final String RESTO_TYPE_ADRESSES = "RESTO_TYPE";
    public static final String RESTO_ADRESSES = "RESTO_ADRESSES";
    public static final String RESTO_ID = null;


    String userLastRestoId;
    String restoId;
    String restoName;
    String restoPhotoUrl;
    String restoTypeAndAdresses;
    Boolean isRestoOpeningHours;

    StringBuilder stringBuilderURL;
    URL mURL;
    String urlConcat;
    UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

       // mUserViewModel  = new ViewModelProvider(this).get(UserViewModel.class);
        mUserViewModel = new UserViewModel();
        restoId = getIntent().getStringExtra(RESTO_ID);
        restoName= getIntent().getStringExtra(RESTO_NAME);
        restoPhotoUrl = getIntent().getStringExtra(RESTO_PHOTO_URL);
        restoTypeAndAdresses = getIntent().getStringExtra(RESTO_TYPE_ADRESSES);
        //isRestoOpeningHours = getIntent().getBooleanExtra(RESTO_OPENINGHOURS,false);



        mImageViewResto = (ImageView) findViewById(R.id.resto_img);
        //tvRestoId = (TextView) findViewById(R.id.resto_id);
        tvRestoName = (TextView) findViewById(R.id.resto_name_detail_activity);
        tvRestoTypeAndAdresses = (TextView) findViewById(R.id.resto_type_detail_detail_activity);
        String urlPart1 = "https://maps.googleapis.com/maps/api/place/photo?maxheigth=500&maxwidth=800&photo_reference=";
        String urlPart2 = restoPhotoUrl;
        String urlPart3 = "&key=AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08";
        urlConcat = urlPart1 + urlPart2 + urlPart3;

        fbaRestoChoice = (FloatingActionButton) findViewById(R.id.fab_choice_resto);
        fbaRestoChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser = mUserViewModel.getCurrentUser();
                // Get uid of user on logged
                String userUid = mUserViewModel.getUsersCollection().document().getId();
                // Get userRestoId on firebaseFirestore
                DocumentReference userDocumentRef = mUserViewModel.getUsersCollection().document(userUid);
                userDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                userLastRestoId = document.getString("userRestoId");

                            } else {
                                userLastRestoId = "restoIDdNULL";
                            }
                        } else {
                            Log.d("LOGGER", "get failed with ", task.getException());
                        }
                    }
                });

                if(userLastRestoId == null){
                    //Set fab icon color green
                    fbaRestoChoice.setImageTintList(ColorStateList.valueOf(getApplicationContext()
                            .getResources().getColor(R.color.fab_green))); //setImageDrawable(
                          //  getResources().getDrawable(R.drawable.ic_baseline_check_circle_24_green));
                    //Ajouter user.setRestoId(restoId)
                    //Set restoId with actual restoIdChoice
                    userDocumentRef.update("userRestoId",restoId);
                }else{//restoId != null
                    if(userLastRestoId == restoId)// C'est le meme resto)
                     {
                        //Set fab icon color gray
                         fbaRestoChoice.setImageTintList(ColorStateList.valueOf(getApplicationContext()
                                 .getResources().getColor(R.color.fab_gray)));
                        //user.setRestoId(null)
                         userDocumentRef.update("userRestoId","restoIDdNULL");
                    }else{//c'est pas le meme resto
                         //Set fab icon color green
                         fbaRestoChoice.setImageTintList(ColorStateList.valueOf(getApplicationContext()
                                 .getResources().getColor(R.color.fab_green)));
                        //user.setRestoId (restoId)
                         userDocumentRef.update("userRestoId",restoId);



                    }
                }
            }
        });
        configureDetailView();
        executeHttpRequestWithRetrofit();

    }

    public void configureDetailView(){
        tvRestoName.setText(restoName);
       // tvRestoId.setText(restoId);
        tvRestoTypeAndAdresses.setText(restoTypeAndAdresses);

        //GLIDE TO SHOW PHOTO
        Glide.with(this)
                .load(getUrl(urlConcat))
                .apply(RequestOptions.noTransformation())
                .centerCrop()
                .into(mImageViewResto);

    }
   Uri getUrl(String base){
        Uri uri = Uri.parse( base );
        return uri;
    }

    // ------------------------------
    //  HTTP REQUEST (Retrofit Way)
    // ------------------------------

    // 4 - Execute HTTP request and update UI
    private void executeHttpRequestWithRetrofit(){

        GooglePlacePhotoApiCalls.fetchRestaurantPhoto(this, RESTO_PHOTO_URL,400,
                300,"AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08");

    }

    // 2 - Override callback methods



    @Override
    public void onResponse(@Nullable GoogleClass1 results) {
        Log.e("TAG", "onResponse: "+results );

    }

    @Override
    public void onFailure() {

    }
}