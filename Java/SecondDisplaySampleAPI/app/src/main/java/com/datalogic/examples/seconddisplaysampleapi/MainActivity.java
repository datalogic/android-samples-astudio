// ©2020 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.seconddisplaysampleapi;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.app.Notification;
import java.util.List;

import com.datalogic.device.display.DLSecondDisplayManager;

public class MainActivity extends Activity {

	static final String TAG = "Test-Scanner";
	private final String PACKAGE_NAME = "com.datalogic.examples.seconddisplaysampleapi";

	// The send notification button
	private Button mSend;

	private final String NOTIFICATION_CHANNEL_ID = "2nd Display Channel";
	private final String NOTIFICATION_CHANNEL_NAME = "2nd display notification sample channel";

	private final int NOTIFICATION_ID = 0;
	private final String NOTIFICATION_TITLE_TEXT = "2nd Display Sample";
	private final String NOTIFICATION_CONTENT_TEXT = "this sample notification is rolling on second display!";

	private NotificationManager mNotificationManager = null;
	private Notification mNotification = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);

		// 2nd Display configuration to enable the App
		// to shown notification on 2nd Display
		setupSecondDisplay();

		// Configure the Notification System to
		// allow the App to send notifications
		setupNotificationSystem();

		// create the View with the button
		// to send notifications
		setupView();
	}

	private void setupNotificationSystem() {
		mNotificationManager = getSystemService(NotificationManager.class);
		CharSequence channelName = NOTIFICATION_CHANNEL_NAME;

		NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
		mNotificationManager.createNotificationChannel(notificationChannel);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(NOTIFICATION_TITLE_TEXT)
				.setContentText(NOTIFICATION_CONTENT_TEXT)
				.setPriority(NotificationCompat.PRIORITY_HIGH);

		mNotification = builder.build();
	}

	private void setupSecondDisplay() {
		// Retrieve the reference to the 2nd Display Manager
		DLSecondDisplayManager secondDisplayManager = DLSecondDisplayManager.getInstance();

		// Check if the 2nd Display is available on the current device
		// 2nd Display is only available for Datalogic Memor20
		boolean available = secondDisplayManager.isSecondDisplayAvailable();

		// if available, 2nd Display can be configured
		if (available) {
			// if disable, enable 2nd Display to show notifications
			boolean enabled = secondDisplayManager.isSecondDisplayEnabled();
			if (!enabled) {
				boolean result = secondDisplayManager.setSecondDisplayEnabled(true);
				if (!result)
					Log.d(TAG, "Error occurred enabling 2nd Display");
			}

			// disable all configured Apps to show notification on 2nd Display
			List<String> allowedPackage = secondDisplayManager.getAllowedPackages();
			for (String packageName : allowedPackage) {
				boolean result = secondDisplayManager.removePackage(packageName);
				if (!result)
					Log.d(TAG, "Error disabling " + packageName + " to show notifications on 2nd Display");
			}

			// disable by default Apps installed in the future
			boolean newAppEnabledByDefault = secondDisplayManager.getNewAppEnabled();
			if (newAppEnabledByDefault) {
				// This feature allow to configure the 2nd Display to discard by default
				// the notifications coming from Apps will be installed later.
				// With the following configuration, when a new App is installed on the device,
				// it shall be explicitly configured through "addPackage" to show notifications
				// on 2nd Display.
				// These operation could take a while, due to the storage of all the
				// installed Apps into the DB.
				secondDisplayManager.setNewAppEnabled(false);
			}

			// enable only this App to show notification on 2nd Display
			boolean allowed = secondDisplayManager.isPackageAllowed(PACKAGE_NAME);
			if (!allowed) {
				boolean added = secondDisplayManager.addPackage(PACKAGE_NAME);
				if (!added)
					Log.d(TAG, "Error occurred allowing " + PACKAGE_NAME + " to show notification on 2nd Display");
			}
		} else {
			Log.d(TAG, "2nd Display is available only on Datlalogic Memor20");
		}
	}

	private void setupView() {
		mSend = (Button) findViewById(R.id.send);
		mSend.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					try {
						mNotificationManager.notify(NOTIFICATION_ID, mNotification);
						mSend.setPressed(false);
					} catch (Exception e) {
						Log.e(TAG, "Action UP", e);
						//showMessage("ERROR! Check logcat");
					}
					v.performClick();
				}
				return true;
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
