package com.datalogic.samples.decodelistener

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

import com.datalogic.decode.BarcodeManager
import com.datalogic.decode.DecodeException
import com.datalogic.decode.ReadListener
import com.datalogic.device.ErrorManager


class DecodeListener : Activity() {

    private val LOGTAG = javaClass.name

    internal var decoder: BarcodeManager? = null
    internal var listener: ReadListener? = null
    internal lateinit var mBarcodeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Retrieve the TextView from the displayed layout.
        mBarcodeText = findViewById<TextView>(R.id.editText1)

        // Prevent soft keyboard from popping up while selecting text.
        mBarcodeText.showSoftInputOnFocus = false
    }

    override fun onResume() {
        super.onResume()

        Log.i(LOGTAG, "onResume")

        // If the decoder instance is null, create it.
        if (decoder == null) { // Remember an onPause call will set it to null.
            decoder = BarcodeManager()
        }

        // From here on, we want to be notified with exceptions in case of errors.
        ErrorManager.enableExceptions(true)

        try {

            // Create an anonymous class.
            listener = ReadListener { decodeResult ->
                // Implement the callback method.
                // Change the displayed text to the current received result.
                mBarcodeText.text = decodeResult.text
            }

            // Remember to add it, as a listener.
            decoder!!.addReadListener(listener)

        } catch (e: DecodeException) {
            Log.e(LOGTAG, "Error while trying to bind a listener to BarcodeManager", e)
        }

    }

    override fun onPause() {
        super.onPause()

        Log.i(LOGTAG, "onPause")

        // If we have an instance of BarcodeManager.
        if (decoder != null) {
            try {
                // Unregister our listener from it and free resources.
                decoder!!.removeReadListener(listener)

                // Let the garbage collector take care of our reference.
                decoder = null
            } catch (e: Exception) {
                Log.e(LOGTAG, "Error while trying to remove a listener from BarcodeManager", e)
            }

        }
    }
}

