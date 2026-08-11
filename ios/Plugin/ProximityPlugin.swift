import Foundation
import Capacitor

@objc(ProximityPlugin)
public class ProximityPlugin: CAPPlugin {
    private let implementation = Proximity()

    @objc func enable(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
            guard self.implementation.enable() else {
                call.reject("The proximity sensor is not available on this device", "UNAVAILABLE")
                return
            }

            call.resolve()
        }
    }

    @objc func disable(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
            self.implementation.disable()
            call.resolve()
        }
    }
}
