package com.sitadigi.go4lunch;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.sitadigi.go4lunch.databinding.ActivityMainBinding;
import com.sitadigi.go4lunch.factory.MainViewModelFactory;
import com.sitadigi.go4lunch.factory.UserViewModelFactory;
import com.sitadigi.go4lunch.repository.GoogleMapApiCallsRepository;
import com.sitadigi.go4lunch.repository.UserRepository;
import com.sitadigi.go4lunch.utils.ShowSignOutDialogueAlertAndDetailActivity;
import com.sitadigi.go4lunch.utils.UtilsMapView;
import com.sitadigi.go4lunch.viewModel.MainViewViewModel;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    TextView userName;
    TextView userEmail;
    ImageView userPhoto;
    LatLng restaurantLatLng;
    CardView mCardViewAutocomplete;
    ImageView imgSearch;
    UtilsMapView mUtilsMapView;
    ShowSignOutDialogueAlertAndDetailActivity mShowSignOutDialogueAlertAndDetailActivity;
    private UserViewModel mUserViewModel;
    private AppBarConfiguration mAppBarConfiguration;
    private MainViewViewModel mMainViewViewModel;
    private boolean locationPermissionGranted;
    private String restaurantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.sitadigi.go4lunch.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        mCardViewAutocomplete = binding.appBarMain.autocompleteCardview;
        imgSearch = binding.appBarMain.toolbarSearchBar;
        Window window = this.getWindow();
        //clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //finally change the statusBar color
        window.setStatusBarColor(this.getResources().getColor(R.color.orange));
        mCardViewAutocomplete.setVisibility(View.GONE);
        UserRepository userRepository = new UserRepository();
        mUserViewModel = new ViewModelProvider(this, new UserViewModelFactory(userRepository))
                .get(UserViewModel.class);
        mShowSignOutDialogueAlertAndDetailActivity = new ShowSignOutDialogueAlertAndDetailActivity();

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
                new LatLng(45.7714678, 4.8901636),
                new LatLng(45.7714678, 4.8901636));
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_map_api_key));
        }
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        //Set search country
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
                restaurantName = place.getName();
                restaurantLatLng = place.getLatLng();
                mMainViewViewModel.setSearchLatLngMutableLiveData(restaurantLatLng);
                mMainViewViewModel.setSearchPlaceNameMutableLiveData(restaurantName);
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
                        restaurantName = null;
                        restaurantLatLng = null;
                        mMainViewViewModel.setSearchLatLngMutableLiveData(restaurantLatLng);
                        mMainViewViewModel.setSearchPlaceNameMutableLiveData(restaurantName);
                        mCardViewAutocomplete.setVisibility(View.GONE);
                        imgSearch.setVisibility(View.VISIBLE);
                    }
                });
        //////////////////////////END OF API PLACE AUTOCOMPLETE //////////////////////////////////

        // Passing each menu ID as a set of Ids because each // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_gallery,
                R.id.menu_map_view, R.id.menu_list_view, R.id.menu_workmates)
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
        if (i > 0) {  // avoid NPE by first checking if there is at least one Header View available
            header = navigationView.getHeaderView(0);
        } else {
            header = navigationView.inflateHeaderView(R.layout.nav_header_main);
        }
        //Instantiate views
        userName = (TextView) header.findViewById(R.id.name_user);
        userEmail = (TextView) header.findViewById(R.id.email_user);
        userPhoto = (ImageView) header.findViewById(R.id.img_user);
        mUserViewModel.updateUIWithUserData(this, userName, userEmail, userPhoto);

        navigationView.getMenu().findItem(R.id.nav_slideshow).setOnMenuItemClickListener(menuItem -> {
            mShowSignOutDialogueAlertAndDetailActivity.showAlertDialogSinOut(this);
            return true;
        });
        navigationView.getMenu().findItem(R.id.nav_home).setOnMenuItemClickListener(menuItem -> {
            Log.e("TAG", "onCreate: R.id.nav_home clicked");
            mShowSignOutDialogueAlertAndDetailActivity.showDetailActivity(mMainViewViewModel, this, this);
            return true;
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this
                , R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void initViewModel() {
        GoogleMapApiCallsRepository googleMapApiCallsRepository = new GoogleMapApiCallsRepository();
        mMainViewViewModel = new ViewModelProvider(this, new MainViewModelFactory(googleMapApiCallsRepository)).get(MainViewViewModel.class);
        mUtilsMapView = new UtilsMapView(this.mMainViewViewModel, getApplicationContext(),
                this.locationPermissionGranted, MainActivity.this);
        mMainViewViewModel.loadLocationMutableLiveData(getApplicationContext(), MainActivity.this, mMainViewViewModel);
        mMainViewViewModel.getLocationMutableLiveData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainViewViewModel.disposeWhenDestroy();
    }
}