package malta.pedro.speedometer.features.persistence

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.SuspendSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.coroutines.toSuspendSettings
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalSettingsApi::class)
class SettingsRepository(settings: ObservableSettings) {

    private val suspendSettings: SuspendSettings = settings.toSuspendSettings()
    private val flowSettings: FlowSettings = settings.toFlowSettings()

    val selectedStyleFlow: Flow<String> =
        flowSettings.getStringFlow("selectedStyle", defaultValue = "vwClassic40sStyle")

    val backgroundColorFlow: Flow<Int> =
        flowSettings.getIntFlow("backgroundColor", defaultValue = 0xFFf3b802.toInt())

    val useMphFlow: Flow<Boolean> =
        flowSettings.getBooleanFlow("useMph", defaultValue = false)

    suspend fun setSelectedStyle(style: String) {
        suspendSettings.putString("selectedStyle", style)
    }

    suspend fun setBackgroundColor(color: Int) {
        suspendSettings.putInt("backgroundColor", color)
    }

    suspend fun setUseMph(value: Boolean) {
        suspendSettings.putBoolean("useMph", value)
    }
}