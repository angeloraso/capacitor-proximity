import UIKit

@objc public class Proximity: NSObject {
    @objc public func enable() -> Bool {
        UIDevice.current.isProximityMonitoringEnabled = true
        return UIDevice.current.isProximityMonitoringEnabled
    }

    @objc public func disable() {
        UIDevice.current.isProximityMonitoringEnabled = false
    }

    deinit {
        if Thread.isMainThread {
            UIDevice.current.isProximityMonitoringEnabled = false
        } else {
            DispatchQueue.main.async {
                UIDevice.current.isProximityMonitoringEnabled = false
            }
        }
    }
}
