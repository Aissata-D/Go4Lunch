package com.sitadigi.go4lunch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sitadigi.go4lunch.models.GoogleClass1;
import com.sitadigi.go4lunch.utils.GoogleMapApiCalls;
import com.sitadigi.go4lunch.utils.GooglePlacePhotoApiCalls;

import java.net.URL;
import java.util.Objects;

import retrofit2.http.Url;

public class DetailActivity extends AppCompatActivity implements GooglePlacePhotoApiCalls.Callbacks{
    ImageView mImageViewResto;
    TextView tvRestoId;
    TextView tvRestoName;
    TextView tvRestoTypeAndAdresses;
    TextView tvRestoOpeningHours;
    public static final String RESTO_NAME = "RESTO_NAME";
    public static final String RESTO_ID = "RESTO_ID";
    public static final String RESTO_PHOTO_URL = "RESTO_PHOTO_URL";
    public static final String RESTO_OPENINGHOURS = "RESTO_OPENINGHOURS";
    public static final String RESTO_TYPE_ADRESSES = "RESTO_TYPE";
    public static final String RESTO_ADRESSES = "RESTO_ADRESSES";


    String restoId;
    String restoName;
    String restoPhotoUrl;
    String restoTypeAndAdresses;
    Boolean isRestoOpeningHours;

    StringBuilder stringBuilderURL;
    URL mURL;
    String urlConcat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

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