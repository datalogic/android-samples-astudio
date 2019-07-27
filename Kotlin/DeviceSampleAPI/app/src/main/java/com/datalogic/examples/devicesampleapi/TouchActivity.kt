// ©2019 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.devicesampleapi

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.datalogic.device.DeviceException
import com.datalogic.device.input.TouchManager

/**
 * Activity to use TouchManager.lockInput
 */
class TouchActivity : Activity() {

    private var btn1: Button? = null
    private var btn2: Button? = null
    private var btn3: Button? = null
    private var btn4: Button? = null

    private var tm: TouchManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_touch)

        btn1 = findViewById<Button>(R.id.button1)
        btn1!!.setOnClickListener {
            Toast.makeText(
                baseContext, "Button 1 clicked",
                Toast.LENGTH_SHORT
            ).show()
        }
        btn2 = findViewById<Button>(R.id.button2)
        btn2!!.setOnClickListener {
            Toast.makeText(
                baseContext, "Button 2 clicked",
                Toast.LENGTH_SHORT
            ).show()
        }
        btn3 = findViewById<Button>(R.id.button3)
        btn3!!.setOnClickListener {
            Toast.makeText(
                baseContext, "Button 3 clicked",
                Toast.LENGTH_SHORT
            ).show()
        }
        btn4 = findViewById<Button>(R.id.button4)
        btn4!!.setOnClickListener {
            Toast.makeText(
                baseContext, "Button 4 clicked",
                Toast.LENGTH_SHORT
            ).show()
        }

        try {
            tm = TouchManager()
        } catch (e: DeviceException) {
            android.util.Log.e(javaClass.name, "While creating activity", e)
            return
        }

    }

    /**
     * Activated by btnTLock. Starts a LockThread
     *
     * @param v
     */
    fun btnTLockOnClick(v: View) {
        LockThread().start()
    }

    /**
     * Lock input through the TouchManager, sleep for two seconds, then unlock
     * input.
     */
    inner class LockThread : Thread() {

        override fun run() {
            try {
                // Lock
                tm!!.lockInput(true)

                // Allow play
                Thread.sleep(2000)

                // Unlock
                tm!!.lockInput(false)
            } catch (e: Exception) {
                android.util.Log.e(javaClass.name, "run in LockThread", e)
            }

        }

    }

}
