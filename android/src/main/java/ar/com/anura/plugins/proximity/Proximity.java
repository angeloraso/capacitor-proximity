package ar.com.anura.plugins.proximity;

import static android.hardware.Sensor.TYPE_PROXIMITY;
import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class Proximity implements SensorEventListener{

    final int DISABLED = 0;
    final int ENABLED = 1;
    final int NEAR = 1;
    final int FAR = 0;
    final String TAG = "Proximity";

    final long TIMEOUT = 30000;
    private SensorManager sensorManager;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;

    Sensor mSensor;
    int status;
    int proximity;
    long timeStamp;
    long lastAccessTime;

    Proximity(final AppCompatActivity activity) {
        proximity = 0;
        timeStamp = 0;
        status = DISABLED;
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        powerManager = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
        wakeLock = null;
    }

    public void enable() {
        if (status == DISABLED) {
            mSensor = sensorManager.getDefaultSensor(TYPE_PROXIMITY);
            sensorManager.registerListener((SensorEventListener) this, mSensor, SENSOR_DELAY_NORMAL);
            lastAccessTime = System.currentTimeMillis();
        }

        Log.d(TAG, "XXX enable proximity sensor");
        if(wakeLock != null) {
            wakeLock.release();
        }
        wakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, this.toString());
        wakeLock.acquire();
        status = ENABLED;
    }

    public void disable() {
        if (status == ENABLED) {
            sensorManager.unregisterListener(this);
        }

        if(wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }

        status = DISABLED;
    }

    public void onSensorChanged(SensorEvent event) {

        int proximity;

        Log.d(TAG, "XXX onSensorChanged sensor length -> " + event.values.length);

        if(event.values.length < 1) {
            Log.e(TAG, "XXX Proximity SensorEvent contained no values");
            return;
        }

        for(int i = 0; i < event.values.length; i++) {
            Log.d(TAG, "XXX onSensorChanged [" + i + "]-> " + event.values[i]);
        }

        if (event.values[0] == 0) {
            proximity = NEAR;
        } else {
            proximity = FAR;
        }

        // Save proximity
        timeStamp = System.currentTimeMillis();
        this.proximity = proximity;

        // If proximity hasn't been read for TIMEOUT time, then turn off sensor to save power
        if ((this.timeStamp - this.lastAccessTime) > this.TIMEOUT) {
            disable();
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        return;
    }
}
