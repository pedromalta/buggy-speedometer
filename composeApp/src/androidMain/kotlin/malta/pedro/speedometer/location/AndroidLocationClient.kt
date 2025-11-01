package malta.pedro.speedometer.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import malta.pedro.speedometer.features.presentation.LocationClient
import malta.pedro.speedometer.features.presentation.LocationData

class AndroidLocationClient(private val context: Context) : LocationClient {
    private var fusedClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private var callback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    override suspend fun startLocationUpdates(onUpdate: (LocationData) -> Unit) {
        if (callback != null) return
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L).build()
        callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let {
                    onUpdate(LocationData(it.latitude, it.longitude, it.speed))
                }
            }
        }
        callback?.let {
            fusedClient.requestLocationUpdates(request, it, context.mainLooper)
        }
    }

    override fun stopLocationUpdates() {
        callback?.let { fusedClient.removeLocationUpdates(it) }
        callback = null
    }
}