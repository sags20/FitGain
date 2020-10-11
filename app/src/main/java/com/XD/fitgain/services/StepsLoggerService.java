package com.XD.fitgain.services;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class StepsLoggerService extends Service implements SensorEventListener {
    private static final String DEBUG_TAG = "StepsLoggerService";

    private SensorManager sensorManager = null;
    private Sensor sensor = null;

    private double MagnitudePrevious = 0;
    private Integer stepCount = 0;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);


        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event != null) {
            float x_acceleration = event.values[0];
            float y_acceleration = event.values[1];
            float z_acceleration = event.values[2];

            double Magnitude = Math.sqrt(x_acceleration * x_acceleration + y_acceleration * y_acceleration + z_acceleration * z_acceleration);
            double MagnitudeDelta = Magnitude - MagnitudePrevious;

            MagnitudePrevious = Magnitude;

            if (MagnitudeDelta > 5) {
                stepCount++;
            }

            saveSteps(stepCount);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void saveSteps(Integer stepCount) {

        Intent intent = new Intent();
        intent.setAction("STEPS_CHANGED");
        intent.putExtra("steps", stepCount);
        sendBroadcast(intent);
    }
}
