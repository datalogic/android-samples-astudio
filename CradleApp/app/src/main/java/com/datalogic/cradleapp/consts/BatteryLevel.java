package com.datalogic.cradleapp.consts;

import android.content.Context;

import com.datalogic.cradleapp.R;

public class BatteryLevel {

    public static int LOW = 30;
    public static int HIGH = 65;

    public static int getColorInt(int batteryPercentage, Context context)
    {
        if (batteryPercentage < LOW)
            return context.getResources().getColor(R.color.batteryLow);
        else if (batteryPercentage > HIGH)
            return context.getResources().getColor(R.color.batteryHigh);
        else
            return context.getResources().getColor(R.color.batteryMedium);
    }

    public static int getTextColorInt(int batteryPercentage, Context context)
    {
        if (batteryPercentage < LOW)
            return context.getResources().getColor(R.color.colorPrimary);  // white on red
        else if (batteryPercentage > HIGH)
            return context.getResources().getColor(R.color.colorPrimary);    // white on green
        else
            return context.getResources().getColor(R.color.colorSecondary);  // black on yellow
    }
}
