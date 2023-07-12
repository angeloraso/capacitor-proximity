import Foundation

@objc public class Proximity: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
