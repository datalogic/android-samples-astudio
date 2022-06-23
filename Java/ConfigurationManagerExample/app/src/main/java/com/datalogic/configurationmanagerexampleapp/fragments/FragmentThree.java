package com.datalogic.configurationmanagerexampleapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.datalogic.configurationmanagerexampleapp.databinding.FragmentThreeBinding;
import com.datalogic.device.configuration.ConfigurationManager;
import com.datalogic.device.configuration.EnumProperty;
import com.datalogic.device.configuration.Property;
import com.datalogic.device.configuration.PropertyGroup;
import com.datalogic.device.configuration.PropertyID;

/** "Set On Not Supported Properties" Fragment **/
public class FragmentThree extends Fragment {
    private final String TAG = "FragmentThree";
    private FragmentThreeBinding fragmentThreeBinding;

    private final int DEFAULT_READ_ONLY_PROPERTY_ID = PropertyID.WIFI_COUNTRY_CODE;
    private final int DEFAULT_UNSUPPORTED_PROPERTY_ID = PropertyID.USB_CURRENT_FUNCTION;


    private ConfigurationManager    configurationManager;
    private PropertyGroup propertyGroupTreeRoot;

    public FragmentThree() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        fragmentThreeBinding = FragmentThreeBinding.inflate(inflater, container, false);

        configurationManager = new ConfigurationManager(getContext());
        propertyGroupTreeRoot = configurationManager.getTreeRoot();

        initTextViews();
        initSpinners();
        setButtonsListener();

        return fragmentThreeBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initTextViews(){
        /* Read only property */
        fragmentThreeBinding.f3ReadOnlyPropertyName.setText(
                "Read only property: \n" +
                configurationManager.getPropertyById(DEFAULT_READ_ONLY_PROPERTY_ID).getName() +
                " (" + configurationManager.getPropertyById(DEFAULT_READ_ONLY_PROPERTY_ID).getId() + ")");

        /* Unsupported property */
        fragmentThreeBinding.f3UnsupportedPropertyName.setText(
                "Unsupported property: \n" +
                configurationManager.getPropertyById(DEFAULT_UNSUPPORTED_PROPERTY_ID).getName() +
                " (" + configurationManager.getPropertyById(DEFAULT_UNSUPPORTED_PROPERTY_ID).getId() + ")");
    }


    private void initSpinners(){
        /* Define a set of values for read only property and populate spinner */
        String[] readOnlyValuesSet = new String[]{"IT", "US", "UK"};
        populateSpinner(fragmentThreeBinding.f3ReadOnlyPropertyValues, readOnlyValuesSet);

        /* Define a set of values for unsupported property and populate spinner */
        Enum[] unsupportedPropertyValuesSet = ((EnumProperty)configurationManager.getPropertyById(DEFAULT_UNSUPPORTED_PROPERTY_ID)).getEnumConstants();
        populateSpinner(fragmentThreeBinding.f3UnsupportedPropertyValues, unsupportedPropertyValuesSet);
    }

    private void setButtonsListener(){
        fragmentThreeBinding.f3ReadOnlySet.setOnClickListener(v -> {
            Property property = configurationManager.getPropertyById(DEFAULT_READ_ONLY_PROPERTY_ID);
            try{
                /* Try to set a read-only property: it will generate an exception! */
                property.set(fragmentThreeBinding.f3ReadOnlyPropertyValues.getSelectedItem());
                configurationManager.commit();
            }catch(Exception e){
                Toast.makeText(requireActivity().getApplicationContext(), "Exception: " + e, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Set read only property, exception: " + e);
            }
        });

        fragmentThreeBinding.f3UnsupportedSet.setOnClickListener(v -> {
            Property property = configurationManager.getPropertyById(DEFAULT_UNSUPPORTED_PROPERTY_ID);
            try{
                /* Try to set an unsupported property: it will generate an exception! */
                property.set(fragmentThreeBinding.f3UnsupportedPropertyValues.getSelectedItem());
                configurationManager.commit();
            }catch(Exception e)
            {
                Toast.makeText(requireActivity().getApplicationContext(), "Exception: " + e, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Set unsupported property, exception: " + e);
            }
        });
    }

    private <T> void populateSpinner(Spinner spinner, Object[] array){
        /* Populate Adapter */
        ArrayAdapter<Object> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0); //by default select the first element
    }
}