package com.datalogic.configurationmanagerexampleapp.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.datalogic.configurationmanagerexampleapp.databinding.FragmentOneBinding;
import com.datalogic.device.configuration.*;

import java.util.Arrays;

/** "How To Manage an Enum" Fragment **/
public class FragmentOne extends Fragment {
    private static final String TAG = "FragmentOne";

    private final int       DEFAULT_ENUM_PROPERTY_ID = PropertyID.POWER_OFF_CHARGING_MODE_POLICY; //1572866;
    private final String    DEFAULT_STRING_PROPERTY_ID = "POWER_OFF_CHARGING_MODE_POLICY";

    private FragmentOneBinding fragmentOneBinding;

    ConfigurationManager    configurationManager;
    Property                property;
    int                     propertyID;
    String                  propertyName;
    PropertyType            propertyType;
    OffChargingModePolicy   propertyCurrentValue;
    boolean                 isPropertyReadOnly;
    boolean                 needReboot;


    public FragmentOne() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         configurationManager = new ConfigurationManager(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        fragmentOneBinding = FragmentOneBinding.inflate(inflater, container, false);

        fragmentOneBinding.f1Commit.setOnClickListener(v -> {
            /* Get the property selected value and commit setting the value in the device */
            property.set(fragmentOneBinding.f1EnumList.getSelectedItem());

            if(configurationManager.commit() == ConfigException.SUCCESS) {
                Toast.makeText(getContext(), "Change Committed", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "Commit Fail", Toast.LENGTH_SHORT).show();
            }

            /* Reload all to show the changes */
            runAllSteps();
        });

        return fragmentOneBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        runAllSteps();
    }

    private void runAllSteps(){
        /*  Select the enum property: getPropertyById() and get getPropertyByName() provide the
            same result */
        step1_getDefaultEnumProperty();

        /*  Verify if property is supported and in case of true visualize: ID, name, type,
            current state and if it's read only or not */
        step2_getPropertyInfo();

        /* Get property enum type and available values */
        step3_getAllowedPropertyValues();
    }


    private void step1_getDefaultEnumProperty(){
        /* Select the enum property: getPropertyById() and get getPropertyByName() provide the
        same result */
        property = configurationManager.getPropertyById(DEFAULT_ENUM_PROPERTY_ID);
        Log.d(TAG, "getPropertyId(int id) and getPropertyByName(String name) provide the same result!");
        Log.d(TAG, "getPropertyById(int id): " + property);
        Log.d(TAG, "getPropertyByName(String name): " + configurationManager.getPropertyByName(DEFAULT_STRING_PROPERTY_ID));
    }

    private void step2_getPropertyInfo() {
     /*  Verify if property is supported and in case of true visualize: ID, name, type,
            current state and if it's read only or not */
        boolean isPropertySupported = property.isSupported();
        if (!isPropertySupported) {
            Log.d(TAG, "Property is not supported");
        } else {
            propertyID = property.getId();
            propertyName = property.getName();
            propertyType = property.getType();
            propertyCurrentValue = (OffChargingModePolicy) property.get();
            isPropertyReadOnly = property.isReadOnly();
            needReboot = property.getNeedReboot();

            /* Set chosen property as title */
            fragmentOneBinding.f1PropertyTitle.setText("Property: " + propertyName);

            String text = "Property is supported:\n" +
                    "• ID: " + propertyID + "\n" +
                    "• Name: " + propertyName + "\n" +
                    "• Type: " + propertyType + "\n" +
                    "• State: " + propertyCurrentValue + "\n" +
                    "• Read only?: " + isPropertyReadOnly + "\n" +
                    "• Need reboot?: " + needReboot + "\n";
            Log.d(TAG,text);

            /* Update DisplayText */
            fragmentOneBinding.f1DisplayProperty.setText(text);

        }
    }

    private void step3_getAllowedPropertyValues(){
        /*  Because of property.get() returns an object property, it have to cast it to the
            right Enum class.

            A property of type Enum is implemented by EnumProperty. This class is inherited by:
                - DualSimPreferredCallsAndSms
                - DualSimPreferredData
                - DualSimStatus
                - OffChargingModePolicy
                - PropertyType
                - TouchMode
                - UsbFunction
                - Wifi802Dot11Mode
                - WifiBandSelection
                - WifiPowerSave
                - WifiRoamingProfile

            To get the right enum type cast the property to EnumProperty mother class and then use method getEnum()
            to know which type of enum is
        */

        Class enumType = ((EnumProperty)property).getEnum();
        Enum[] enumAvailableValues = ((EnumProperty)property).getEnumConstants();

        String text =
                "• Enum type: " + enumType + "\n" +
                "• Available enums: " + Arrays.toString(enumAvailableValues);

        Log.d(TAG, text);

        /* Update Display Text*/
        fragmentOneBinding.f1DisplayProperty.append(text);

        /* Populate Adapter */
        ArrayAdapter<Enum> adapter = new ArrayAdapter<Enum>(getContext(), android.R.layout.simple_spinner_dropdown_item, enumAvailableValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentOneBinding.f1EnumList.setAdapter(adapter);
        fragmentOneBinding.f1EnumList.setSelection(-1);
    }

}