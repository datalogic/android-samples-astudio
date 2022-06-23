package com.datalogic.configurationmanagerexampleapp.provider;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.datalogic.configurationmanagerexampleapp.R;
import com.datalogic.configurationmanagerexampleapp.fragments.FragmentFive;
import com.datalogic.configurationmanagerexampleapp.fragments.FragmentFour;
import com.datalogic.configurationmanagerexampleapp.fragments.FragmentOne;
import com.datalogic.configurationmanagerexampleapp.fragments.FragmentSix;
import com.datalogic.configurationmanagerexampleapp.fragments.FragmentThree;
import com.datalogic.configurationmanagerexampleapp.fragments.FragmentTwo;
import com.datalogic.configurationmanagerexampleapp.fragments.FragmentZero;

import java.util.HashMap;

public class FragmentProvider {
    private static final String TAG = "FragmentProvider";

    public enum FragmentType{
        FRAGMENT_0,
        FRAGMENT_1,
        FRAGMENT_2,
        FRAGMENT_3,
        FRAGMENT_4,
        FRAGMENT_5,
        FRAGMENT_6
    }

    private final HashMap<FragmentType, Fragment> fragmentLUT = new HashMap<>();
    public Fragment getFragment(FragmentType fragmentType){
        if (fragmentLUT.get(fragmentType) == null){
            switch (fragmentType){
                case FRAGMENT_0:
                    fragmentLUT.put(fragmentType, new FragmentZero());
                    break;
                case FRAGMENT_1:
                    fragmentLUT.put(fragmentType, new FragmentOne());
                    break;
                case FRAGMENT_2:
                    fragmentLUT.put(fragmentType, new FragmentTwo());
                    break;
                case FRAGMENT_3:
                    fragmentLUT.put(fragmentType, new FragmentThree());
                    break;
                case FRAGMENT_4:
                    fragmentLUT.put(fragmentType, new FragmentFour());
                    break;
                case FRAGMENT_5:
                    fragmentLUT.put(fragmentType, new FragmentFive());
                    break;
                case FRAGMENT_6:
                    fragmentLUT.put(fragmentType, new FragmentSix());
            }
            Log.d(TAG, "created fragment " + fragmentType.name());
        }
        return fragmentLUT.get(fragmentType);
    }

    public void loadFragment(FragmentActivity activity, FragmentType fragmentType) {
        Fragment requiredFragment = getFragment(fragmentType);
        activity.getSupportFragmentManager()
                .beginTransaction().replace(R.id.flFragment, requiredFragment)
                .commitAllowingStateLoss();
        Log.d(TAG, "loaded fragment " + fragmentType.name());
    }

}
