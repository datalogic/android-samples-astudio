package com.datalogic.configurationmanagerexampleapp.listener;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.datalogic.configurationmanagerexampleapp.provider.FragmentProvider;
import com.datalogic.configurationmanagerexampleapp.provider.FragmentProvider.FragmentType;
import com.datalogic.configurationmanagerexampleapp.R;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public class NavigationDrawerListener implements NavigationView.OnNavigationItemSelectedListener{

    private FragmentProvider _fragmentProvider;
    private WeakReference<FragmentActivity> _activityWeakReference;
    public NavigationDrawerListener(FragmentActivity activity, FragmentProvider fragmentProvider){
        this._fragmentProvider = fragmentProvider;
        this._activityWeakReference = new WeakReference<>(activity);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        int itemId = item.getItemId();
        FragmentType tappedFragment = FragmentType.FRAGMENT_0;

        if (itemId == R.id.menu_fragment1){
            tappedFragment = FragmentType.FRAGMENT_1;
        }else if (itemId == R.id.menu_fragment2){
            tappedFragment = FragmentType.FRAGMENT_2;
        }else if (itemId == R.id.menu_fragment3) {
            tappedFragment = FragmentType.FRAGMENT_3;
        }else if (itemId == R.id.menu_fragment4) {
            tappedFragment = FragmentType.FRAGMENT_4;
        } else if (itemId == R.id.menu_fragment5) {
            tappedFragment = FragmentType.FRAGMENT_5;
        } else if (itemId == R.id.menu_fragment6) {
            tappedFragment = FragmentType.FRAGMENT_6;
        }

        _fragmentProvider.loadFragment(_activityWeakReference.get(), tappedFragment);

        ((DrawerLayout)_activityWeakReference.get().findViewById(R.id.dl_drawer_layout)).closeDrawers();

        return true;
    }
}
