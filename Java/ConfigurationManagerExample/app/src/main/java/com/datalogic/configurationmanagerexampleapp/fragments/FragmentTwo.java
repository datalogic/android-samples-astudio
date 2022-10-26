package com.datalogic.configurationmanagerexampleapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.datalogic.configurationmanagerexampleapp.R;
import com.datalogic.configurationmanagerexampleapp.databinding.FragmentTwoBinding;
import com.datalogic.device.configuration.ConfigurationManager;
import com.datalogic.device.configuration.Property;
import com.datalogic.device.configuration.PropertyGroup;
import com.datalogic.device.configuration.PropertyGroupID;

import java.util.ArrayList;
import java.util.List;

/** "Show Group Status" Fragment
 * Learn how to get a group status and its related properties.
 * Ref. {https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/device/configuration/ConfigurationManager.html}
 *      {https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/device/configuration/PropertyGroup.html}
 *      {https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/device/configuration/PropertyGroupID.html}
 * **/
public class FragmentTwo extends Fragment {

    private static final String TAG = "FragmentTwo";

    private final int       DEFAULT_ENUM_PROPERTY_GROUP = PropertyGroupID.POWER_OFF_CHARGING_MODE_GROUP;
    private final String    DEFAULT_STRING_PROPERTY_GROUP = "POWER_OFF_CHARGING_MODE_GROUP";

    private FragmentTwoBinding fragmentTwoBinding;

    private ConfigurationManager configurationManager;
    private PropertyGroup propertyGroup;

    public FragmentTwo() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configurationManager = new ConfigurationManager(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        fragmentTwoBinding =  FragmentTwoBinding.inflate(inflater, container, false);

        /* Update property status */
        fragmentTwoBinding.f2UpdateButton.setOnClickListener(v -> {
            clearPropertyTable();
            populateRowsPropertyTable(getStateOfPropertiesInTableFormat());
        });


        runAllSteps();

        return fragmentTwoBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void runAllSteps(){
        step1_getGroupByIdAndName();
        step2_verifyIfDeviceSupportGroup();
        step3_listStateOfAllGroupProperties();
    }

    private void step1_getGroupByIdAndName(){
        /* Select the enum property group: getPropertyGroupById() and get getPropertyGroupByName() provide the
        same result */
        propertyGroup = configurationManager.getPropertyGroupById(DEFAULT_ENUM_PROPERTY_GROUP);
        Log.d(TAG, "getPropertyGroupId(int id) and getPropertyGroupByName(String name) provide the same result!");
        Log.d(TAG, "getPropertyGroupById(int id): " + propertyGroup);
        Log.d(TAG, "getPropertyGroupByName(String name): " + configurationManager.getPropertyByName(DEFAULT_STRING_PROPERTY_GROUP));

        /* Use the name get from SDK as title */
        fragmentTwoBinding.f2PropertyGroupName.setText(
                "Property Group: \n" +
                propertyGroup.getName() +
                " (" + propertyGroup.getId() + ")");
        Log.d(TAG, "Property group name: " + propertyGroup.getName());
    }

    private void step2_verifyIfDeviceSupportGroup(){
        /* Verify the device fully supports the group */
        Log.d(TAG, "Device fully support group? " + propertyGroup.isFullySupported());

        /* Verify the device fully supports the group */
        Log.d(TAG, "" + "Device support group? " + propertyGroup.isFullySupported());

    }

    private void step3_listStateOfAllGroupProperties(){

        /* Populate list of properties */
        populateHeaderPropertyTable(new String[]{"Supported", "Id", "Name", "Type", "State"});
        populateRowsPropertyTable(getStateOfPropertiesInTableFormat());
    }

    private List<String[]> getStateOfPropertiesInTableFormat() {
        /* Show the state of all group properties: the list shows the value of each property group
           property, if available.
        *  Then it's possible to get property name, id, etc using own method as done below */
        List<Property> propertyList = propertyGroup.getProperties();

        Log.d(TAG, "Property List: " + propertyList.toString());

        List<String[]> table_content = new ArrayList<>();
        for (Property property : propertyList){
            table_content.add(new String[]{
                    String.valueOf(property.isSupported()), /* is Supported? */
                    String.valueOf(property.getId()),       /* id */
                    property.getName(),                     /* name */
                    property.getType().toString(),          /* type */
                    property.get().toString()});            /* state */
        }

        return table_content;
    }

    private void populateHeaderPropertyTable(String[] header){
        TableRow row_view = (TableRow) LayoutInflater.from(getContext()).inflate(R.layout.property_table_row, null);

        /* Populate header */
        ((TextView) row_view.findViewById(R.id.row_col_0)).setText(header[0]);
        ((TextView) row_view.findViewById(R.id.row_col_1)).setText(header[1]);
        ((TextView) row_view.findViewById(R.id.row_col_2)).setText(header[2]);
        ((TextView) row_view.findViewById(R.id.row_col_3)).setText(header[3]);
        ((TextView) row_view.findViewById(R.id.row_col_4)).setText(header[4]);

        /* Change colors */
        row_view.setBackgroundColor(getResources().getColor(R.color.dl_table_header));
        ((TextView) row_view.findViewById(R.id.row_col_0)).setTextColor(getResources().getColor(R.color.white));
        ((TextView) row_view.findViewById(R.id.row_col_1)).setTextColor(getResources().getColor(R.color.white));
        ((TextView) row_view.findViewById(R.id.row_col_2)).setTextColor(getResources().getColor(R.color.white));
        ((TextView) row_view.findViewById(R.id.row_col_3)).setTextColor(getResources().getColor(R.color.white));
        ((TextView) row_view.findViewById(R.id.row_col_4)).setTextColor(getResources().getColor(R.color.white));

        fragmentTwoBinding.f2PropertyTable.addView(row_view);
    }

    private void populateRowsPropertyTable(List<String[]> rows){
        for (int row_index = 0; row_index<rows.size(); row_index++) {
            TableRow row_view = (TableRow) LayoutInflater.from(getContext()).inflate(R.layout.property_table_row, null);

            ((TextView)row_view.findViewById(R.id.row_col_0)).setText(rows.get(row_index)[0]);
            ((TextView)row_view.findViewById(R.id.row_col_1)).setText(rows.get(row_index)[1]);
            ((TextView)row_view.findViewById(R.id.row_col_2)).setText(rows.get(row_index)[2]);
            ((TextView)row_view.findViewById(R.id.row_col_3)).setText(rows.get(row_index)[3]);
            ((TextView)row_view.findViewById(R.id.row_col_4)).setText(rows.get(row_index)[4]);

            /* Set background */
            if (row_index % 2 == 0){ /* even row */
                row_view.setBackgroundColor(getResources().getColor(R.color.dl_table_row_even));
            }else{ /* odd row */
                row_view.setBackgroundColor(getResources().getColor(R.color.dl_table_row_odd));
            }
            fragmentTwoBinding.f2PropertyTable.addView(row_view);
        }
    }

    private void clearPropertyTable(){
        /* Clear table deleting all rows */
        int count = fragmentTwoBinding.f2PropertyTable.getChildCount();
        if (count>0) {
            fragmentTwoBinding.f2PropertyTable.removeViews(1, count - 1);
        }
    }
}