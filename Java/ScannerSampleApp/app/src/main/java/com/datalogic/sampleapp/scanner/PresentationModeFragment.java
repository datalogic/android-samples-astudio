package com.datalogic.sampleapp.scanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.datalogic.decode.*;
import com.datalogic.decode.configuration.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * PresentationModeFragment is an example of how to use the Datalogic SDK to configure Scanner's format.
 * <p>
 * Standard Formatter let user can modify scanner's format
 * Links to the Datalogic SDK documentation for the relevant properties:
 * <ul>
 *   <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/ECIPolicy.html">ECIPolicy</a></li>
 *   <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/Gs1Conversion2d.html">Gs1Conversion2d</a></li>
 *   <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/Gs1LabelSetTransmitMode.html">Gs1LabelSetTransmitMode</a></li>
 *   <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/GtinFormat.html">GtinFormat</a></li>
 *   <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/SendCodeID.html">SendCodeID</a></li>
 * </ul>
 * </p>
 */
public class PresentationModeFragment extends Fragment {

    BarcodeManager mBarcodeManager;

    PresentationMode mPresentationMode;

    Button mStoreButton;

    static Map<String, Boolean> kBooleanOptionsMap = new HashMap<>();

    static {
        // Initialize boolean map
        kBooleanOptionsMap.put("Enable", true);
        kBooleanOptionsMap.put("Disable", false);
    }

    Spinner mSpinnerPresentationModeAimer;

    Spinner mSpinnerPresentationMode;

    EditText mEditTextPresentationModeSensitivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ======================================================================
        // STEP 1: INITIALIZING THE BARCODE MANAGER
        // ======================================================================
        // The BarcodeManager allows interaction with the scanner's symbology.
        // It provides methods for enabling or disabling specific symbology.
        //
        // Refer to the official Datalogic SDK documentation here:
        // https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/BarcodeManager.html
        //
        mBarcodeManager =  new BarcodeManager();

        // ======================================================================
        // STEP 2: INITIALIZING THE PRESENTATION MODE
        // ======================================================================
        // The Formatting allows user config format.
        // It provides methods for scanner's presentation modification.
        //
        // Refer to the official Datalogic SDK documentation here:
        // https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/PresentationMode.html
        //
        mPresentationMode = new PresentationMode(mBarcodeManager);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_presentation, container, false);
        mStoreButton = view.findViewById(R.id.apply_change_button);

        // Adapter for boolean value
        ArrayAdapter<String> adapter = new ArrayAdapter<>(                                                                                                                                                                                getContext()
                , android.R.layout.simple_spinner_item
                , new ArrayList<>(kBooleanOptionsMap.keySet()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Spinner for  presentation mode aimer
        mSpinnerPresentationModeAimer = view.findViewById(R.id.presentation_mode_aimer_value);
        mSpinnerPresentationModeAimer.setAdapter(adapter);

        // Spinner for  presentation mode enable
        mSpinnerPresentationMode = view.findViewById(R.id.presentation_mode_enable_value);
        mSpinnerPresentationMode.setAdapter(adapter);

        // Edit text for  presentation mode sensitivity
        mEditTextPresentationModeSensitivity = view.findViewById(R.id.presentation_mode_sensitivity_value);


        mStoreButton.setOnClickListener(v->{
            // ======================================================================
            // STEP 3: ENABLE/DISABLE PRESENTATION MODE AIMER
            // ======================================================================
            // Retrieve the presentation mode aimer attribute from presentation
            String presentationModeAimer =  mSpinnerPresentationModeAimer.getSelectedItem().toString();
            mPresentationMode.presentationModeAimerEnable.set(kBooleanOptionsMap.get(presentationModeAimer));

            // ======================================================================
            // STEP 4: ENABLE/DISABLE PRESENTATION MODE
            // ======================================================================
            // Retrieve the presentation mode attribute from presentation
            String  presentationMode = mSpinnerPresentationMode.getSelectedItem().toString();
            mPresentationMode.presentationModeEnable.set(kBooleanOptionsMap.get(presentationMode));

            // ======================================================================
            // STEP 5: ENABLE/DISABLE PRESENTATION MODE SENSITIVITY
            // ======================================================================
            // Retrieve the presentation mode sensitivity attribute from presentation
            int presentationModeSensitivity =  Integer.parseInt(mEditTextPresentationModeSensitivity.getText().toString());
            mPresentationMode.presentationModeSensitivity.set(presentationModeSensitivity);

            // ======================================================================
            // STEP 6: STORING THE CHANGES TO APPLY THE SCANNER'S PRESENTATION
            // ======================================================================
            // Store the changes to persist the updated settings to the device's storage.
            // This saves and applies the changes to the scanner's presentation, making them persistent across reboots.
            mPresentationMode.store(mBarcodeManager, true);
        });

        return view;
    }
}
