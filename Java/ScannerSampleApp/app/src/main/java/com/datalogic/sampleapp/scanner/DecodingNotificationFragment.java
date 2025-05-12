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

import com.datalogic.decode.BarcodeManager;
import com.datalogic.decode.configuration.DecodingNotification;
import com.datalogic.decode.configuration.ToneNotificationChannel;
import com.datalogic.decode.configuration.ToneNotificationMode;

import java.util.Arrays;
import java.util.List;

/**
 * DecodingNotificationFragment is an example of how to use the Datalogic SDK to configure the Scanner's decoding notification.
 * <p>
 * The Decoding Notification allows users to modify the scanner's notification settings for successful decodes.
 * Links to the Datalogic SDK documentation for the relevant properties:
 * <ul>
 *   <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/ToneNotificationChannel.html">ToneNotificationChannel</a></li>
 *   <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/ToneNotificationMode.html">ToneNotificationMode</a></li>
 * </ul>
 * </p>
 */
public class DecodingNotificationFragment extends Fragment {
    // ======================================================================
    // STEP 1: DECLARING VARIABLES
    // ======================================================================
    BarcodeManager mBarcodeManager;
    DecodingNotification mDecodingNotification;
    Spinner mSpinnerGoodReadAudioChannel ;
    Spinner mSpinnerGoodReadAudioMode;

    EditText mEditTextGoodReadAudioFile;

    EditText mEditTextGoodReadAudioVolume;

    EditText mEditTextGoodReadCount;

    EditText mEditTextGoodReadDuration;

    EditText mEditTextGoodReadInterval;

    EditText mEditTextGoodReadTimeout;

    Button mButtonStore;

    static List<ToneNotificationChannel> kToneNotificationChannelOptionsList = Arrays.asList(ToneNotificationChannel.values());

    static List<ToneNotificationMode> kToneNotificationModeOptionsList = Arrays.asList(ToneNotificationMode.values());


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
        // STEP 3: INITIALIZING THE DECODING NOTIFICATION
        // ======================================================================
        // The Decoding Notification allows users to configure the scanner's notification settings.
        // It provides methods for setting audio channels, modes, and other notification properties.
        //
        // Refer to the official Datalogic SDK documentation here:
        // https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/DecodingNotification.html
        //
        mDecodingNotification = new DecodingNotification(mBarcodeManager);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_decoding_notification, container, false);
        mButtonStore = view.findViewById(R.id.apply_change_button);

        // Adapter for tone notification channel value
        ArrayAdapter<ToneNotificationChannel> adapterToneNotificationChannel = new ArrayAdapter<>(                                                                                                                                                                                getContext()
                , android.R.layout.simple_spinner_item
                , kToneNotificationChannelOptionsList);
        adapterToneNotificationChannel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Adapter for tone notification mode value
        ArrayAdapter<ToneNotificationMode> adapterToneNotificationMode = new ArrayAdapter<>(                                                                                                                                                                                getContext()
                , android.R.layout.simple_spinner_item
                , kToneNotificationModeOptionsList);
        adapterToneNotificationMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Spinner for tone notification channel
        mSpinnerGoodReadAudioChannel = view.findViewById(R.id.good_read_audio_channel_value);
        mSpinnerGoodReadAudioChannel.setAdapter(adapterToneNotificationChannel);

        // Spinner for tone notification mode
        mSpinnerGoodReadAudioMode = view.findViewById(R.id.good_read_audio_mode_value);
        mSpinnerGoodReadAudioMode.setAdapter(adapterToneNotificationMode);

        // Edittext for audio file
        mEditTextGoodReadAudioFile = view.findViewById(R.id.good_read_audio_file_value);

        // Edittext for audio volume
        mEditTextGoodReadAudioVolume = view.findViewById(R.id.good_read_audio_volume_value);

        // Edittext for count
        mEditTextGoodReadCount = view.findViewById(R.id.good_read_count_value);

        // Edittext for duration
        mEditTextGoodReadDuration = view.findViewById(R.id.good_read_duration_value);

        // Edittext for interval
        mEditTextGoodReadInterval = view.findViewById(R.id.good_read_interval_value);

        // Edittext for timeout
        mEditTextGoodReadTimeout = view.findViewById(R.id.good_read_timeout_value);

        // Handle logic button
        mButtonStore.setOnClickListener(v->{

            // ======================================================================
            // STEP 4: CHANGE AUDIO CHANNEL
            // ======================================================================
            // Retrieve the audio channel attribute from the spinner
            ToneNotificationChannel toneNotificationChannel = (ToneNotificationChannel) mSpinnerGoodReadAudioChannel.getSelectedItem();
            // Set the audio channel for the Decoding Notification
            mDecodingNotification.goodReadAudioChannel.set(toneNotificationChannel);

            // ======================================================================
            // STEP 5: CHANGE AUDIO MODE
            // ======================================================================
            // Retrieve the audio mode attribute from the spinner
            ToneNotificationMode toneNotificationMode = (ToneNotificationMode) mSpinnerGoodReadAudioMode.getSelectedItem();
            // Set the audio mode for the Decoding Notification
            mDecodingNotification.goodReadAudioMode.set(toneNotificationMode);

            // ======================================================================
            // STEP 6: CHANGE AUDIO FILE
            // ======================================================================
            // Retrieve the audio file path from the EditText
            String audioFile = mEditTextGoodReadAudioFile.getText().toString();
            // Set the audio file for the Decoding Notification
            mDecodingNotification.goodReadAudioFile.set(audioFile);

            // ======================================================================
            // STEP 7: CHANGE AUDIO VOLUME
            // ======================================================================
            // Retrieve the audio volume value from the EditText
            int audioVolume = Integer.parseInt(mEditTextGoodReadAudioVolume.getText().toString());
            // Set the audio volume for the Decoding Notification
            mDecodingNotification.goodReadAudioVolume.set(audioVolume);

            // ======================================================================
            // STEP 8: CHANGE GOOD READ COUNT
            // ======================================================================
            // Retrieve the good read count value from the EditText
            int count = Integer.parseInt(mEditTextGoodReadCount.getText().toString());
            // Set the good read count for the Decoding Notification
            mDecodingNotification.goodReadCount.set(count);

            // ======================================================================
            // STEP 9: CHANGE GOOD READ DURATION
            // ======================================================================
            // Retrieve the good read duration value from the EditText
            int duration = Integer.parseInt(mEditTextGoodReadDuration.getText().toString());
            // Set the good read duration for the Decoding Notification
            mDecodingNotification.goodReadDuration.set(duration);

            // ======================================================================
            // STEP 10: CHANGE GOOD READ INTERVAL
            // ======================================================================
            // Retrieve the good read interval value from the EditText
            int interval = Integer.parseInt(mEditTextGoodReadInterval.getText().toString());
            // Set the good read interval for the Decoding Notification
            mDecodingNotification.goodReadInterval.set(interval);

            // ======================================================================
            // STEP 11: CHANGE GOOD READ TIMEOUT
            // ======================================================================
            // Retrieve the good read timeout value from the EditText
            int timeout = Integer.parseInt(mEditTextGoodReadTimeout.getText().toString());
            // Set the good read timeout for the Decoding Notification
            mDecodingNotification.goodReadTimeout.set(timeout);

            // ======================================================================
            // STEP 12: STORING THE CHANGES TO APPLY THE SCANNER'S DECODING NOTIFICATIONS
            // ======================================================================
            // Store the changes to persist the updated Decoding notification configuration
            mDecodingNotification.store(mBarcodeManager, true);
        });
        return view;
    }

}
