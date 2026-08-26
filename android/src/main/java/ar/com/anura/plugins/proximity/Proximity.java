package ar.com.anura.plugins.proximity;

import android.content.Context;
import android.os.PowerManager;

public class Proximity {

    private final PowerManager powerManager;
    private final String wakeLockTag;
    private PowerManager.WakeLock wakeLock;
    private boolean enabled;

    Proximity(final Context context) {
        powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLockTag = context.getPackageName() + ":proximity";
    }

    public synchronized void enable() {
        if (enabled) {
            return;
        }

        if (!powerManager.isWakeLockLevelSupported(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK)) {
            throw new IllegalStateException("The proximity sensor is not available on this device");
        }

        acquireWakeLock();
        enabled = true;
    }

    public synchronized void disable() {
        enabled = false;
        releaseWakeLock();
    }

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
