// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.devicesampleapi

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.datalogic.device.DeviceException
import com.datalogic.device.ErrorManager
import com.datalogic.device.power.PowerManager
import com.datalogic.device.power.SuspendTimeout
import com.datalogic.device.power.WakeupSource

import java.util.ArrayList

/**
 * Activity to edit settings involving suspending the device.
 */
class SleepActivity : Activity() {

    private var txtSleep: TextView? = null
    private var listSuspendTimeout: ListView? = null
    private var listWakeupSource: ListView? = null

    private var pm: PowerManager? = null
    private val sourceList = ArrayList<WakeupSource>()

    /**
     * @return String representation of PowerManager information regarding
     * suspending the device.
     */
    private val description: String
        get() {
            val outVal = StringBuilder()

            try {
                outVal.append(
                    "Suspend Timeout(external): "
                            + pm!!.getSuspendTimeout(true) + "\n"
                )
                outVal.append(
                    "Suspend Timeout(internal): "
                            + pm!!.getSuspendTimeout(false) + "\n"
                )
                for (s in sourceVals) {
                    outVal.append("isWakeupActive")
                    outVal.append("(" + s.name + "): ")
                    outVal.append("" + pm!!.isWakeupActive(s) + "\n")
                }

                try {
                    outVal.append("getWakeupReason: " + pm!!.wakeupReason)
                } catch (e: Exception) {
                    android.util.Log.e(javaClass.name, "Did the device go to sleep?", e)
                }

            } catch (e1: DeviceException) {
                android.util.Log.e(javaClass.name, "getDescription", e1)
            }

            return outVal.toString()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sleep)

        ErrorManager.enableExceptions(true)

        try {
            pm = PowerManager()
        } catch (e: DeviceException) {
            android.util.Log.e(javaClass.name, "While creating activity", e)
            return
        }

        txtSleep = findViewById<TextView>(R.id.txtSleep)

        // load available timeouts
        setTimeouts()
        // load wakeupsources
        setSources()

        val timeoutAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, timeouts!!
        )
        val sourceAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, sources!!
        )

        listSuspendTimeout = findViewById<ListView>(R.id.listSuspendTimeout)
        listSuspendTimeout!!.adapter = timeoutAdapter
        listWakeupSource = findViewById<ListView>(R.id.listWakeupSource)
        listWakeupSource!!.adapter = sourceAdapter

        listSuspendTimeout!!.onItemClickListener = TimeoutListListener()
        listWakeupSource!!.onItemClickListener = SourceListListener()

        setText()
    }

    /**
     * set txtSleep with getDescription
     */
    private fun setText() {
        txtSleep!!.text = description
    }

    /**
     * Initialize sources and sourceVals.
     */
    private fun setSources() {
        if (sources == null) {
            val definedSources = WakeupSource.values()
            for (i in definedSources.indices) {
                if (pm!!.isWakeupSupported(definedSources[i])) {
                    sourceVals.add(definedSources[i])
                }
            }
            sources = arrayOfNulls(sourceVals.size)
            for (i in sources!!.indices) {
                sources[i] = sourceVals.get(i).name()
            }

        }
    }

    /**
     * Initialize timeouts and timeoutVals.
     */
    private fun setTimeouts() {
        if (timeouts == null) {
            timeoutVals = SuspendTimeout.values()
            timeouts = arrayOfNulls(timeoutVals!!.size)
            for (i in timeouts!!.indices) {
                timeouts[i] = "" + timeoutVals!![i].name()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.reset, menu)
        return true
    }

    /**
     * When menu button is clicked, clear active wakeups.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_reset -> {
                setText()

                clearWakeup()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * pm.clearWakeup with all Integers in sourceList.
     * Postcondition : sourceList will be emptied.
     */
    private fun clearWakeup() {
        for (source in sourceList) {
            try {
                pm!!.clearWakeup(source)
            } catch (e: DeviceException) {
                android.util.Log.e(javaClass.name, "clearWakeup", e)
            }

        }
        sourceList.clear()

        try {
            Thread.sleep(100)
        } catch (e: InterruptedException) {
        }

        setText()
    }

    /**
     * Set the timeout to suspend the device. pos is the index in timeoutVals.
     *
     * Currently external source is always true.
     */
    inner class TimeoutListListener : OnItemClickListener {

        override fun onItemClick(
            arg0: AdapterView<*>, arg1: View, pos: Int,
            arg3: Long
        ) {
            try {
                pm!!.setSuspendTimeout(timeoutVals!![pos], true)
            } catch (e: DeviceException) {
                android.util.Log.e(javaClass.name, "onItemClick", e)
            }

            try {
                Thread.sleep(100)
            } catch (e: InterruptedException) {
            }

            setText()
        }
    }

    /**
     * Activate a wakeup source. pos is the index of a value in sourceVals.
     */
    inner class SourceListListener : OnItemClickListener {

        override fun onItemClick(
            arg0: AdapterView<*>, arg1: View, pos: Int,
            arg3: Long
        ) {
            try {
                if (!pm!!.isWakeupActive(sourceVals.get(pos)))
                    pm!!.activateWakeup(sourceVals.get(pos))
                else
                    pm!!.clearWakeup(sourceVals.get(pos))
                sourceList.add(sourceVals.get(pos))
            } catch (e: DeviceException) {
                android.util.Log.e(javaClass.name, "onItemClick", e)
            }

            try {
                Thread.sleep(100)
            } catch (e: InterruptedException) {
            }

            setText()
        }

    }

    companion object {

        private var timeouts: Array<String>? = null
        private var timeoutVals: Array<SuspendTimeout>? = null
        private var sources: Array<String>? = null
        private val sourceVals = ArrayList<WakeupSource>()
    }

}
