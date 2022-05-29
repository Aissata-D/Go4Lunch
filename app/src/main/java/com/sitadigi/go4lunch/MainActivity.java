package com.sitadigi.go4lunch;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.sitadigi.go4lunch.databinding.ActivityMainBinding;
import com.sitadigi.go4lunch.models.GoogleClass1;
import com.sitadigi.go4lunch.ui.mapView.MapViewViewModel;
import com.sitadigi.go4lunch.utils.DialogClass;
import com.sitadigi.go4lunch.utils.MapViewUtils;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

public class MainActivity extends AppCompatActivity {


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
        // Passing each menu ID as a set of Ids because each // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery,
                R.id.menu_map_view, R.id.menu_map_view, R.id.menu_workmates
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