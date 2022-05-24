package com.sitadigi.go4lunch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    ImageView mImageViewResto;
    TextView tvRestoId;
    TextView tvRestoName;
    private final String RESTO_NAME = "RESTO_NAME";
    private final String RESTO_ID = "RESTO_ID";
    String restoId;
    String restoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle bundle = getIntent().getExtras();
        restoId = bundle.getString(RESTO_ID);
        restoName= bundle.getString(RESTO_NAME);


        mImageViewResto = (ImageView) findViewById(R.id.resto_img);
        tvRestoId = (TextView) findViewById(R.id.resto_id);
        tvRestoName = (TextView) findViewById(R.id.resto_name1);



    }
    public void configureDetailView(){
        tvRestoName.setText(restoName);
        tvRestoId.setText(restoId);

    }
}