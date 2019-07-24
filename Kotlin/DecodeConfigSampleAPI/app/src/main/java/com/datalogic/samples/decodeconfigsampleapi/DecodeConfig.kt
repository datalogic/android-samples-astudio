package com.datalogic.samples.decodeconfigsampleapi

// ©2019 Datalogic S.p.A. and/or its affiliates. All rights reserved.

import com.datalogic.decode.BarcodeManager
import com.datalogic.decode.configuration.LengthControlMode
import com.datalogic.decode.configuration.ScannerProperties
import com.datalogic.device.configuration.ConfigException
import com.datalogic.device.ErrorManager

import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.datalogic.samples.R

class DecodeConfiguration : Activity() {

    // Constant for log messages.
    private val LOGTAG = javaClass.name

    internal var manager: BarcodeManager? = null
    internal var configuration: ScannerProperties? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create a BarcodeManager.
        manager = BarcodeManager()

        // Pass it to ScannerProperties class.
        // ScannerProperties cannot be instantiated directly, instead call edit.
        configuration = ScannerProperties.edit(manager)

        // Now we can change some Scanner/Device configuration parameters.
        // These values are not applied, as long as the store method is not called.
        configuration!!.code39.enable.set(true)
        configuration!!.code39.enableChecksum.set(false)
        configuration!!.code39.fullAscii.set(true)
        configuration!!.code39.Length1.set(20)
        configuration!!.code39.Length2.set(2)
        configuration!!.code39.lengthMode.set(LengthControlMode.TWO_FIXED)
        configuration!!.code39.sendChecksum.set(false)
        configuration!!.code39.userID.set('x')

        configuration!!.code128.enable.set(true)
        configuration!!.code128.Length1.set(6)
        configuration!!.code128.Length2.set(2)
        configuration!!.code128.lengthMode.set(LengthControlMode.RANGE)
        configuration!!.code128.userID.set('y')

        if (configuration!!.qrCode.isSupported) {
            configuration!!.qrCode.enable.set(false)
        }

        // Change the IntentWedge action and category to specific ones.
        configuration!!.intentWedge.action.set("com.datalogic.examples.decode_action")
        configuration!!.intentWedge.category.set("com.datalogic.examples.decode_category")

        // From here on, we would like to get a return value instead of an exception in case of error.
        ErrorManager.enableExceptions(false)

        // Now we are ready to store our settings changes.
        // Second parameter set to true saves configuration in a permanent way.
        // After boot, settings will be still valid.
        val errorCode = configuration!!.store(manager, true)

        // Check the return value.
        if (errorCode != ConfigException.SUCCESS) {
            Log.e(LOGTAG, "Error during store", ErrorManager.getLastError())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle menu item selection
        return when (item.itemId) {
            R.id.action_settings -> {
                startSettingsActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Method called by the displayed button
    // Unused parameter 'v' is required by the OnClick attribute in main_activity.xml
    fun buttonClicked(v: View) = startSettingsActivity()

    private fun startSettingsActivity() {
        // Create and start an intent to pop up Android Settings
        val dialogIntent = Intent(android.provider.Settings.ACTION_SETTINGS)
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(dialogIntent)
    }
}
