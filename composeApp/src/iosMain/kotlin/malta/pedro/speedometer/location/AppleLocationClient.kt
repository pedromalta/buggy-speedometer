package malta.pedro.speedometer.location

import kotlinx.cinterop.*
import malta.pedro.speedometer.features.presentation.LocationClient
import malta.pedro.speedometer.features.presentation.LocationData
import platform.CoreLocation.*
import platform.darwin.NSObject

class IOSLocationClient : LocationClient {
    private val manager = CLLocationManager()
    private val delegate = LocationDelegate()

    override suspend fun startLocationUpdates(onUpdate: (LocationData) -> Unit) {
        delegate.onUpdate = onUpdate
        manager.delegate = delegate
        manager.desiredAccuracy = kCLLocationAccuracyBest
        manager.startUpdatingLocation()
    }

    override fun stopLocationUpdates() {
        manager.stopUpdatingLocation()
        delegate.onUpdate = null
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private class LocationDelegate : NSObject(), CLLocationManagerDelegateProtocol {
    var onUpdate: ((LocationData) -> Unit)? = null

    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
        val loc = didUpdateLocations.lastOrNull() as? CLLocation ?: return
        val locationData = loc.coordinate.useContents {
            LocationData(
                latitude = latitude,
                longitude = longitude,
                speed = loc.speed.toFloat()
            )
        }
        onUpdate?.invoke(locationData)
    }
}