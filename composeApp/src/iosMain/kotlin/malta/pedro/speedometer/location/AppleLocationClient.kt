package malta.pedro.speedometer.location

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import malta.pedro.speedometer.features.presentation.LocationClient
import malta.pedro.speedometer.features.presentation.LocationData
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.darwin.NSObject

class IOSLocationClient : NSObject(), LocationClient, CLLocationManagerDelegateProtocol {
    private val manager = CLLocationManager()
    private var onUpdate: ((LocationData) -> Unit)? = null

    override suspend fun startLocationUpdates(onUpdate: (LocationData) -> Unit) {
        this.onUpdate = onUpdate
        manager.delegate = this
        manager.desiredAccuracy = kCLLocationAccuracyBest
        manager.startUpdatingLocation()
    }

    override fun stopLocationUpdates() {
        manager.stopUpdatingLocation()
        onUpdate = null
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
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