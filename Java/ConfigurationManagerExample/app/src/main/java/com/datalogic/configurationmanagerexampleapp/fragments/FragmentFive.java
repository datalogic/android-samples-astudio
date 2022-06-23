package com.datalogic.configurationmanagerexampleapp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;

import com.datalogic.configurationmanagerexampleapp.databinding.FragmentFiveBinding;
import com.datalogic.device.Intents;
import com.datalogic.device.configuration.ConfigurationManager;
import com.datalogic.device.configuration.EnumProperty;
import com.datalogic.device.configuration.Property;
import com.datalogic.device.configuration.PropertyID;

import java.util.HashMap;

/** "Intent Interface" Fragment **/
public class FragmentFive extends Fragment {
    private static final String TAG = "FragmentFive";

    private static final int DEFAULT_PROPERTY_ID = PropertyID.WIFI_BAND_SELECTION;

    private FragmentFiveBinding         fragmentFiveBinding;
    private ConfigurationManager        configurationManager;
    private BroadcastReceiver           broadcastReceiver;

    private Property selectedProperty;

    public FragmentFive() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        /* Inflate the layout for this fragment */
        fragmentFiveBinding = FragmentFiveBinding.inflate(inflater, container, false);
        configurationManager = new ConfigurationManager(getContext());

        selectedProperty = configurationManager.getPropertyById(DEFAULT_PROPERTY_ID);

        return fragmentFiveBinding.getRoot();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        registerBroadcastReceiver();

        initTextViews();
        initSpinners();
        initButtons();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        /* Unregister the listener when app run another fragment */
        unregisterBroadcastReceiver();
    }

    private void initTextViews(){
        fragmentFiveBinding.f5PropertyName.setText(
            "Property: \n" +
            selectedProperty.getName() +
            " (" + selectedProperty.getId() + ")");
    }

    private void initSpinners(){
        /* Populate Spinner */
        Enum[] propertyValueList = ((EnumProperty)selectedProperty).getEnumConstants();
        ArrayAdapter<Object> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, propertyValueList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentFiveBinding.f5PropertyValuesList.setAdapter(adapter);
        fragmentFiveBinding.f5PropertyValuesList.setSelection(0); //by default select the first element
    }

    private void initButtons(){
        fragmentFiveBinding.f5CommitButton.setOnClickListener(v -> {
            /* Commit only if a new value is selected */
            if (!fragmentFiveBinding.f5PropertyValuesList.getSelectedItem().equals(selectedProperty.get())){
                String selectedItem = fragmentFiveBinding.f5PropertyValuesList.getSelectedItem().toString();

                /*  Commit with intent interface require a map<Integer, Object> with the PropertyID
                    and the new value of the all properties to set.

                    The action intent is "ACTION_CONFIGURATION_COMMIT", while map is passed to intent
                    as extra with "EXTRA_CONFIGURATION_CHANGED_MAP
                */

                /* Create the map with the list of key-values to set (in the example only one) */
                HashMap<Integer, String> map = new HashMap<>();
                map.put(DEFAULT_PROPERTY_ID, selectedItem);

                /* Create the commit intent and sens broadcast */
                Intent commitIntent = new Intent();
                commitIntent.setAction(Intents.ACTION_CONFIGURATION_COMMIT);
                commitIntent.putExtra(Intents.EXTRA_CONFIGURATION_CHANGED_MAP, map);
                requireActivity().sendBroadcast(commitIntent);

                updateLogTextBox("New value: " + selectedItem);
            }else{
                updateLogTextBox("Value already set. Choose another!");
            }
        });

    }

    private void registerBroadcastReceiver(){
        /* Create the broadcast receiver that will print the action received */
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String logString = "intent : " + intent.getAction();
                Log.d(TAG, logString);
                updateLogTextBox(logString);
            }
        };

        /*  Register broadcast receiver to two events:
            - ACTION_CONFIGURATION_CHANGED: occurs when there is a change in configuration
            - ACTION_CONFIGURATION_BOOT_REQUIRED: occurs when it required a reboot to apply changes */
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intents.ACTION_CONFIGURATION_CHANGED);
        intentFilter.addAction(Intents.ACTION_CONFIGURATION_BOOT_REQUIRED);
        requireActivity().registerReceiver(broadcastReceiver, intentFilter);
    }

    private void unregisterBroadcastReceiver(){
        if (broadcastReceiver != null) {
            requireActivity().unregisterReceiver(broadcastReceiver);
        }
    }

    private void updateLogTextBox(String text){
        fragmentFiveBinding.f5Log.append(text + "\n");
    }
}