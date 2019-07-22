// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.devicesampleapi;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.datalogic.device.DeviceException;
import com.datalogic.device.ErrorManager;
import com.datalogic.device.power.PowerManager;
import com.datalogic.device.power.SuspendTimeout;
import com.datalogic.device.power.WakeupSource;

/**
 * Activity to edit settings involving suspending the device.
 */
public class SleepActivity extends Activity {

	private static String[] timeouts = null;
	private static SuspendTimeout[] timeoutVals = null;
	private static String[] sources = null;
	private static ArrayList<WakeupSource> sourceVals = new ArrayList<WakeupSource>();

	private TextView txtSleep;
	private ListView listSuspendTimeout;
	private ListView listWakeupSource;

	private PowerManager pm;
	private ArrayList<WakeupSource> sourceList = new ArrayList<WakeupSource>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_sleep);

		ErrorManager.enableExceptions(true);

		try {
			pm = new PowerManager();
		} catch (DeviceException e) {
			android.util.Log.e(getClass().getName(), "While creating activity", e);
			return;
		}

		txtSleep = (TextView) findViewById(R.id.txtSleep);

		// load available timeouts
		setTimeouts();
		// load wakeupsources
		setSources();

		ArrayAdapter<String> timeoutAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, timeouts);
		ArrayAdapter<String> sourceAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, sources);

		listSuspendTimeout = (ListView) findViewById(R.id.listSuspendTimeout);
		listSuspendTimeout.setAdapter(timeoutAdapter);
		listWakeupSource = (ListView) findViewById(R.id.listWakeupSource);
		listWakeupSource.setAdapter(sourceAdapter);

		listSuspendTimeout.setOnItemClickListener(new TimeoutListListener());
		listWakeupSource.setOnItemClickListener(new SourceListListener());

		setText();
	}

	/**
	 * set txtSleep with getDescription
	 */
	private void setText() {
		txtSleep.setText(getDescription());
	}

	/**
	 * Initialize sources and sourceVals.
	 */
	private void setSources() {
		if (sources == null) {
			WakeupSource[] definedSources = WakeupSource.values();
			for(int i = 0; i < definedSources.length; i++) {
				if(pm.isWakeupSupported(definedSources[i])) {
					sourceVals.add(definedSources[i]);
				}
			}
			sources = new String[sourceVals.size()];
			for (int i = 0; i < sources.length; i++) {
				sources[i] = sourceVals.get(i).name();
			}

		}
	}

	/**
	 * Initialize timeouts and timeoutVals.
	 */
	private void setTimeouts() {
		if (timeouts == null) {
			timeoutVals = SuspendTimeout.values();
			timeouts = new String[timeoutVals.length];
			for (int i = 0; i < timeouts.length; i++) {
				timeouts[i] = "" + timeoutVals[i].name();
			}
		}
	}

	/**
	 * @return String representation of PowerManager information regarding
	 *         suspending the device.
	 */
	protected String getDescription() {
		StringBuilder outVal = new StringBuilder();

		try {
			outVal.append("Suspend Timeout(external): "
					+ pm.getSuspendTimeout(true) + "\n");
			outVal.append("Suspend Timeout(internal): "
					+ pm.getSuspendTimeout(false) + "\n");
			for(WakeupSource s : sourceVals) {
				outVal.append("isWakeupActive");
				outVal.append("(" + s.name() +"): ");
				outVal.append("" + pm.isWakeupActive(s) + "\n");
			}

			try {
				outVal.append("getWakeupReason: " + pm.getWakeupReason());
			} catch (Exception e) {
				android.util.Log.e(getClass().getName(), "Did the device go to sleep?", e);
			}

		} catch (DeviceException e1) {
			android.util.Log.e(getClass().getName(), "getDescription", e1);
		}

		return outVal.toString();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.reset, menu);
		return true;
	}

	/**
	 * When menu button is clicked, clear active wakeups.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_reset:
			setText();

			clearWakeup();

			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * pm.clearWakeup with all Integers in sourceList.
	 * Postcondition : sourceList will be emptied.
	 */
	private void clearWakeup ( ) {
		for (WakeupSource source : sourceList) {
			try {
				pm.clearWakeup(source);
			} catch (DeviceException e) {
				android.util.Log.e(getClass().getName(), "clearWakeup", e);
			}
		}
		sourceList.clear();

		try{
			Thread.sleep(100);
		} catch (InterruptedException e) {}

		setText();
	}

	/**
	 * Set the timeout to suspend the device. pos is the index in timeoutVals.
	 * 
	 * Currently external source is always true.
	 */
	public class TimeoutListListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			try {
				pm.setSuspendTimeout(timeoutVals[pos], true);
			} catch (DeviceException e) {
				android.util.Log.e(getClass().getName(), "onItemClick", e);
			}

			try{
				Thread.sleep(100);
			} catch (InterruptedException e) {}

			setText();
		}
	}

	/**
	 * Activate a wakeup source. pos is the index of a value in sourceVals.
	 */
	public class SourceListListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			try {
				if(!pm.isWakeupActive(sourceVals.get(pos)))
					pm.activateWakeup(sourceVals.get(pos));
				else
					pm.clearWakeup(sourceVals.get(pos));
				sourceList.add(sourceVals.get(pos));
			} catch (DeviceException e) {
				android.util.Log.e(getClass().getName(), "onItemClick", e);
			}

			try{
				Thread.sleep(100);
			} catch (InterruptedException e) {}

			setText();
		}

	}

}
