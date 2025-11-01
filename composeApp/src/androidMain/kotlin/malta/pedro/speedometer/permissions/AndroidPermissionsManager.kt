package malta.pedro.speedometer.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import malta.pedro.speedometer.features.presentation.PermissionsManager
import kotlin.coroutines.resume

class AndroidPermissionsManager(private val context: Context) : PermissionsManager {
    override suspend fun requestLocationPermission(): Boolean {
        return suspendCancellableCoroutine { cont ->
            val granted = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            if (granted){
                cont.resume(true)
            }
            else {
                cont.resume(false)
            }
        }
    }
}