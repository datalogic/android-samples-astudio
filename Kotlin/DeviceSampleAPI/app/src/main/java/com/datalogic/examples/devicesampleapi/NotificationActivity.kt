// ©2019 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.devicesampleapi

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import com.datalogic.device.DeviceException
import com.datalogic.device.ErrorManager
import com.datalogic.device.notification.Led
import com.datalogic.device.notification.LedManager

/**
 * Activity to use the Led green spot, and blink green spot.
 */
class NotificationActivity : Activity() {

    private var led: LedManager? = null

    private var btnLed: Button? = null
    private var btnLedEnable: Button? = null

    internal var enable = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        // Use exceptions to handle this
        ErrorManager.enableExceptions(true)

        try {
            led = LedManager()
        } catch (e1: Exception) {
            Log.e(javaClass.name, "Error while creating LedManager", e1)
            return
        }

        // Associate blink functionality to blink button.
        btnLed = findViewById<Button>(R.id.btnLed)
        btnLed!!.setOnClickListener(object : OnClickListener {

            override fun onClick(v: View) {
                try {
                    for (i in 0..19) {
                        led!!.setLed(Led.LED_GREEN_SPOT, enable)
                        enable = !enable
                        try {
                            Thread.sleep(500)
                        } catch (e: InterruptedException) {
                            Log.e(javaClass.name, "Sleep for Green spot blink was interrupted", e)
                            break
                        }

                    }
                } catch (e: DeviceException) {
                    Log.e(javaClass.name, "Cannot blink Green spot", e)
                }

            }
        })

        // Associate set led functionality to led button enable.
        btnLedEnable = findViewById<Button>(R.id.btnLedEnable)
        btnLedEnable!!.setOnClickListener(object : OnClickListener {

            override fun onClick(v: View) {
                enable = !enable
                try {
                    led!!.setLed(Led.LED_GREEN_SPOT, enable)
                    btnLedEnable!!.text = "green spot is " + if (enable) "on" else "off"

                } catch (e: DeviceException) {
                    Log.e(javaClass.name, "Cannot set Green spot", e)
                }

            }
        })

        // Turn on Green spot led. When the activity is created
        try {
            led!!.setLed(Led.LED_GREEN_SPOT, enable)
            btnLedEnable!!.text = "green spot is " + if (enable) "on" else "off"
        } catch (e: DeviceException) {
            Log.e(javaClass.name, "Cannot set Green spot", e)
        }

    }
}
