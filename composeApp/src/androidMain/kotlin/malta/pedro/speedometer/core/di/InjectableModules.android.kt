package malta.pedro.speedometer.core.di

import androidx.activity.ComponentActivity
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SharedPreferencesSettings
import malta.pedro.speedometer.features.presentation.LocationClient
import malta.pedro.speedometer.features.presentation.PermissionsManager
import malta.pedro.speedometer.location.AndroidLocationClient
import malta.pedro.speedometer.permissions.AndroidPermissionsManager
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun appModules(): Module {
    return module {
        factory<LocationClient> { AndroidLocationClient(get<ComponentActivity>()) }
        factory<PermissionsManager> { AndroidPermissionsManager(get()) }
        factory<ObservableSettings> {
            SharedPreferencesSettings(
                get<ComponentActivity>().getSharedPreferences("malta.pedro.speedometer.prefs", 0)
            )
        }
    }
}
