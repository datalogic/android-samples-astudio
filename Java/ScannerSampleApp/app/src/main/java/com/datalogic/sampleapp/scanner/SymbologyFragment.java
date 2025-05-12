package com.datalogic.sampleapp.scanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.datalogic.decode.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SymbologyFragment is an example of how to use the Datalogic SDK to enable/disable specific symbology.
 * <p>
 * In particular, Symbology allows devices choose specific symbology can be enabled or disabled,
 * ensuring a seamless and controlled pairing process.
 * Links to the Datalogic SDK documentation for the relevant properties:
 * <ul>
 *   <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/Symbology.html">Symbology</a></li>
 * </ul>
 * </p>
 */
public class SymbologyFragment extends Fragment {

    private BarcodeManager mBarcodeManager;
    Button mButtonApplyAll;

    Spinner mSpinnerValueAll;

    Spinner mSpinnerOption;

    Button mButtonApplySpecific;
    Spinner mSpinnerValueSpecific;
    List<Symbology> symbologyList = Arrays.asList(Symbology.values());
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_symbology, container, false);

        mSpinnerOption = view.findViewById(R.id.package_spinner);
        ArrayAdapter<Symbology> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, symbologyList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerOption.setAdapter(adapter);

        // common value adapter
        ArrayAdapter<CharSequence> adapterValue = new ArrayAdapter<>(                                                                                                                                                                                getContext()
                , android.R.layout.simple_spinner_item
                , new ArrayList<>(kBooleanOptionsMap.keySet()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // spinner
        mSpinnerValueAll = view.findViewById(R.id.package_spinner_value_all);
        mSpinnerValueSpecific = view.findViewById(R.id.package_spinner_value_specific);
        mSpinnerValueAll.setAdapter(adapterValue);
        mSpinnerValueSpecific.setAdapter(adapterValue);

        mButtonApplyAll = view.findViewById(R.id.apply_all_button);
        mButtonApplyAll.setOnClickListener(v->{
            String value = mSpinnerValueAll.getSelectedItem().toString();
            // ======================================================================
            // STEP 2: Enable/Disable all symbology
            // ======================================================================
            if (value.equals("Enable")) {
                if (mBarcodeManager.enableAllSymbologies(true) == DecodeException.SUCCESS) {
                    Toast.makeText(getContext(),"apply successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(),"apply failed", Toast.LENGTH_SHORT).show();
                }
            } else  {
                if (mBarcodeManager.enableAllSymbologies(false) == DecodeException.SUCCESS) {
                    Toast.makeText(getContext(),"apply successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(),"apply failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        mButtonApplySpecific = view.findViewById(R.id.apply_specific_button);
        mButtonApplySpecific.setOnClickListener(v->{
            Symbology valueSymbology = (Symbology) mSpinnerOption.getSelectedItem();
            String value = mSpinnerValueSpecific.getSelectedItem().toString();
            // ======================================================================
            // STEP 3: Enable/Disable specific symbology
            // ======================================================================
            if (value.equals("Enable")) {
                if (mBarcodeManager.enableSymbology(valueSymbology,true) == DecodeException.SUCCESS) {
                    Toast.makeText(getContext(),"apply successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(),"apply failed", Toast.LENGTH_SHORT).show();
                }
            } else  {
                if (mBarcodeManager.enableSymbology(valueSymbology,false) == DecodeException.SUCCESS) {
                    Toast.makeText(getContext(),"apply successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(),"apply failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}
