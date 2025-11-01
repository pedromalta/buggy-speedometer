package malta.pedro.speedometer.permissions

import malta.pedro.speedometer.features.presentation.PermissionsManager
import platform.CoreLocation.CLLocationManager

class IOSPermissionsManager : PermissionsManager {
    override suspend fun requestLocationPermission(): Boolean {
        val manager = CLLocationManager()
        manager.requestWhenInUseAuthorization()
        return true // You can refine this with delegate callbacks if you need
    }
}