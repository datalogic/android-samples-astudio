// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.devicesampleapi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.datalogic.device.DeviceException;
import com.datalogic.device.input.TouchManager;

/**
 * Activity to use TouchManager.lockInput
 */
public class TouchActivity extends Activity {

	private Button btn1, btn2, btn3, btn4;

	private TouchManager tm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_touch);

		btn1 = (Button) findViewById(R.id.button1);
		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), "Button 1 clicked",
						Toast.LENGTH_SHORT).show();
			}
		});
		btn2 = (Button) findViewById(R.id.button2);
		btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), "Button 2 clicked",
						Toast.LENGTH_SHORT).show();
			}
		});
		btn3 = (Button) findViewById(R.id.button3);
		btn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), "Button 3 clicked",
						Toast.LENGTH_SHORT).show();
			}
		});
		btn4 = (Button) findViewById(R.id.button4);
		btn4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), "Button 4 clicked",
						Toast.LENGTH_SHORT).show();
			}
		});

		try {
			tm = new TouchManager();
		} catch (DeviceException e) {
			android.util.Log.e(getClass().getName(), "While creating activity", e);
			return;
		}
	}

	/**
	 * Activated by btnTLock. Starts a LockThread
	 * 
	 * @param v
	 */
	public void btnTLockOnClick(View v) {
		new LockThread().start();
	}

	/**
	 * Lock input through the TouchManager, sleep for two seconds, then unlock
	 * input.
	 */
	public class LockThread extends Thread {

		@Override
		public void run() {
			try {
				// Lock
				tm.lockInput(true);

				// Allow play
				Thread.sleep(2000);

				// Unlock
				tm.lockInput(false);
			} catch (Exception e) {
				android.util.Log.e(getClass().getName(), "run in LockThread", e);
			} 
		}

	}

}
