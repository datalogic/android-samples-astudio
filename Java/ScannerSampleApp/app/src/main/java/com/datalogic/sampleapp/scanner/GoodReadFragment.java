package com.datalogic.sampleapp.scanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.datalogic.decode.BarcodeManager;
import com.datalogic.decode.configuration.GoodRead;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * GoodReadFragment is an example of how to use the Datalogic SDK to configure the Scanner's Good Read settings.
 * <p>
 * The Good Read settings allow users to enable or disable various feedback mechanisms, such as LED, vibration,
 * and green spot, for successful barcode scans.
 * </p>
 */
public class GoodReadFragment extends Fragment {
    // ======================================================================
    // STEP 1: DECLARING VARIABLES
    // ======================================================================
    BarcodeManager mBarcodeManager;
    GoodRead mGoodRead;

    Spinner mSpinnerGoodReadEnable;
    Spinner mSpinnerGoodReadLedEnable;

    Spinner mSpinnerGoodReadVibrateEnable;
    Spinner mSpinnerGreenSpot;

    Button mStoreButton;

    static Map<String, Boolean> kBooleanOptionsMap = new HashMap<>();

    static {
        kBooleanOptionsMap.put("Enable", true);
        kBooleanOptionsMap.put("Disable", false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ======================================================================
        // STEP 2: INITIALIZING THE BARCODE MANAGER
        // ======================================================================
        // The BarcodeManager allows interaction with the scanner's symbology.
        // It provides methods for enabling or disabling specific symbology.
        //
        // Refer to the official Datalogic SDK documentation here:
        // https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/BarcodeManager.html
        //
        mBarcodeManager = new BarcodeManager();

        // ======================================================================
        // STEP 3: INITIALIZING THE GOOD READ CONFIGURATION
        // ======================================================================
        // The Good Read configuration allows users to enable or disable various feedback mechanisms,
        // such as LED, vibration, and green spot, for successful barcode scans.
        //
        // Refer to the official Datalogic SDK documentation here:
        // https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/GoodRead.html
        //
        mGoodRead = new GoodRead(mBarcodeManager);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_good_read, container, false);
        // Adapter for boolean value
        ArrayAdapter<String> adapter = new ArrayAdapter<>(                                                                                                                                                                                getContext()
                , android.R.layout.simple_spinner_item
                , new ArrayList<>(kBooleanOptionsMap.keySet()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Spinner for good read enable
        mSpinnerGoodReadEnable = view.findViewById(R.id.good_read_enable_value);
        mSpinnerGoodReadEnable.setAdapter(adapter);

        // Spinner for good read led enable
        mSpinnerGoodReadLedEnable = view.findViewById(R.id.good_read_led_enable_value);
        mSpinnerGoodReadLedEnable.setAdapter(adapter);

        // Spinner for good read vibrate enable
        mSpinnerGoodReadVibrateEnable = view.findViewById(R.id.good_read_vibrate_enable_value);
        mSpinnerGoodReadVibrateEnable.setAdapter(adapter);

        // Spinner for green spot enable
        mSpinnerGreenSpot = view.findViewById(R.id.green_spot_enable_value);
        mSpinnerGreenSpot.setAdapter(adapter);

        // Button for store change
        mStoreButton = view.findViewById(R.id.apply_change_button);

        // Handle logic for button
        mStoreButton.setOnClickListener( v-> {
            // ======================================================================
            // STEP 4: CHANGE GOOD READ ENABLE
            // ======================================================================
            // Retrieve the good read enable attribute from the spinner
            String goodReadEnable = mSpinnerGoodReadEnable.getSelectedItem().toString();
            // Set the good read enable state for the Good Read
            mGoodRead.goodReadEnable.set(kBooleanOptionsMap.get(goodReadEnable));

            // ======================================================================
            // STEP 5: CHANGE GOOD READ LED ENABLE
            // ======================================================================
            // Retrieve the good read LED enable attribute from the spinner
            String goodReadLedEnable = mSpinnerGoodReadLedEnable.getSelectedItem().toString();
            // Set the good read LED enable state for the Good Read
            mGoodRead.goodReadLedEnable.set(kBooleanOptionsMap.get(goodReadLedEnable));

            // ======================================================================
            // STEP 6: CHANGE GOOD READ VIBRATE ENABLE
            // ======================================================================
            // Retrieve the good read vibrate enable attribute from the spinner
            String goodReadVibrateEnable = mSpinnerGoodReadVibrateEnable.getSelectedItem().toString();
            // Set the good read vibrate enable state for the Good Read
            mGoodRead.goodReadVibrateEnable.set(kBooleanOptionsMap.get(goodReadVibrateEnable));

            // ======================================================================
            // STEP 7: CHANGE GREEN SPOT ENABLE
            // ======================================================================
            // Retrieve the green spot enable attribute from the spinner
            String greenSpot = mSpinnerGreenSpot.getSelectedItem().toString();
            // Set the green spot enable state for the Good Read
            mGoodRead.greenSpotEnable.set(kBooleanOptionsMap.get(greenSpot));

            // ======================================================================
            // STEP 8: STORING THE CHANGES TO APPLY THE SCANNER'S GOOD READ SETTINGS
            // ======================================================================
            // Store the changes to persist the updated Good Read configuration
            mGoodRead.store(mBarcodeManager, true);
        });
        return view;
    }
}
