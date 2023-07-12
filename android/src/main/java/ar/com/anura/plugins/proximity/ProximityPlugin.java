package ar.com.anura.plugins.proximity;

import androidx.appcompat.app.AppCompatActivity;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "Proximity")
public class ProximityPlugin extends Plugin {

    private Proximity proximity;

    public void load() {
        AppCompatActivity activity = getActivity();
        proximity = new Proximity(activity);
    }

    @PluginMethod
    public void enable(PluginCall call) {
        if (getActivity().isFinishing()) {
            call.reject("Proximity plugin error: App is finishing");
            return;
        }

        proximity.enable();

        call.resolve();
    }

    @PluginMethod
    public void disable(PluginCall call) {
        if (getActivity().isFinishing()) {
            call.reject("Proximity plugin error: App is finishing");
            return;
        }

        proximity.disable();

        call.resolve();
    }

    /**
     * Called when the activity will be destroyed.
     */
    @Override
    public void handleOnDestroy() {
        proximity.disable();
    }

}
