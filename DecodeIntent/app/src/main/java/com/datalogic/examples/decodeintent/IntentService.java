// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.decodeintent;

import java.util.Set;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Log and Toast the received action, then display a notification containing result.
 */
public class IntentService extends Service {

	public IntentService() {
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		showMessage("Started IntentService, check notifications");
		Log.d(getClass().getName(),"Started service with Intent");

		Set<String> category_all = intent.getCategories();
		StringBuilder category = new StringBuilder();
		for (String s : category_all) {
			category.append(s);
		}

		String type = intent.getStringExtra(IntentWedgeSample.EXTRA_TYPE);
		// Retrieve result data.
		String data = intent.getStringExtra(IntentWedgeSample.EXTRA_DATA);

		NotificationManager notificationManager =
				(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// We are going to show a notification, don't exaggerate with characters.
		if(data.length() > 20) {
			data = data.substring(0,20);
		}

		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle(type)
		.setContentText("Result: " + data);

		// Show it.
		notificationManager.notify(0, mBuilder.build());

		stopSelf();
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void showMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}
