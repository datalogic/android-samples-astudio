package com.example.decodeintent


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast

/**
 * Log and Toast the received action, categories, barcode data and symboloty type.
 * MainActivity.isReading will be set to false.
 */
class IntentStartActivity : Activity() {
    private var textMsg: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intent_start)

        // Get the Intent that created this activity.
        val intent = intent
        val action = intent.action

        showMessage("Started IntentStartActivity")
        Log.d(javaClass.name, "Started activity with Intent")

        val category_all = intent.categories
        val category = StringBuilder()
        for (s in category_all) {
            category.append(s)
        }

        // Which Barcode type?
        val type = intent.getStringExtra(IntentWedgeSample.EXTRA_TYPE)
        // Get the Barcode value
        val data = intent.getStringExtra(IntentWedgeSample.EXTRA_DATA_STRING)

        textMsg = findViewById(R.id.textResult) as TextView
        textMsg!!.append(
            "Action: " + action + "\n"
                    + "Category: " + category.toString() + "\n"
                    + "Type: " + type + "\n"
                    + "Data: " + data
        )
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
