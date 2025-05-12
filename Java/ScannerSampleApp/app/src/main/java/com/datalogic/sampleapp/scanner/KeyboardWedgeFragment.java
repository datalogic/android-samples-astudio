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
import com.datalogic.decode.configuration.KeyWedgeMode;
import com.datalogic.decode.configuration.KeyboardWedge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * KeyboardWedgeFragment is an example of how to use the Datalogic SDK to configure the Scanner's keyboard wedge.
 * <p>
 * The Keyboard Wedge allows users to modify the scanner's keyboard wedge settings.
 * Links to the Datalogic SDK documentation for the relevant properties:
 * <ul>
 *   <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/KeyWedgeMode.html">KeyWedgeMode</a></li>
 * </ul>
 * </p>
 */
public class KeyboardWedgeFragment extends Fragment {

    KeyboardWedge mKeyboardWedge;

    BarcodeManager mBarcodeManager;

    Spinner mSpinnerKeyboardWedge;

    Spinner mSpinnerOnlyFocus;

    Spinner mSpinnerWedgeMode;

    Button mButtonStore;

    static List<KeyWedgeMode> kKeyWedgeModeOptionsList = Arrays.asList(KeyWedgeMode.values());

    static Map<String, Boolean> kBooleanOptionsMap = new HashMap<>();

    static {
        kBooleanOptionsMap.put("Enable", true);
        kBooleanOptionsMap.put("Disable", false);
    }

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
        mBarcodeManager = new BarcodeManager();

        // ======================================================================
        // STEP 2: INITIALIZING THE KEYBOARD WEDGE
        // ======================================================================
        // The Keyboard Wedge allows users to configure the scanner's keyboard wedge.
        // It provides methods for enabling/disabling the keyboard wedge and setting its mode.
        //
        // Refer to the official Datalogic SDK documentation here:
        // https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/KeyboardWedge.html
        //
        mKeyboardWedge = new KeyboardWedge(mBarcodeManager);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keyboard_wedge, container, false);

        mButtonStore = view.findViewById(R.id.apply_change_button);
        // Adapter for boolean value
        ArrayAdapter<String> adapter = new ArrayAdapter<>(                                                                                                                                                                                getContext()
                , android.R.layout.simple_spinner_item
                , new ArrayList<>(kBooleanOptionsMap.keySet()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Adapter for wedge mode value
        ArrayAdapter<KeyWedgeMode> adapterKeyWedgeMode = new ArrayAdapter<>(                                                                                                                                                                                getContext()
                , android.R.layout.simple_spinner_item
                , kKeyWedgeModeOptionsList);
        adapterKeyWedgeMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // Spinner for keyboard wedge
        mSpinnerKeyboardWedge = view.findViewById(R.id.keyboard_wedge_enable_value);
        mSpinnerKeyboardWedge.setAdapter(adapter);

        // Spinner for only focus
        mSpinnerOnlyFocus = view.findViewById(R.id.only_focus_value);
        mSpinnerOnlyFocus.setAdapter(adapter);

        // Spinner for wedge mode
        mSpinnerWedgeMode = view.findViewById(R.id.wedge_mode_value);
        mSpinnerWedgeMode.setAdapter(adapterKeyWedgeMode);

        // Handle logic for button
        mButtonStore.setOnClickListener(v->{
            // ======================================================================
            // STEP 3: ENABLE/DISABLE KEYBOARD WEDGE
            // ======================================================================
            // Retrieve the keyboard wedge enable attribute from the spinner
            String keyboardWedgeValue = mSpinnerKeyboardWedge.getSelectedItem().toString();
            // Set the enable/disable state for the Keyboard Wedge
            mKeyboardWedge.enable.set(kBooleanOptionsMap.get(keyboardWedgeValue));

            // ======================================================================
            // STEP 4: CHANGE ONLY ON FOCUS
            // ======================================================================
            // Retrieve the only on focus attribute from the spinner
            String onlyFocusValue = mSpinnerOnlyFocus.getSelectedItem().toString();
            // Set the only on focus state for the Keyboard Wedge
            mKeyboardWedge.onlyOnFocus.set(kBooleanOptionsMap.get(onlyFocusValue));

            // ======================================================================
            // STEP 5: CHANGE WEDGE MODE
            // ======================================================================
            // Retrieve the wedge mode attribute from the spinner
            KeyWedgeMode keyWedgeMode = (KeyWedgeMode) mSpinnerWedgeMode.getSelectedItem();
            // Set the wedge mode for the Keyboard Wedge
            mKeyboardWedge.wedgeMode.set(keyWedgeMode);

            // ======================================================================
            // STEP 6: STORING THE CHANGES TO APPLY THE SCANNER'S KEYBOARD WEDGE
            // ======================================================================
            // Store the changes to persist the updated Keyboard Wedge configuration
            mKeyboardWedge.store(mBarcodeManager, true);
        });
        return view;
    }
}
