package com.datalogic.configurationmanagerexampleapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import com.datalogic.configurationmanagerexampleapp.provider.FragmentProvider;
import com.datalogic.configurationmanagerexampleapp.listener.NavigationDrawerListener;
import com.datalogic.configurationmanagerexampleapp.R;
import com.datalogic.configurationmanagerexampleapp.databinding.ActivityMainBinding;
import com.datalogic.configurationmanagerexampleapp.databinding.NavigationDrawerBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity.java";
    private ActivityMainBinding activityMainBinding;
    private NavigationDrawerBinding navigationDrawerBinding;

    final FragmentProvider fragmentProvider = new FragmentProvider();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Layout binding */
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        navigationDrawerBinding=NavigationDrawerBinding.inflate(getLayoutInflater());
        setContentView(navigationDrawerBinding.getRoot());

        /* Custom appbar and navigation drawer */
        setNavigationDrawer();
        setAppBar();

        /* Start from help activity */
        fragmentProvider.loadFragment(this, FragmentProvider.FragmentType.FRAGMENT_0);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    private void setNavigationDrawer(){
        NavigationView navView = navigationDrawerBinding.dlNavigationView;
        navView.setNavigationItemSelectedListener(new NavigationDrawerListener(this, fragmentProvider));
        navView.setCheckedItem(R.id.menu_fragment0);
    }

    private void setAppBar(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_appbar);

        ImageButton menu_button = (ImageButton) (getSupportActionBar().getCustomView().findViewById(R.id.t_menu_icon));
        menu_button.setOnClickListener(v -> {
            DrawerLayout drawerLayout = navigationDrawerBinding.dlDrawerLayout;
            if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
            }else{
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


    }

}