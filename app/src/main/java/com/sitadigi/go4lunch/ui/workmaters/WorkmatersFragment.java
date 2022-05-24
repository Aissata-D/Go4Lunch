package com.sitadigi.go4lunch.ui.workmaters;

import static android.content.Context.LOCATION_SERVICE;


import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationListener;
import com.sitadigi.go4lunch.databinding.FragmentWorkmatersBinding;


import com.sitadigi.go4lunch.models.GoogleClass1;
import com.sitadigi.go4lunch.utils.GoogleMapApiCalls;


import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WorkmatersFragment extends Fragment implements GoogleMapApiCalls.Callbacks{

    private FragmentWorkmatersBinding binding;
    TextView textView;
    String currentLocation;
    private LocationManager locationManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WorkmatersViewModel workmatersViewModel =
                new ViewModelProvider(this).get(WorkmatersViewModel.class);

        binding = FragmentWorkmatersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


         textView = binding.workmatersText;
        //locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2 * 1000, 1, locationListener);

        this.executeHttpRequestWithRetrofit();
       // workmatersViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // ------------------------------
    //  HTTP REQUEST (Retrofit Way)
    // ------------------------------

    // 4 - Execute HTTP request and update UI
    private void executeHttpRequestWithRetrofit(){
        this.updateUIWhenStartingHTTPRequest();
        GoogleMapApiCalls.fetchResultFollowing(this, "45.771944,4.8901709",1500,
                "restaurant","AIzaSyDsQUD7ukIhqdJYZIQxj535IvrDRrkrH08");
        //"45.771944%2C4.8901709"
    }

    // 2 - Override callback methods

    @Override
    public void onResponse(@Nullable GoogleClass1 results) {
        // 2.1 - When getting response, we update UI
        if (results != null) this.updateUIWithListOfUsers(results);
    }

    @Override
    public void onFailure() {


        // 2.2 - When getting error, we update UI
        this.updateUIWhenStopingHTTPRequest("An error happened !");
    }



            // ------------------
            //  UPDATE UI
            // ------------------



    // 3 - Update UI showing only name of users
    private void updateUIWithListOfUsers(GoogleClass1 results){
        StringBuilder stringBuilder = new StringBuilder();
        //GoogleClass1 result;
       // stringBuilder.append("-"+ results.getResults().get(0).getName() +"\n");
      //  for (GoogleClass1 result : results.getResults()){
            for(int i = 0; i < results.getResults().size(); i++) {
                stringBuilder.append("-" + results.getResults().get(i).getName() + "\n"
                       +"45.771944,4.8901709"+"\n"+
                "currentLocation; " +currentLocation +"\n");
            }
        //}
        updateUIWhenStopingHTTPRequest(stringBuilder.toString());
    }
    private void updateUIWhenStartingHTTPRequest(){
        this.textView.setText("Downloading...");
    }

    private void updateUIWhenStopingHTTPRequest(String response){
        this.textView.setText(response);
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
             currentLocation = location.getLatitude() + "," + location.getLongitude();
        }
    };

}