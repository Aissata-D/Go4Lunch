package com.sitadigi.go4lunch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
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
import com.sitadigi.go4lunch.ui.gallery.GalleryFragment;
import com.sitadigi.go4lunch.ui.home.HomeFragment;
import com.sitadigi.go4lunch.ui.listView.ListViewFragment;
import com.sitadigi.go4lunch.ui.mapView.MapViewFragment;
import com.sitadigi.go4lunch.ui.mapView.MapViewViewModel;
import com.sitadigi.go4lunch.ui.workmaters.WorkmatersFragment;
import com.sitadigi.go4lunch.utils.DialogClass;
import com.sitadigi.go4lunch.utils.MapViewUtils;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    TextView userName;
    TextView userEmail;
    ImageView userPhoto;
    DialogClass mDialogClass;
    private UserViewModel mUserViewModel;
    private AppBarConfiguration mAppBarConfiguration;
    private MapViewViewModel mapViewViewModel;
    MapViewUtils mMapViewUtils;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234;
    private boolean locationPermissionGranted;
    Location lastKnownLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    String location;
    private String restoId;
    private String restoName;
    private int menuItemVisible;
    LatLng restoLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.sitadigi.go4lunch.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

         mUserViewModel = UserViewModel.getInstance();
        mDialogClass = new DialogClass();
        initViewModel();

        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        // Manage Navigation menus
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        BottomNavigationView bottomNavigation = binding.bottomNavigation;

        //////////////////////////AUTOCOMPLETE
      // EditText queryText = findViewById(R.id.edit_text);
        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(), getString(R.string.google_map_api_key));
        }
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME
                , Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.e("TAG", "Place: " + place.getName() + ", placeID : " + place.getId());
                restoId = place.getId();
                restoName = place.getName();
                restoLatLng = place.getLatLng();
                List<String> list = Arrays.asList(restoId,restoName);

                mapViewViewModel.getSearchLocationMutableLiveData(restoLatLng);
               // mapViewViewModel.getResultSearchLatLng();
                mapViewViewModel.getSearchMutableLiveData(list);
                /*
                Bundle bundle = new Bundle();
                bundle.putString("RESTO_NAME", restoName);
                bundle.putString("RESTO_ID", restoId);
                Fragment fragmentOnScreen = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);

                //MapViewFragment mapViewFragment1 = fragmentOnScreen instanceof MapViewFragment ?
                //        ((MapViewFragment) fragmentOnScreen) : null;
                MapViewFragment mapViewFragment =  new MapViewFragment();
                ListViewFragment listViewFragment = new ListViewFragment();
                WorkmatersFragment workmatersFragment = new WorkmatersFragment();
               // Bundle bundle = new Bundle();
              //  bundle.putString(TABLET, "TELEPHONE");
                mapViewFragment.setArguments(bundle);
                listViewFragment.setArguments(bundle);
                workmatersFragment.setArguments(bundle);
                NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                //navController.navigate(R.id.menu_map_view, bundle);
                if(menuItemVisible == 0){
                    navController.navigate(R.id.menu_map_view, bundle);
                    Log.e("TAG", "onPlaceSelected: Menu Item "+ menuItemVisible +" "
                            +R.id.menu_map_view );
                }
                if(menuItemVisible == 1){
                    navController.navigate(R.id.menu_list_view, bundle);
                    Log.e("TAG", "onPlaceSelected: Menu Item "+ menuItemVisible +" "
                            +R.id.menu_list_view );

                }
                if(menuItemVisible == 2){
                    navController.navigate(R.id.menu_workmates, bundle);
                    Log.e("TAG", "onPlaceSelected: Menu Item "+ menuItemVisible +" "
                            +R.id.menu_workmates );

                }
                if(menuItemVisible == 3){
                    navController.navigate(R.id.menu_map_view, bundle);
                    Log.e("TAG", "onPlaceSelected: Menu Item "+ menuItemVisible +" "
                            +R.id.menu_map_view );
                }
                if(menuItemVisible == 4){
                    navController.navigate(R.id.menu_list_view, bundle);
                    Log.e("TAG", "onPlaceSelected: Menu Item "+ menuItemVisible +" "
                            +R.id.menu_list_view );

                }
                if(menuItemVisible == 5){
                    navController.navigate(R.id.menu_workmates, bundle);
                    Log.e("TAG", "onPlaceSelected: Menu Item "+ menuItemVisible +" "
                            +R.id.menu_workmates );

                }
                bottomNavigation.getMenu().findItem(R.id.menu_map_view).setOnMenuItemClickListener(menuItem -> {
                    //showAlertDialogSinOut(this);
                    navController.navigate(R.id.menu_map_view, bundle); // called fragment with agruments

                    return true;
                });
                bottomNavigation.getMenu().findItem(R.id.menu_list_view).setOnMenuItemClickListener(menuItem -> {
                    //showAlertDialogSinOut(this);
                    navController.navigate(R.id.menu_list_view, bundle); // called fragment with agruments

                    return true;
                });
                bottomNavigation.getMenu().findItem(R.id.menu_workmates).setOnMenuItemClickListener(menuItem -> {
                    //showAlertDialogSinOut(this);
                    navController.navigate(R.id.menu_workmates, bundle); // called fragment with agruments

                    return true;
                });
                //MapViewFragment mapViewFragment = (MapViewFragment) findViewById(R.id.map_view_fragment);
                if (fragmentOnScreen == mapViewFragment) {
                    navController.navigate(R.id.menu_map_view, bundle); // called fragment with agruments
                }
                if (fragmentOnScreen.equals(listViewFragment)) {
                    navController.navigate(R.id.menu_list_view, bundle); // called fragment with agruments
                }
                if (fragmentOnScreen instanceof WorkmatersFragment) {
                    navController.navigate(R.id.menu_workmates, bundle); // called fragment with agruments
                }
                if (fragmentOnScreen instanceof HomeFragment) {
                    navController.navigate(R.id.menu_map_view, bundle); // called fragment with agruments
                }
                if (fragmentOnScreen instanceof GalleryFragment) {
                    navController.navigate(R.id.menu_map_view, bundle); // called fragment with agruments
                }
                */

                //navController.navigate(R.id.menu_list_view, bundle); // called fragment with agruments
                //navController.navigate(R.id.menu_workmates, bundle); // called fragment with agruments
            }


            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });


        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        // Create a RectangularBounds object.
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(45.7714678,4.8901636),
                new LatLng(45.7714678,4.8901636));
        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                .setLocationBias(bounds)
                //.setLocationRestriction(bounds)
                .setOrigin(new LatLng(45.7714678,4.8901636))
                .setCountries("FR")
                .setTypeFilter(TypeFilter.ESTABLISHMENT/* ADDRESS*/)
                .setSessionToken(token)
                .setQuery("RUE Flachet"/*queryText.getText().toString()*/)
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
        });



        /////////////////////////AUTOCOMPLETE FIN



        // Passing each menu ID as a set of Ids because each // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery,
                R.id.menu_map_view, R.id.menu_list_view, R.id.menu_workmates
        )
                .setOpenableLayout(drawer)
                .build();

        Bundle bundle = new Bundle();
        bundle.putString("RESTO_NAME", restoName);
        bundle.putString("RESTO_ID", restoId);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
      //  navController.navigate(R.id.menu_map_view, bundle); // called fragment with agruments
       // navController.navigate(R.id.menu_list_view, bundle); // called fragment with agruments
        //navController.navigate(R.id.menu_workmates, bundle); // called fragment with agruments
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



    // For menu settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
        //Bundle bundle = new Bundle();
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
        // getLocationPermission();
        //getDeviceLocation();

    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        menuItemVisible = item.getItemId();
        return true;
    }

  /*  public void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
      /*  if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    public  void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */

    /*    try {

            if (locationPermissionGranted) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    return;
                }
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(MainActivity.this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            location = lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude();
                            //moveCamera();
                            mapViewViewModel.loadRestaurentData(location);
                            mapViewViewModel.getRestaurent();
                            Log.e("TAG", "onComplete: MainActivity" );
                        } else {
                            Log.e("TAG", "Exception: %s MainActivity", task.getException());

                        }

                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }

    }*/



}