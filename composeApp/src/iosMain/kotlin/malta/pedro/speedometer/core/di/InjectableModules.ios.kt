package malta.pedro.speedometer.core.di

import malta.pedro.speedometer.features.presentation.LocationClient
import malta.pedro.speedometer.features.presentation.PermissionsManager
import malta.pedro.speedometer.location.IOSLocationClient
import malta.pedro.speedometer.permissions.IOSPermissionsManager
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun appModules(): Module {
    return module {
        single<LocationClient> { IOSLocationClient() }
        single<PermissionsManager> { IOSPermissionsManager() }
    }
}