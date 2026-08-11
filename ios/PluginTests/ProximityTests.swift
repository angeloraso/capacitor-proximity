import XCTest
import UIKit
@testable import Plugin

class ProximityTests: XCTestCase {
    override func tearDown() {
        UIDevice.current.isProximityMonitoringEnabled = false
        super.tearDown()
    }

    func testDisableIsIdempotent() {
        let implementation = Proximity()

        implementation.disable()
        implementation.disable()

        XCTAssertFalse(UIDevice.current.isProximityMonitoringEnabled)
    }
}
