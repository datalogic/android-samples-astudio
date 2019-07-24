package com.datalogic.examples.decodeintent

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import android.widget.Toast

import com.datalogic.decode.BarcodeManager
import com.datalogic.decode.DecodeException
import com.datalogic.decode.configuration.IntentDeliveryMode
import com.datalogic.decode.configuration.IntentWedge
import com.datalogic.decode.configuration.ScannerProperties
import com.datalogic.device.ErrorManager
import com.datalogic.device.configuration.ConfigException

/**
 * During creation initializes a radio button change listener, then register its broadcast receiver.
 * Each time the checked radio button changes, MyClickedItemListener class is invoked.
 */
class IntentWedgeSample : Activity() {

    private val LOGTAG = javaClass.name

    private var receiver: BroadcastReceiver? = null
    private var filter: IntentFilter? = null

    private var radioGroup: RadioGroup? = null

    private var manager: BarcodeManager? = null
    private var configuration: ScannerProperties? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create a BarcodeManager. It will be used later to change intent delivery modes.
        manager = BarcodeManager()

        // Get the Radio Group from the displayed layout.
        radioGroup = findViewById(R.id.radioGroup) as RadioGroup
        // Associate a specific listener.
        radioGroup!!.setOnCheckedChangeListener(MyClickedItemListener())
        // Clear check and force a default radio button checked.
        radioGroup!!.clearCheck()
        radioGroup!!.check(R.id.radioStartActivity)
    }

    override fun onResume() {
        super.onResume()

        // Register dynamically decode wedge intent broadcast receiver.
        receiver = DecodeWedgeIntentReceiver()
        filter = IntentFilter()
        filter!!.addAction(ACTION_BROADCAST_RECEIVER)
        filter!!.addCategory(CATEGORY_BROADCAST_RECEIVER)
        registerReceiver(receiver, filter)
    }

    override fun onPause() {
        Log.i(this.javaClass.name, "onPause")
        super.onPause()

        // Unregister our BroadcastReceiver.
        unregisterReceiver(receiver)
        receiver = null
        filter = null
    }

    // Receives action ACTION_BROADCAST_RECEIVER
    inner class DecodeWedgeIntentReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, wedgeIntent: Intent) {

            val action = wedgeIntent.action

            if (action == ACTION_BROADCAST_RECEIVER) {

                // Read content of result intent.
                val barcode = wedgeIntent.getStringExtra(EXTRA_DATA_STRING)

                showMessage("Received intent broadcast:" + barcode!!)
                Log.d(LOGTAG, "Decoding Broadcast Received")
            }
        }
    }

    inner class MyClickedItemListener : OnCheckedChangeListener {

        override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
            // Handle errors through java Exceptions
            ErrorManager.enableExceptions(true)

            try {
                // get the current settings from the BarcodeManager
                configuration = ScannerProperties.edit(manager)
                // disables KeyboardWedge
                configuration!!.keyboardWedge.enable.set(false)
                // enable wedge intent
                configuration!!.intentWedge.enable.set(true)

                when (checkedId) {
                    R.id.radioBroadcast -> {
                        // set wedge intent action and category
                        configuration!!.intentWedge.action.set(ACTION_BROADCAST_RECEIVER)
                        configuration!!.intentWedge.category.set(CATEGORY_BROADCAST_RECEIVER)
                        // set wedge intent delivery through broadcast
                        configuration!!.intentWedge.deliveryMode.set(IntentDeliveryMode.BROADCAST)
                        configuration!!.store(manager, false)
                    }
                    R.id.radioStartActivity -> {
                        // set wedge intent action and category
                        configuration!!.intentWedge.action.set(ACTION)
                        configuration!!.intentWedge.category.set(CATEGORY)
                        // intent delivery startActivity
                        configuration!!.intentWedge.deliveryMode.set(IntentDeliveryMode.START_ACTIVITY)
                        configuration!!.store(manager, false)
                    }
                    R.id.radioStartService -> {
                        // set wedge intent action and category
                        configuration!!.intentWedge.action.set(ACTION)
                        configuration!!.intentWedge.category.set(CATEGORY)
                        // intent delivery startService
                        configuration!!.intentWedge.deliveryMode.set(IntentDeliveryMode.START_SERVICE)
                        configuration!!.store(manager, false)
                    }
                    else -> {
                    }
                }
            } catch (e: Exception) { // Any error?
                if (e is ConfigException) {
                    Log.e(LOGTAG, "Error while retrieving/setting properties: " + e.error_number, e)
                } else if (e is DecodeException) {
                    Log.e(LOGTAG, "Error while retrieving/setting properties: " + e.error_number, e)
                } else {
                    Log.e(LOGTAG, "Error in onCheckedChanged", e)
                }
            }

        }

    }

    // Display a simple toast message
    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        // Constants for Broadcast Receiver defined below.
        val ACTION_BROADCAST_RECEIVER = "com.datalogic.examples.decode_action"
        val CATEGORY_BROADCAST_RECEIVER = "com.datalogic.examples.decode_category"

        // Default Extra contents added to the intent containing results.
        val EXTRA_DATA = IntentWedge.EXTRA_BARCODE_DATA
        val EXTRA_TYPE = IntentWedge.EXTRA_BARCODE_TYPE
        val EXTRA_DATA_STRING = IntentWedge.EXTRA_BARCODE_STRING

        // Action and Category defined in AndroidManifest.xml, associated to a dedicated activity.
        private val ACTION = "com.datalogic.examples.STARTINTENT"
        private val CATEGORY = "android.intent.category.DEFAULT"
    }
}
