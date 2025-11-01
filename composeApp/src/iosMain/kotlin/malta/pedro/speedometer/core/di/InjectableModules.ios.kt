package malta.pedro.speedometer.core.di

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import malta.pedro.speedometer.features.presentation.LocationClient
import malta.pedro.speedometer.features.presentation.PermissionsManager
import malta.pedro.speedometer.location.IOSLocationClient
import malta.pedro.speedometer.permissions.IOSPermissionsManager
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

actual fun appModules(): Module {
    return module {
        factory<LocationClient> { IOSLocationClient() }
        factory<PermissionsManager> { IOSPermissionsManager() }
        factory<ObservableSettings> {
            NSUserDefaultsSettings(
                NSUserDefaults.standardUserDefaults
            )
        }
    }
}