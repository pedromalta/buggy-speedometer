package malta.pedro.speedometer.features.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SpeedometerViewModel(
    private val locationClient: LocationClient,
    private val permissionsManager: PermissionsManager,
) : ViewModel() {

    var speed by mutableStateOf(0f)
        private set

    fun start() {
        viewModelScope.launch {
            val granted = permissionsManager.requestLocationPermission()
            if (granted) {
                locationClient.startLocationUpdates { location ->
                    // speed in meters/second, location.speed can be 0.0 if unavailable
                    speed = location.speed ?: 0f
                }
            } else {
                speed = 0f
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationClient.stopLocationUpdates()
    }
}

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val speed: Float? = null,
)

interface LocationClient {
    suspend fun startLocationUpdates(onUpdate: (LocationData) -> Unit)
    fun stopLocationUpdates()
}

interface PermissionsManager {
    suspend fun requestLocationPermission(): Boolean
}