// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.joyatouchcradlesampleapi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.datalogic.examples.joyatouchcradlesampleapi.R;
import com.datalogic.extension.selfshopping.cradle.joyatouch.CradleJoyaTouch;
import com.datalogic.extension.selfshopping.cradle.joyatouch.LedAction;

/**
 * Activity for Cradle LED control.
 */
public class ControlCradleLEDActivity extends Activity
{
	private final static String[] NAMES =
	{
		"LED ON",
		"LED OFF",
		"LED BLINK FAST",
		"LED BLINK SLOW",
		"LED TOGGLE"	
	};

	private final static LedAction[] ACTIONS =
	{
		LedAction.ON,
		LedAction.OFF,
		LedAction.BLINK_FAST,
		LedAction.BLINK_SLOW,
		LedAction.TOGGLE
	};
	
	private CradleJoyaTouch jtCradle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cradle_led);

		JoyaTouchCradleApplication application = (JoyaTouchCradleApplication)getApplicationContext();
		jtCradle = application.getCradleJoyaTouch();
		
		// Fill the Adapter with the available LED names/actions
		ListView listMainActivities = (ListView) findViewById(R.id.listLedActions);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, NAMES);
		listMainActivities.setAdapter(adapter);
		listMainActivities.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				setLed(ACTIONS[position]);
			}
		});
	}
	
	public void setLed(LedAction action)
	{
		if (jtCradle.controlLed(action))
		{
			Toast.makeText(this, action.toString(), Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(this, "Failed to control the LED. Check if the device is inserted in the Cradle",
					Toast.LENGTH_LONG).show();
		}
	}
}
