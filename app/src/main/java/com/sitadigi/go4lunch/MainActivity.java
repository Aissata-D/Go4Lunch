package com.sitadigi.go4lunch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.sitadigi.go4lunch.databinding.ActivityMainBinding;
import com.sitadigi.go4lunch.ui.mapView.MapViewViewModel;
import com.sitadigi.go4lunch.utils.MapViewUtils;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView userName;
    TextView userEmail;
    ImageView userPhoto;
    private UserViewModel mUserViewModel;
    private AppBarConfiguration mAppBarConfiguration;
    private MapViewViewModel mapViewViewModel;
    MapViewUtils mMapViewUtils;
    private boolean locationPermissionGranted;
    FusedLocationProviderClient fusedLocationProviderClient;
    private String restoId;
    private String restoName;
    LatLng restoLatLng;
    CardView mCardViewAutocomplete;
    ImageView imgSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.sitadigi.go4lunch.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        mCardViewAutocomplete = binding.appBarMain.autocompleteCardview;
        imgSearch = binding.appBarMain.toolbarSearchBar;
        mCardViewAutocomplete.setVisibility(View.GONE);
        mUserViewModel = UserViewModel.getInstance();

        initViewModel();
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCardViewAutocomplete.setVisibility(View.VISIBLE);
                imgSearch.setVisibility(View.GONE);
            }
        });

        // Manage Navigation menus
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        BottomNavigationView bottomNavigation = binding.bottomNavigation;

        //////////////////////////API PLACE AUTOCOMPLETE//////////////////////////////////
        // Create a RectangularBounds object.
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(45.7714678,4.8901636),
                new LatLng(45.7714678,4.8901636));
        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(), getString(R.string.google_map_api_key));
        }
        PlacesClient placesClient = Places.createClient(this);
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        //Set search contry
        assert autocompleteFragment != null;
        autocompleteFragment.setCountry("fr");
        autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);
        autocompleteFragment.setHint("search restaurant");
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME
                , Place.Field.LAT_LNG));
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.e("TAG", "Place: " + place.getName() + ", placeID : " + place.getId());
                restoName = place.getName();
                restoLatLng = place.getLatLng();
                mapViewViewModel.setSearchLatLngMutableLiveData(restoLatLng);
                mapViewViewModel.setSearchPlaceNameMutableLiveData(restoName);
                }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });
// Custom Button clear autocomplete
        autocompleteFragment.getView().findViewById(com.google.android.libraries.places
                        .R.id.places_autocomplete_clear_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        autocompleteFragment.setText("");
                                    restoName = null;
                                    restoLatLng = null;
                                    mapViewViewModel.setSearchLatLngMutableLiveData(restoLatLng);
                                    mapViewViewModel.setSearchPlaceNameMutableLiveData(restoName);
                                    mCardViewAutocomplete.setVisibility(View.GONE);
                                    imgSearch.setVisibility(View.VISIBLE);
                    }
                });

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
      /*  AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                .setLocationBias(bounds)
                //.setLocationRestriction(bounds)
                .setOrigin(new LatLng(45.7714678,4.8901636))
                .setCountries("FR")
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setSessionToken(token)
                .setQuery("RUE Flachet")
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                Log.e("TAG", prediction.getPlaceId());
                Log.e("TAG", prediction.getPrimaryText(null).toString());
            }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e("TAG", "Place not found: " + apiException.getStatusCode());
            }
        });*/
        /////////////////////////AUTOCOMPLETE END

        // Passing each menu ID as a set of Ids because each // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery,
                R.id.menu_map_view, R.id.menu_list_view, R.id.menu_workmates
        )
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //Passing navigationView menu to a navigation controller
        NavigationUI.setupWithNavController(navigationView, navController);
        //Passing bottomNavigation menu to a navigation controller
        NavigationUI.setupWithNavController(bottomNavigation, navController);
        //FOR navigation header // Allow to access to a headerNavigationView
        View header;
        // View header = navigationView1.inflateHeaderView(R.layout.nav_header_main);
        int i = navigationView.getHeaderCount();
        if (i > 0) {
            // avoid NPE by first checking if there is at least one Header View available
            header = navigationView.getHeaderView(0);
        } else {
            header = navigationView.inflateHeaderView(R.layout.nav_header_main);
        }
        navigationView.getMenu().findItem(R.id.nav_slideshow).setOnMenuItemClickListener(menuItem -> {
            showAlertDialogSinOut(this);
            return true;
        });

        //Instanciate views
        userName = (TextView) header.findViewById(R.id.name_user);
        userEmail = (TextView) header.findViewById(R.id.email_user);
        userPhoto = (ImageView) header.findViewById(R.id.img_user);
        mUserViewModel.updateUIWithUserData(this, userName, userEmail, userPhoto);
    }

    private void showAlertDialogSinOut(Context context) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("SIGN OUT")
                .setMessage("Are you sure you want to sign out ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mUserViewModel.signOut(MainActivity.this)
                                // after sign out is executed we are redirecting on LoginActivity
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // below method is used after logout from device.
                                Toast.makeText(MainActivity.this, "User Signed Out", Toast.LENGTH_SHORT).show();
                                // Return to LoginActivity via an intent.
                                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(i);
                            }
                        });
                    }

                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private  void initViewModel(){
        mapViewViewModel = new ViewModelProvider(this).get(MapViewViewModel.class);
        mMapViewUtils = new MapViewUtils(this.mapViewViewModel, getApplicationContext(),
                this.locationPermissionGranted, MainActivity.this);
        mapViewViewModel.loadLocationMutableLiveData(getApplicationContext(),MainActivity.this,mapViewViewModel);
        mapViewViewModel.getLocationMutableLiveData();
    }
}