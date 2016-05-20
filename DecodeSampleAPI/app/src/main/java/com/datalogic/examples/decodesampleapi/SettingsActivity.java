// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.decodesampleapi;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.util.Log;

import com.datalogic.decode.BarcodeManager;
import com.datalogic.decode.DecodeException;
import com.datalogic.decode.Symbology;

/**
 * A PreferenceActivity that presents a set of application settings.
 */
public class SettingsActivity extends PreferenceActivity {

	private static final String TAG = "SettingsActivity";
	/**
	 * Event to handle a symbology checkbox.
	 */
	public final static int HANDLE_SYMBOLOGY = 1;

	private static BarcodeManager decoder = null;

	/**
	 * Enable or disable symbologies as the check box is used.
	 */
	private static SymbologyPreferenceListener listener = null;

	static HashMap<String, Symbology> symbologyMap = null;
	static HashMap<Symbology, CheckBoxPreference> preferenceMap = null;

	/**
	 * @see OnChangeHandler
	 */
	private Handler handler = null;

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		if (decoder == null) {
			try {
				decoder = new BarcodeManager();
			} catch (DecodeException e) {
				e.printStackTrace();
				Log.e(TAG, "Unable to instantiate BarcodeManager");
				return;
			}
		}

		handler = new Handler(new OnChangeHandler());

		if (listener == null) {
			listener = new SymbologyPreferenceListener(handler);
		}

		// In the simplified UI, fragments are not used at all and we instead
		// use the older PreferenceActivity APIs.

		// Add 'general' preferences.
		addPreferencesFromResource(R.xml.pref_general);

		addEnablers(getPreferenceScreen(), this);
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
		load();
	}

	private static void addEnablers(PreferenceScreen ps, Context context) {
		if (ps == null)
			return;
		PreferenceCategory groupy = new PreferenceCategory(context);
		groupy.setSelectable(false);
		groupy.setTitle("Symbologies Enabled");
		ps.addPreference(groupy);

		if (symbologyMap == null) {
			symbologyMap = new HashMap<String, Symbology>();
		}
		preferenceMap = new HashMap<Symbology, CheckBoxPreference>();
		Symbology[] symbologies = Symbology.values();

		CheckBoxPreference checky;
		String name;
		Boolean value;

		try {
			for (Symbology s : symbologies) {
				name = s.toString();
				symbologyMap.put(name, s);
				checky = new CheckBoxPreference(context);
				if (decoder.isSymbologySupported(s)) {
					value = decoder.isSymbologyEnabled(s);
				} else {
					value = false;
					checky.setSelectable(false);
				}
				checky.setTitle(name);
				checky.setChecked(value);
				checky.setOnPreferenceClickListener(listener);
				ps.addPreference(checky);
				preferenceMap.put(s, checky);
			}
		} catch (DecodeException e) {
			Log.e(TAG, "addEnablers", e);
		}
	}

	/**
	 * @Precondition : decoder exists. preferenceMap contains mapping from each
	 *               Symbology to each CheckboxPreference.
	 * @Postcondition : All CheckboxPreferences in preferenceMap will be filled
	 *                with values from their prospective Symbologies.
	 */
	private void load() {
		Boolean value;

		try {
			for (Symbology s : preferenceMap.keySet()) {
				if (decoder.isSymbologySupported(s)) {
					CheckBoxPreference pref = preferenceMap.get(s);
					value = decoder.isSymbologyEnabled(s);
					pref.setChecked(value);
				}
			}
		} catch (DecodeException e) {
			Log.e(TAG, "load", e);
		}
	}

	/**
	 * HANDLE_CHANGED refreshes check boxes. HANDLE_SYMBOLOGY receives a
	 * CheckBoxPreference and enables or disables that symbology.
	 */
	public class OnChangeHandler implements Handler.Callback {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLE_SYMBOLOGY:
				onSymbologyChanged((CheckBoxPreference) msg.obj);
				load();
				break;
			default:
				break;
			}
			return false;
		}

	}

	/**
	 * @param checky
	 *            The title is a key in symbologyMap. That symbology is enabled
	 *            or disabled from the value of checky.
	 */
	private void onSymbologyChanged(CheckBoxPreference checky) {
		Symbology s = symbologyMap.get(checky.getTitle());
		try {
			if (decoder.isSymbologySupported(s)) {
				Boolean val = checky.isChecked();
				decoder.enableSymbology(s, val);
			}
		} catch (DecodeException e) {
			Log.e(TAG, "onSymbologyChanged", e);
		}

	}

	public class SymbologyPreferenceListener implements OnPreferenceClickListener {

		private Handler handler ;

		public SymbologyPreferenceListener ( Handler handler ) {
			this.handler = handler ;
		}

		@Override
		public boolean onPreferenceClick(Preference preference) {
			Message msg = handler.obtainMessage() ;
			msg.what = SettingsActivity.HANDLE_SYMBOLOGY ;
			msg.obj = preference;

			handler.sendMessage(msg);
			return false;
		}

	}
}
