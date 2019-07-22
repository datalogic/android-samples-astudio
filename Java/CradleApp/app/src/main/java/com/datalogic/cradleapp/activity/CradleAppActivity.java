package com.datalogic.cradleapp.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.datalogic.cradleapp.R;
import com.datalogic.cradleapp.consts.BatteryLevel;
import com.datalogic.cradleapp.service.CradleManagerService;
import com.datalogic.extension.selfshopping.cradle.Cradle;
import com.datalogic.extension.selfshopping.cradle.CradleManager;
import com.datalogic.extension.selfshopping.cradle.CradleType;
import com.datalogic.extension.selfshopping.cradle.joyatouch.ConfigArea;
import com.datalogic.extension.selfshopping.cradle.joyatouch.CradleJoyaTouch;
import com.datalogic.extension.selfshopping.cradle.joyatouch.LockAction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static android.text.format.DateUtils.FORMAT_NUMERIC_DATE;
import static com.datalogic.cradleapp.application.CradleManagerApplication.LOG_TAG;
import static com.datalogic.cradleapp.application.CradleManagerApplication.SERVICE_TAG;
import static com.datalogic.extension.selfshopping.cradle.Cradle.InsertionState.INSERTED_CORRECTLY;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class CradleAppActivity extends Activity {

    private CradleJoyaTouch cradleJoyaTouch;
    //private Trolley trolleyJoyaTouch;


    private BroadcastReceiver batteryUpdateReceiver;
    private BroadcastReceiver deviceExtractedReceiver;

    private View mTapToUnlockButton;

    private final int CRADLE_RELOCK_TIMEOUT_DEFAULT = 20000;
    private static int cradleUnlockCountdownInterval = -1;
    private boolean cradleUnlockCountdownActive = false;
    private Handler cradleUnlockCountdownHandler = new Handler();
    private Runnable cradleUnlockCountdownRunnable = new Runnable() {
        @Override
        public void run() {
            cradleUnlockCountdownActive = false;
            if (cradleJoyaTouch != null) {
                // some cradles unlock automatically. some don't
                cradleJoyaTouch.controlLock(LockAction.LOCK);
            }
            ((Button)mTapToUnlockButton).setText(R.string.tap_to_remove);
        }
    };


    private final View.OnTouchListener mTapToUnlockButtonListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (cradleJoyaTouch == null) {
                    Log.e(LOG_TAG, "error unlocking dock. cradleJoyaTouch is null");
                    return false;
                }

                if (cradleJoyaTouch.getInsertionState() == INSERTED_CORRECTLY) {
                    Log.d(LOG_TAG, "unlock dock");
                    cradleJoyaTouch.controlLock(LockAction.UNLOCK);

                    ((Button)mTapToUnlockButton).setText(R.string.ready_to_remove);
                    cradleUnlockCountdownActive = true;

                    cradleUnlockCountdownHandler.postDelayed(cradleUnlockCountdownRunnable, getCradleUnlockCountdownIntervalMS());

                } else {
                    Log.d(LOG_TAG, "no need to unlock dock");
                }


            }
            return false;  // why false?
        }
    };

    private int getCradleUnlockCountdownIntervalMS() {
        if (cradleUnlockCountdownInterval > 0) {
            return cradleUnlockCountdownInterval;
        }

        if (cradleJoyaTouch == null) {
            return CRADLE_RELOCK_TIMEOUT_DEFAULT;
        }

        ConfigArea area = new ConfigArea();
        boolean success = cradleJoyaTouch.readConfigArea(area);
        if (!success) {
            Log.d(LOG_TAG, "error reading dock config area");
            return CRADLE_RELOCK_TIMEOUT_DEFAULT;
        }

        cradleUnlockCountdownInterval = area.getRelockTimeout() * 1000;  // sec --> ms
        Log.d(LOG_TAG, "cradleUnlockCountdownInterval is " + cradleUnlockCountdownInterval);
        return cradleUnlockCountdownInterval;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cradle_app);

        // already done in the layout, but it must be done here for screen to
        // properly stay on after a device reboot or power-off
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setTurnScreenOn(true);
        setShowWhenLocked(true);

        batteryUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(LOG_TAG, "batteryUpdateReceiver");
                updateBattery();
            }
        };

        deviceExtractedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(LOG_TAG, "deviceExtractedReceiver");
                finish();
            }
        };

        mTapToUnlockButton = findViewById(R.id.tap_to_remove);
        mTapToUnlockButton.setOnTouchListener(mTapToUnlockButtonListener);

        TextView mSerialNumber = (TextView)findViewById(R.id.serial_number);
        mSerialNumber.setText(Build.SERIAL); // must convert to getSerial for targetAPI > 26.

        CradleManagerService.startService(getApplicationContext());

        Cradle cradle = CradleManager.getCradle();
        if (cradle != null && cradle.getType() == CradleType.JOYA_TOUCH_CRADLE) {
            cradleJoyaTouch = (CradleJoyaTouch) cradle;
        }

        if (cradleJoyaTouch != null) {
            if (!cradleJoyaTouch.isDeviceInCradle()) {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "onDestroy");
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume");

        super.onResume();

        if (CradleManagerService.insertionTime != -1) {
            TextView insertedOn = (TextView) findViewById(R.id.inserted_on);

            String insertedOnText = DateUtils.formatDateTime(getApplicationContext(),
                    CradleManagerService.insertionTime,
                    DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE);

            insertedOn.setText(insertedOnText);
        }

        updateBattery();

        hideSystemUI();

        LocalBroadcastManager.getInstance(this).registerReceiver((batteryUpdateReceiver),
                new IntentFilter(CradleManagerService.BATTERY_CHANGED_MESSAGE));

        LocalBroadcastManager.getInstance(this).registerReceiver((deviceExtractedReceiver),
                new IntentFilter(CradleManagerService.DEVICE_EXTRACTED));
    }

    private void updateBattery() {
        int battery = CradleManagerService.getBatteryStatus(getApplicationContext());
        if (battery != -1) {
            Log.d(LOG_TAG, "updateBattery charge =" +  battery);

            cradleUnlockCountdownActive = false;

            Button removeButton = (Button) findViewById(R.id.tap_to_remove);
            removeButton.setBackgroundColor(BatteryLevel.getColorInt(battery, getApplicationContext()));
            removeButton.setTextColor(BatteryLevel.getTextColorInt(battery, getApplicationContext()));
        }
    }

    @Override
    protected void onPause() {

        Log.d(LOG_TAG, "onPause");

        LocalBroadcastManager.getInstance(this).unregisterReceiver(batteryUpdateReceiver);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(deviceExtractedReceiver);

        showSystemUI();

        super.onPause();
    }


    @Override
    protected void onStart() {

        Log.d(LOG_TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {

        Log.d(LOG_TAG, "onStop");
        super.onStop();

        showSystemUI(); // failsafe in case onPause isn't called
    }

    private void hideSystemUI() {

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

}
