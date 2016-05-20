// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.devicesampleapi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.datalogic.device.BootType;
import com.datalogic.device.DeviceException;
import com.datalogic.device.ErrorManager;
import com.datalogic.device.power.PowerManager;

/**
 * Activity to reset the terminal.
 */
public class ResetActivity extends Activity {
	private static String[] listArray = null;
	private static BootType[] bootTypes = null;

	private ListView listReset;

	private PowerManager pm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView ( R.layout.activity_reset);

		ErrorManager.enableExceptions(true);

		try {
			pm = new PowerManager();
		} catch (DeviceException e) {
			android.util.Log.e(getClass().getName(), "While creating ResetActivity", e);
			return;
		}
		// Set listArray and bootTypes
		setArray();

		ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this,
				android.R.layout.simple_list_item_1, listArray);
		listReset = (ListView) findViewById(R.id.listReset);
		listReset.setAdapter(adapter);
		listReset.setOnItemClickListener(new ResetAdapter());
	}

	/**
	 * Set bootTypes to all BootType enum values, and listArray to the names of
	 * each.
	 */
	private void setArray() {
		if (listArray == null || bootTypes == null) {
			bootTypes = BootType.values();
			listArray = new String[bootTypes.length];
			for (int i = 0; i < bootTypes.length; i++) {
				listArray[i] = bootTypes[i].name();
			}
		}
	}

	/**
	 * Reset the terminal on an item click. pos is the BootType index in bootTypes
	 */
	public class ResetAdapter implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			try {
				pm.reboot(bootTypes[pos]);
			} catch (DeviceException e) {
				android.util.Log.e(getClass().getName(), "While onItemClick", e);
				return;
			}
		}

	}
}
