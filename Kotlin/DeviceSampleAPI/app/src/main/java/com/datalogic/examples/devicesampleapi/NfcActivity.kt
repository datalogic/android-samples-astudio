// ©2019 Datalogic S.p.A. and/or its affiliates. All rights reserved.

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

/**
 * Activity to enable/disable Nfc
 */
class NfcActivity : Activity() {

    private var nfcStatus: TextView? = null

    /**
     * @return True if Nfc is enabled, false otherwise.
     */
    val isNfcEnabled: Boolean
        get() {
            val manager = getSystemService(Context.NFC_SERVICE) as android.nfc.NfcManager
            val adapter = manager.defaultAdapter
            return adapter.isEnabled
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)

        nfcStatus = findViewById<TextView>(R.id.nfcStatus)

        val newVal: Boolean = isNfcEnabled
        nfcStatus!!.text = "Nfc is " + (if (newVal) "" else "not ") + "enabled"
    }

    /**
     * Start NFC settings activity.
     */
    fun btnSettingsOnClick(v: View) {
        val viewIntent = Intent(Settings.ACTION_NFC_SETTINGS)
        startActivity(viewIntent)
    }

    fun btnSwitchNFC(v: View) {
        val newNfcState = !isNfcEnabled
        setEnableNfc(newNfcState)
        try {
            Thread.sleep(500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        nfcStatus!!.text = "Nfc is " + (if (newNfcState) "" else "not ") + "enabled"
    }

    /**
     * Enable or disable Nfc, using com.datalogic.device.nfc.NfcManager.
     */
    fun setEnableNfc(enable: Boolean) {
        val previous = ErrorManager.areExceptionsEnabled()
        ErrorManager.enableExceptions(false)
        ErrorManager.clearErrors()

        val error = com.datalogic.device.nfc.NfcManager().enableNfcAdapter(enable)
        if (error != DeviceException.SUCCESS) {
            Log.e(javaClass.name, "Error while setting NFC", ErrorManager.getLastError())
        }
        ErrorManager.enableExceptions(previous)
    }

}
