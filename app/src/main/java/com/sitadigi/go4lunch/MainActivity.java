package com.sitadigi.go4lunch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.sitadigi.go4lunch.databinding.ActivityMainBinding;
import com.sitadigi.go4lunch.utils.DialogClass;
import com.sitadigi.go4lunch.viewModel.UserViewModel;

public class MainActivity extends AppCompatActivity {

    TextView userName;
    TextView userEmail;
    ImageView userPhoto;
    DialogClass mDialogClass;
    private UserViewModel mUserViewModel;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.sitadigi.go4lunch.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        mUserViewModel = UserViewModel.getInstance();
        mDialogClass = new DialogClass();
        //  mMyViewModel = new ViewModelProvider(MainActivity.this).get(UserViewModel.class);

        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserViewModel.signOut(MainActivity.this);
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

}