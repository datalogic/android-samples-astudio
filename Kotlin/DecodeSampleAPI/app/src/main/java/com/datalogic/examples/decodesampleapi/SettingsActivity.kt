// ©2019 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.decodesampleapi

import java.util.HashMap

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.preference.CheckBoxPreference
import android.preference.Preference
import android.preference.Preference.OnPreferenceClickListener
import android.preference.PreferenceActivity
import android.preference.PreferenceCategory
import android.preference.PreferenceScreen
import android.util.Log

import com.datalogic.decode.BarcodeManager
import com.datalogic.decode.DecodeException
import com.datalogic.decode.Symbology

/**
 * A PreferenceActivity that presents a set of application settings.
 */
class SettingsActivity : PreferenceActivity() {

    /**
     * @see OnChangeHandler
     */
    private var handler: Handler? = null

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        if (decoder == null) {
            try {
                decoder = BarcodeManager()
            } catch (e: DecodeException) {
                e.printStackTrace()
                Log.e(TAG, "Unable to instantiate BarcodeManager")
                return
            }

        }

        handler = Handler(OnChangeHandler())

        if (listener == null) {
            listener = SymbologyPreferenceListener(handler ?: return)
        }

        // In the simplified UI, fragments are not used at all and we instead
        // use the older PreferenceActivity APIs.

        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.pref_general)

        addEnablers(preferenceScreen, this)
    }

    override fun onPostResume() {
        super.onPostResume()
        load()
    }

    /**
     * @Precondition : decoder exists. preferenceMap contains mapping from each
     * Symbology to each CheckboxPreference.
     * @Postcondition : All CheckboxPreferences in preferenceMap will be filled
     * with values from their prospective Symbologies.
     */
    private fun load() {
        var value: Boolean?

        try {
            for (s in preferenceMap!!.keys) {
                if (decoder!!.isSymbologySupported(s)) {
                    val pref = preferenceMap!![s]
                    value = decoder!!.isSymbologyEnabled(s)
                    pref!!.isChecked = value
                }
            }
        } catch (e: DecodeException) {
            Log.e(TAG, "load", e)
        }

    }

    /**
     * HANDLE_CHANGED refreshes check boxes. HANDLE_SYMBOLOGY receives a
     * CheckBoxPreference and enables or disables that symbology.
     */
    inner class OnChangeHandler : Handler.Callback {

        override fun handleMessage(msg: Message): Boolean {
            when (msg.what) {
                HANDLE_SYMBOLOGY -> {
                    onSymbologyChanged(msg.obj as CheckBoxPreference)
                    load()
                }
                else -> {
                }
            }
            return false
        }

    }

    /**
     * @param checky
     * The title is a key in symbologyMap. That symbology is enabled
     * or disabled from the value of checky.
     */
    private fun onSymbologyChanged(checky: CheckBoxPreference) {
        val s = symbologyMap!![checky.title]
        try {
            if (decoder!!.isSymbologySupported(s)) {
                val `val` = checky.isChecked
                decoder!!.enableSymbology(s, `val`)
            }
        } catch (e: DecodeException) {
            Log.e(TAG, "onSymbologyChanged", e)
        }

    }

    inner class SymbologyPreferenceListener(private val handler: Handler) : OnPreferenceClickListener {

        override fun onPreferenceClick(preference: Preference): Boolean {
            val msg = handler.obtainMessage()
            msg.what = HANDLE_SYMBOLOGY
            msg.obj = preference

            handler.sendMessage(msg)
            return false
        }

    }

    companion object {

        private const val TAG = "SettingsActivity"
        /**
         * Event to handle a symbology checkbox.
         */
        const val HANDLE_SYMBOLOGY = 1

        private var decoder: BarcodeManager? = null

        /**
         * Enable or disable symbologies as the check box is used.
         */
        private var listener: SymbologyPreferenceListener? = null

        internal var symbologyMap: HashMap<String, Symbology>? = null
        internal var preferenceMap: HashMap<Symbology, CheckBoxPreference>? = null

        private fun addEnablers(ps: PreferenceScreen?, context: Context) {
            if (ps == null)
                return
            val groupy = PreferenceCategory(context)
            groupy.isSelectable = false
            groupy.title = "Symbologies Enabled"
            ps.addPreference(groupy)

            if (symbologyMap == null) {
                symbologyMap = HashMap()
            }
            preferenceMap = HashMap()
            val symbologies = Symbology.values()

            var checky: CheckBoxPreference
            var name: String
            var value: Boolean?

            try {
                for (s in symbologies) {
                    name = s.toString()
                    symbologyMap!![name] = s
                    checky = CheckBoxPreference(context)
                    if (decoder!!.isSymbologySupported(s)) {
                        value = decoder!!.isSymbologyEnabled(s)
                    } else {
                        value = false
                        checky.isSelectable = false
                    }
                    checky.title = name
                    checky.isChecked = value
                    checky.onPreferenceClickListener = listener
                    ps.addPreference(checky)
                    preferenceMap!![s] = checky
                }
            } catch (e: DecodeException) {
                Log.e(TAG, "addEnablers", e)
            }

        }
    }
}
