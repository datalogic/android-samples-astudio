package com.datalogic.configurationmanagerexampleapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.datalogic.configurationmanagerexampleapp.databinding.FragmentSixBinding;
import com.datalogic.device.configuration.ConfigurationManager;
import com.datalogic.device.configuration.Property;
import com.datalogic.device.configuration.PropertyGroup;
import com.datalogic.device.configuration.PropertyGroupID;

/** "Hidden Registration" Fragment **/
public class FragmentSix extends Fragment {
    private static final String TAG = "FragmentSix";
    private FragmentSixBinding          fragmentSixBinding;
    private ConfigurationManager        configurationManager;
    private PropertyGroup               rootPropertyGroup;

    private final int DEFAULT_PROPERTY_GROUP_ID = PropertyGroupID.USB_GROUP;

    public FragmentSix() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        /* Inflate the layout for this fragment */
        fragmentSixBinding = FragmentSixBinding.inflate(inflater, container, false);

        initButtons();

        configurationManager = new ConfigurationManager(getContext());

        /*  Get the tree root of properties tree. It's the first node of tree.
            Method getTreeRoot() give a reference to the TreeRoot, not the the TreeRoot itself.

            In this way, if tree root changes due to a property change, the user will see the change
            with run getTreeRoot() another time.
            This mechanism is called "hidden registration" because there is not notification after
            change.
            */
        rootPropertyGroup  = configurationManager.getTreeRoot();

        return fragmentSixBinding.getRoot();
    }

    @Override
    public void onStart() { super.onStart();}


    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void initButtons(){
        fragmentSixBinding.f6Update.setOnClickListener(v ->{
            PropertyGroup selectedPropertyGroup = rootPropertyGroup.getPropertyGroupById(DEFAULT_PROPERTY_GROUP_ID);

            fragmentSixBinding.f6PropertyGroupView.setText(
                    "Property Group: " +
                    selectedPropertyGroup.getName() +
                    " (" + selectedPropertyGroup.getId() + ")\n");

            for (Property property : selectedPropertyGroup.getProperties()){
                fragmentSixBinding.f6PropertyGroupView.append("• " + property.getName() + ": " + property.get() + "\n");
            }

        });

    };
}