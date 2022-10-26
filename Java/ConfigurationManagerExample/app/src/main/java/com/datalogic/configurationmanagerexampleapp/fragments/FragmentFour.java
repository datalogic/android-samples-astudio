package com.datalogic.configurationmanagerexampleapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;

import com.datalogic.configurationmanagerexampleapp.databinding.FragmentFourBinding;
import com.datalogic.device.configuration.ConfigurationChangeListener;
import com.datalogic.device.configuration.ConfigurationManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** "Register to Listener" Fragment
 * Use listener interface to manage events.
 * Ref. {https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/device/configuration/ConfigurationChangeListener.html}
 * **/
public class FragmentFour extends Fragment {
    private static final String         TAG = "FragmentFour";
    private FragmentFourBinding         fragmentFourBinding;
    private ConfigurationManager        configurationManager;
    private ConfigurationChangeListener configurationChangeListener;

    private List<String> eventList;
    private ArrayAdapter<String> eventListAdapter;

    public FragmentFour() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        /* Inflate the layout for this fragment */
        fragmentFourBinding = FragmentFourBinding.inflate(inflater, container, false);

        configurationManager = new ConfigurationManager(getContext());

        return fragmentFourBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        initEventListView();
        registerToListener();
    }


    @Override
    public void onDetach() {
        super.onDetach();

        /* Unregister the listener when app run another fragment */
        unregisterListener();
    }

    private void registerToListener() {
        /*  The interface is used by the application to listen to configuration changes.
            If a change occurs, onConfigurationChanged will be called to notify that.

            A change can be the consequence of an interaction with the environment (e.g: cradle
            insertion or usb plug) or the change of a property followed by a commit action.

            onConfigurationChanged has a map<Integer, Object> as parameter, where:
            - Integer (key): is the PropertyId of the changed property
            - Object (value): is the new value of property.
        */

        configurationChangeListener = new ConfigurationChangeListener() {
            @Override
            public void onConfigurationChanged(HashMap<Integer, Object> map) {
                String printText = "";
                for (int propID : map.keySet()) {
                    printText += "• Property " + configurationManager.getPropertyById(propID).getName() + " has new value: " + map.get(propID) + "\n";
                    Log.d(TAG, printText);
                    updateEventListView(printText);
                }
            }
        };

        configurationManager.registerListener(configurationChangeListener);
    }

    private void initEventListView(){
        eventList = new ArrayList<>();
        eventListAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, eventList);
        fragmentFourBinding.f4EventListview.setAdapter(eventListAdapter);

    }

    private void updateEventListView(String text){
        requireActivity().runOnUiThread(() -> {
            eventList.add(text);
            eventListAdapter.notifyDataSetChanged();
        });
    }

    private void unregisterListener(){
        Log.d(TAG, "Unregister listener");
        configurationManager.unregisterListener(configurationChangeListener);

    }
}