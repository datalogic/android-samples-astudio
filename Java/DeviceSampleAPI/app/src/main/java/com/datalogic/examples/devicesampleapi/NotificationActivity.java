// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.devicesampleapi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.datalogic.device.DeviceException;
import com.datalogic.device.ErrorManager;
import com.datalogic.device.notification.Led;
import com.datalogic.device.notification.LedManager;

/**
 * Activity to use the Led green spot, and blink green spot.
 */
public class NotificationActivity extends Activity {

	private LedManager led;

	private Button btnLed;
	private Button btnLedEnable;

	boolean enable = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);

		// Use exceptions to handle this
		ErrorManager.enableExceptions(true);

		try {
			led = new LedManager();
		} catch (Exception e1) {
			Log.e(getClass().getName(), "Error while creating LedManager", e1);
			return;
		}

		// Associate blink functionality to blink button.
		btnLed = (Button) findViewById(R.id.btnLed);
		btnLed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					led.blinkLed(Led.LED_GREEN_SPOT, 10, 500, 500);
				} catch (DeviceException e) {
					Log.e(getClass().getName(), "Cannot blink Green spot", e);
				}
			}
		});

		// Associate set led functionality to led button enable.
		btnLedEnable = (Button) findViewById(R.id.btnLedEnable);
		btnLedEnable.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				enable = !enable;
				try {
					led.setLed(Led.LED_GREEN_SPOT, enable);
					btnLedEnable.setText("green spot is " + (enable ? "on" : "off"));

				} catch (DeviceException e) {
					Log.e(getClass().getName(), "Cannot set Green spot", e);
				}

			}
		});

		// Turn on Green spot led. When the activity is created
		try {
			led.setLed(Led.LED_GREEN_SPOT, enable);
			btnLedEnable.setText("green spot is " + (enable ? "on" : "off"));
		} catch (DeviceException e) {
			Log.e(getClass().getName(), "Cannot set Green spot", e);
		}
	}
}
