// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.devicesampleapi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import com.datalogic.device.DeviceException
import com.datalogic.device.ErrorManager
import com.datalogic.device.location.LocationManager
import com.datalogic.device.location.LocationMode

/**
 * Switches on/off GPS+Network location.
 */
class LocationActivity : Activity() {

    // Displays current gps status: if on or off.
    private var gpsStatus: TextView? = null

    /**
     * Use android.location.LocationManager to determine if GPS is enabled.
     */
    val isGPSEnabled: Boolean
        get() {
            val loc = getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
            return loc.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        gpsStatus = findViewById<TextView>(R.id.gpsStatus)

        val newVal: Boolean = isGPSEnabled
        gpsStatus!!.text = "GPS is " + (if (newVal) "" else "not ") + "enabled"
    }

    /**
     * Activated by btnGps. Sets GPS to the opposite state it is currently in.
     */
    fun btnGpsOnClick(v: View) {
        var newVal = !isGPSEnabled
        setGPSState(newVal)
        try {
            Thread.sleep(300)
        } catch (e: InterruptedException) {
            // It should not fail
            Log.e(javaClass.name, "Error during sleep", e)
        }

        newVal = isGPSEnabled
        gpsStatus!!.text = "GPS is " + (if (newVal) "" else "not ") + "enabled"
    }

    /**
     * Start Location settings activity.
     */
    fun btnSettingsOnClick(v: View) {
        val viewIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(viewIntent)
    }

    /**
     * Use LocationManager to set the gps as enabled (true), or disabled
     * (false).
     */
    fun setGPSState(enable: Boolean) {
        var gps: LocationManager? = null

        // Store previous exception preference.
        val previous = ErrorManager.areExceptionsEnabled()

        // We want to be notified through an exception if something goes wrong.
        ErrorManager.enableExceptions(true)
        try {
            gps = LocationManager()
            gps!!.setLocationMode(if (enable) LocationMode.SENSORS_AND_NETWORK else LocationMode.OFF)
        } catch (e1: DeviceException) {
            // Just in case we get an error.
            Log.e(javaClass.name, "Exception while switching location mode ", e1)
        }

        // Set previous value.
        ErrorManager.enableExceptions(previous)
    }

}
