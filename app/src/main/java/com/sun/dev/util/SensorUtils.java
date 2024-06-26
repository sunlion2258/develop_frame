package com.sun.dev.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.List;

/**
 * Created by fengwj on 2024/6/26.
 */
public class SensorUtils {
    public static boolean hasOrientationSensor(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        boolean hasOrientationSensor = sensorManager.getSensorList(Sensor.TYPE_ALL).size() > 0;
        return hasOrientationSensor;
    }
}
