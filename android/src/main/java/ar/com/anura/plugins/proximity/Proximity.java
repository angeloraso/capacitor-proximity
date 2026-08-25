package ar.com.anura.plugins.proximity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;

public class Proximity implements SensorEventListener {

    private final PowerManager powerManager;
    private final SensorManager sensorManager;
    private final Sensor proximitySensor;
    private final String wakeLockTag;
    private PowerManager.WakeLock wakeLock;
    private boolean enabled;

    Proximity(final Context context) {
        powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        wakeLockTag = context.getPackageName() + ":proximity";
    }

    public synchronized void enable() {
        if (enabled) {
            return;
        }

        if (
            proximitySensor == null ||
            !powerManager.isWakeLockLevelSupported(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK)
        ) {
            throw new IllegalStateException("The proximity sensor is not available on this device");
        }

        enabled = true;
        if (!sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL)) {
            enabled = false;
            throw new IllegalStateException("Unable to listen to the proximity sensor");
        }
    }

    public synchronized void disable() {
        boolean wasEnabled = enabled;
        enabled = false;
        if (wasEnabled && proximitySensor != null) {
            sensorManager.unregisterListener(this, proximitySensor);
        }
        releaseWakeLock();
    }

    @Override
    public synchronized void onSensorChanged(SensorEvent event) {
        if (!enabled || event.sensor.getType() != Sensor.TYPE_PROXIMITY || event.values.length == 0) {
            return;
        }

        boolean isNear = event.values[0] < proximitySensor.getMaximumRange();
        if (isNear) {
            acquireWakeLock();
        } else {
            releaseWakeLock();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private void acquireWakeLock() {
        if (wakeLock != null && wakeLock.isHeld()) {
            return;
        }

        PowerManager.WakeLock newWakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, wakeLockTag);
        newWakeLock.setReferenceCounted(false);
        newWakeLock.acquire();
        wakeLock = newWakeLock;
    }

    private void releaseWakeLock() {
        PowerManager.WakeLock currentWakeLock = wakeLock;
        wakeLock = null;
        if (currentWakeLock != null && currentWakeLock.isHeld()) {
            currentWakeLock.release();
        }
    }
}
