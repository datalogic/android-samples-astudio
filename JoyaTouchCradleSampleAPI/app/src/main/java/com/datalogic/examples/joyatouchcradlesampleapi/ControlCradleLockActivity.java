// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.joyatouchcradlesampleapi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.datalogic.examples.joyatouchcradlesampleapi.R;
import com.datalogic.extension.selfshopping.cradle.joyatouch.CradleJoyaTouch;
import com.datalogic.extension.selfshopping.cradle.joyatouch.LockAction;

/**
 * Activity for Cradle lock control.
 */
public class ControlCradleLockActivity extends Activity
{
	private final static String[] NAMES =
	{
		"LOCK",
		"LOCK WITH LED OFF",
		"UNLOCK",
		"UNLOCK WITH LED ON"
	};

	private final static LockAction[] ACTIONS =
	{
		LockAction.LOCK,
		LockAction.LOCK_WITH_LED_OFF,
		LockAction.UNLOCK,
		LockAction.UNLOCK_WITH_LED_ON
	};
	
	private CradleJoyaTouch jtCradle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cradle_lock);

		JoyaTouchCradleApplication application = (JoyaTouchCradleApplication)getApplicationContext();
		jtCradle = application.getCradleJoyaTouch();
		
		// Fill the Adapter with the available LED names/actions
		ListView listMainActivities = (ListView) findViewById(R.id.listLockActions);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, NAMES);
		listMainActivities.setAdapter(adapter);
		listMainActivities.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				setLock(ACTIONS[position]);
			}
		});
	}
	
	public void setLock(LockAction action)
	{
		if (jtCradle.controlLock(action))
		{
			Toast.makeText(this, action.toString(), Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(this, "Failed to control the lock. Check if the device is inserted in the Cradle",
					Toast.LENGTH_LONG).show();
		}
	}
}
