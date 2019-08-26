package com.datalogic.cradleapp.service;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.datalogic.cradleapp.R;
import com.datalogic.cradleapp.activity.CradleAppActivity;
import com.datalogic.extension.selfshopping.cradle.Cradle;
import com.datalogic.extension.selfshopping.cradle.CradleInsertionListener;
import com.datalogic.extension.selfshopping.cradle.CradleManager;

import java.util.Timer;
import java.util.TimerTask;

import static com.datalogic.cradleapp.application.CradleManagerApplication.SERVICE_TAG;
import static com.datalogic.cradleapp.application.CradleManagerApplication.CHANNEL_ID;
import static com.datalogic.cradleapp.application.CradleManagerApplication.CHANNEL_NAME;
import static com.datalogic.cradleapp.application.CradleManagerApplication.CHANNEL_DESC;

public class CradleManagerService extends Service implements CradleInsertionListener {

    public static final String BATTERY_CHANGED_MESSAGE = "com.datalogic.cradleapp.message.BATTERY_CHANGED_MESSAGE";
    public static final String DEVICE_EXTRACTED = "com.datalogic.cradleapp.message.DEVICE_EXTRACTED_MESSAGE";

    public static long insertionTime = -1;
    public static int batteryPercent = -1;

    public static boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(CHANNEL_NAME)
                .setContentText(CHANNEL_DESC)
                .setSmallIcon(R.drawable.ic_dock_black)
                .build();

        startForeground(1, notification);
    }

    private static boolean loadBatteryStatus(Context context) {

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.getApplicationContext().registerReceiver(null, ifilter);

        //int newBatteryPercent = (new Random()).nextInt(100);
        int newBatteryPercent = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

        if (newBatteryPercent != batteryPercent) {
            batteryPercent = newBatteryPercent;
            return true;
        }

        return false;
    }

    public static int getBatteryStatus(Context context) {
        if (batteryPercent == -1) {
            loadBatteryStatus(context);
        }
        return batteryPercent;
    }

    public static class BootReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                CradleManagerService.startService(context);
            }
        }

    }

    public static void startService(Context context) {
        if (isRunning) {
            Log.d(SERVICE_TAG, "service already running.");
        } else {
            Intent startServiceIntent = new Intent(context, CradleManagerService.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(startServiceIntent);
                Log.d(SERVICE_TAG, "started service foreground");
            } else {
                context.startService(startServiceIntent);
                Log.d(SERVICE_TAG, "started service");
            }
        }
    }

    @Override
    public void onDeviceInsertedCorrectly() {
        Log.d(SERVICE_TAG, "onDeviceInsertedCorrectly");

        onInsertion();
    }

    @Override
    public void onDeviceInsertedWrongly() {
        Log.d(SERVICE_TAG, "onDeviceInsertedWrongly");
        //cancelUnlockNotification(this);
    }

    @Override
    public void onDeviceExtracted() {
        Log.d(SERVICE_TAG, "onDeviceExtracted");
        //cancelUnlockNotification(this);

        onExtracted();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy()
    {
        Log.d(SERVICE_TAG, "onDestroy");
        super.onDestroy();
        isRunning = false;
    }

    // constant
    public static final long NOTIFY_INTERVAL = 30 * 1000; // n (sec) * 1000

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

    private LocalBroadcastManager broadcaster = null;

    private void onInsertion() {
        // save the insertion time.
        insertionTime = System.currentTimeMillis();

        Intent myIntent = new Intent(this, CradleAppActivity.class);
        startActivity(myIntent);
    }

    private void onExtracted() {
        Intent intent = new Intent(DEVICE_EXTRACTED);
        broadcaster.sendBroadcast(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        isRunning = true;

        broadcaster = LocalBroadcastManager.getInstance(this);

//        if (BuildConfig.FLAVOR == "mock") {
//            Method m;
//            try {
//                m = CradleManager.class.getMethod("setContext", Context.class);
//                m.invoke(null, getApplicationContext());
//            } catch (NoSuchMethodException e) {
//                Log.e(SERVICE_TAG, "error calling setContext", e);
//            } catch (IllegalAccessException e) {
//                Log.e(SERVICE_TAG, "error calling setContext", e);
//            } catch (InvocationTargetException e) {
//                Log.e(SERVICE_TAG, "error calling setContext", e);
//            }
//        }

        setupCradle();

        //if(mTimer != null) {
        //    mTimer.cancel();
        //} else {
            // recreate new
            mTimer = new Timer();
        //}

        // schedule task
        mTimer.scheduleAtFixedRate(new UpdateBatteryTimerTask(), 0, NOTIFY_INTERVAL);

        loadBatteryStatus(getApplicationContext());


        Log.d(SERVICE_TAG, "onStartCommand");
        return START_STICKY;
    }

    private void setupCradle() {
        Cradle cradle = CradleManager.getCradle();

        if (cradle != null) {
            // controversial - we don't want to assume the cradle API will never go down.
            // so we re-register periodically so we ensure we never miss an event.
            cradle.removeCradleInsertionListener(this);
            cradle.addCradleInsertionListener(this);

            if (cradle.getInsertionState() == Cradle.InsertionState.INSERTED_CORRECTLY) {
                onInsertion();
            }
        }

    }

    class UpdateBatteryTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {

                    //setupCradle();

                    boolean changed = loadBatteryStatus(getApplicationContext());
                    if (changed) {
                        Intent intent = new Intent(BATTERY_CHANGED_MESSAGE);
                        broadcaster.sendBroadcast(intent);
                        Log.d(SERVICE_TAG, "UpdateBatteryTimerTask run battery % changed: " + batteryPercent);
                    }
                }


            });
        }

    }

}
