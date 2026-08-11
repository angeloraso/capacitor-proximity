package ar.com.anura.plugins.proximity;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "Proximity")
public class ProximityPlugin extends Plugin {

    private Proximity proximity;

    @Override
    public void load() {
        proximity = new Proximity(getContext());
    }

    @PluginMethod
    public void enable(PluginCall call) {
        try {
            proximity.enable();
            call.resolve();
        } catch (IllegalStateException exception) {
            call.unavailable(exception.getMessage());
        } catch (RuntimeException exception) {
            call.reject("Unable to enable the proximity sensor", "ENABLE_FAILED", exception);
        }
    }

    @PluginMethod
    public void disable(PluginCall call) {
        try {
            proximity.disable();
            call.resolve();
        } catch (RuntimeException exception) {
            call.reject("Unable to disable the proximity sensor", "DISABLE_FAILED", exception);
        }
    }

    /**
     * Called when the activity will be destroyed.
     */
    @Override
    public void handleOnDestroy() {
        try {
            if (proximity != null) {
                proximity.disable();
            }
        } finally {
            super.handleOnDestroy();
        }
    }
}
