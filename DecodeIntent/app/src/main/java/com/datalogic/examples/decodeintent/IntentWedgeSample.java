// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.decodeintent;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.datalogic.decode.BarcodeManager;
import com.datalogic.decode.DecodeException;
import com.datalogic.decode.configuration.IntentDeliveryMode;
import com.datalogic.decode.configuration.IntentWedge;
import com.datalogic.decode.configuration.ScannerProperties;
import com.datalogic.device.ErrorManager;
import com.datalogic.device.configuration.ConfigException;

/**
 * During creation initializes a radio button change listener, then register its broadcast receiver.
 * Each time the checked radio button changes, MyClickedItemListener class is invoked.
 */
public class IntentWedgeSample extends Activity {
	// Constants for Broadcast Receiver defined below.
	public static final String ACTION_BROADCAST_RECEIVER = "com.datalogic.examples.decode_action";
	public static final String CATEGORY_BROADCAST_RECEIVER = "com.datalogic.examples.decode_category";

	// Default Extra contents added to the intent containing results.
	public static final String EXTRA_DATA = IntentWedge.EXTRA_BARCODE_DATA;
	public static final String EXTRA_TYPE = IntentWedge.EXTRA_BARCODE_TYPE;

	// Action and Category defined in AndroidManifest.xml, associated to a dedicated activity.
	private static final String ACTION = "com.datalogic.examples.STARTINTENT" ;
	private static final String CATEGORY = "android.intent.category.DEFAULT";

	private final String LOGTAG = getClass().getName();

	private BroadcastReceiver receiver = null;
	private IntentFilter filter = null;

	private RadioGroup radioGroup;

	private BarcodeManager manager;
	private ScannerProperties configuration;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create a BarcodeManager. It will be used later to change intent delivery modes.
		manager = new BarcodeManager();

		// Get the Radio Group from the displayed layout.
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		// Associate a specific listener.
		radioGroup.setOnCheckedChangeListener(new MyClickedItemListener());
		// Clear check and force a default radio button checked.
		radioGroup.clearCheck();
		radioGroup.check(R.id.radioStartActivity);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Register dynamically decode wedge intent broadcast receiver.
		receiver = new DecodeWedgeIntentReceiver();
		filter = new IntentFilter();
		filter.addAction(ACTION_BROADCAST_RECEIVER);
		filter.addCategory(CATEGORY_BROADCAST_RECEIVER);
		registerReceiver(receiver, filter);
	}

	@Override
	protected void onPause() {
		Log.i(this.getClass().getName(), "onPause");
		super.onPause();

		// Unregister our BroadcastReceiver.
		unregisterReceiver(receiver);
		receiver = null;
		filter = null;
	}

	// Called when start button is pressed.
	public void onStartBtnPressed (View v) {
		startDecode();
	}

	// Called when stop button is pressed.
	public void onStopBtnPressed (View v) {
		stopDecode();
	}

	// Creates an intent and start decoding.
	private void startDecode() {
		Intent myintent = new Intent();
		myintent.setAction(BarcodeManager.ACTION_START_DECODE);
		sendBroadcast(myintent);
	}

	// Creates an intent and stop decoding.
	private void stopDecode() {
		Intent myintent = new Intent();
		myintent.setAction(BarcodeManager.ACTION_STOP_DECODE);
		sendBroadcast(myintent);
	}

	// Receives action ACTION_BROADCAST_RECEIVER
	public class DecodeWedgeIntentReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent wedgeIntent) {

			String action = wedgeIntent.getAction();

			if (action.equals(ACTION_BROADCAST_RECEIVER)) {

				// Read content of result intent.
				String barcode = wedgeIntent.getStringExtra(EXTRA_DATA);

				showMessage("Received intent broadcast:" + barcode);
				Log.d(LOGTAG, "Decoding Broadcast Received");
			}
		}
	}

	public class MyClickedItemListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// Handle errors through java Exceptions
			ErrorManager.enableExceptions(true);

			try {
				// get the current settings from the BarcodeManager
				configuration = ScannerProperties.edit(manager);
				// disables KeyboardWedge
				configuration.keyboardWedge.enable.set(false);
				// enable wedge intent
				configuration.intentWedge.enable.set(true);

				switch (checkedId) {
				case R.id.radioBroadcast:
					// set wedge intent action and category
					configuration.intentWedge.action.set(ACTION_BROADCAST_RECEIVER);
					configuration.intentWedge.category.set(CATEGORY_BROADCAST_RECEIVER);
					// set wedge intent delivery through broadcast
					configuration.intentWedge.deliveryMode.set(IntentDeliveryMode.BROADCAST);
					configuration.store(manager, false);
					break;
				case R.id.radioStartActivity:
					// set wedge intent action and category
					configuration.intentWedge.action.set(ACTION);
					configuration.intentWedge.category.set(CATEGORY);
					// intent delivery startActivity
					configuration.intentWedge.deliveryMode.set(IntentDeliveryMode.START_ACTIVITY);
					configuration.store(manager, false);
					break;
				case R.id.radioStartService:
					// set wedge intent action and category
					configuration.intentWedge.action.set(ACTION);
					configuration.intentWedge.category.set(CATEGORY);
					// intent delivery startService
					configuration.intentWedge.deliveryMode.set(IntentDeliveryMode.START_SERVICE);
					configuration.store(manager, false);
					break;
				default:
					break;
				}
			} catch (Exception e) { // Any error?
				if(e instanceof ConfigException) {
					ConfigException ex = (ConfigException) e;
					Log.e(LOGTAG, "Error while retrieving/setting properties: " + ex.error_number, ex);
				} else if(e instanceof DecodeException) {
					DecodeException ex = (DecodeException) e;
					Log.e(LOGTAG, "Error while retrieving/setting properties: " + ex.error_number, ex);
				} else {
					Log.e(LOGTAG, "Error in onCheckedChanged", e);
				}
			}
		}

	}

	// Display a simple toast message
	private void showMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}
