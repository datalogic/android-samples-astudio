package com.datalogic.cradleapp.application;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.datalogic.extension.selfshopping.cradle.joyatouch.CradleJoyaTouch;

public class CradleManagerApplication extends Application {

    // LOGTAG consts
    public static final String LOG_TAG = "CradleApp";
    public static final String SERVICE_TAG = "CradleApp(S)";

    // Notification channel consts
    public static final String CHANNEL_ID = "cradleAppServiceChannel";
    public static final String CHANNEL_NAME = "Cradle App Service";
    public static final String CHANNEL_DESC = "Cradle App";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
