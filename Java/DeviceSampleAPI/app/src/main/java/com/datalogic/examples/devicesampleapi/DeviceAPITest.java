// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.devicesampleapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * DeviceSampleAPI Launcher activity.
 *
 */
public class DeviceAPITest extends Activity {
	private final static String[] ACT_NAMES = { "Battery",
		"Location - LocationManager",
		"NFC - NfcManager",
		"Notifications - LedManager",
		"Touch - TouchManager",
		"Sleep and Wakeup - PowerManager",
		"Informations - SYSTEM",
		"Reset device" };

	private final static Class<?>[] ACT_CLASSES = { BatteryActivity.class, 
		LocationActivity.class, 
		NfcActivity.class, 
		NotificationActivity.class, 
		TouchActivity.class, 
		SleepActivity.class,
		InfoActivity.class, 
		ResetActivity.class };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Fill the Adapter with the available activity names/classes
		ListView listMainActivities = (ListView) findViewById(R.id.listMainActivities);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, ACT_NAMES);
		listMainActivities.setAdapter(adapter);
		listMainActivities.setOnItemClickListener(new OpenActivityListener());
	}

	/**
	 * Open the activity of corresponding position.
	 * pos is the index in ACT_CLASSES
	 */
	public class OpenActivityListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			Intent intent ;
			intent = new Intent ( DeviceAPITest.this, ACT_CLASSES [pos]);
			startActivity ( intent ) ;
		}

	}
}
