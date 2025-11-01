package malta.pedro.speedometer.features.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import malta.pedro.speedometer.features.persistence.SettingsRepository
import malta.pedro.speedometer.features.presentation.styles.SpeedometerStyle
import malta.pedro.speedometer.features.presentation.styles.styles
import malta.pedro.speedometer.features.presentation.styles.vwClassic40sStyle

class SpeedometerViewModel(
    private val locationClient: LocationClient,
    private val permissionsManager: PermissionsManager,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    var speed by mutableStateOf(0f)
        private set

    var selectedStyle by mutableStateOf(vwClassic40sStyle)
        private set

    var backgroundColor by mutableStateOf(Color(0xFFf3b802))
        private set

    var useMph by mutableStateOf(false)
        private set

    val colors = listOf(
        Color(0xFFf3b802), // Sahara Yellow
        Color(0xFFFFD700), // Sunshine Yellow
        Color(0xFFFFA500), // Tangerine Orange
        Color(0xFFFF5722), // Sunset Orange
        Color(0xFFE91E63), // Ruby Red
        Color(0xFFF44336), // Tornado Red
        Color(0xFFFFC0CB), // Coral Pink
        Color(0xFF8E24AA), // Royal Purple
        Color(0xFF3F51B5), // Deep Ocean Blue
        Color(0xFF1976D2), // Marina Blue
        Color(0xFF00BCD4), // Capri Turquoise
        Color(0xFF009688), // Sea Green
        Color(0xFF4CAF50), // Emerald Green
        Color(0xFF8BC34A), // Lime Green
        Color(0xFFCDDC39), // Pistachio
        Color(0xFFFFEB3B), // Lemon Yellow
        Color(0xFF795548), // Mocha Brown
        Color(0xFFD7CCC8), // Savanna Beige
        Color(0xFF9E9E9E), // Ash Gray
        Color(0xFFB0BEC5), // Silver Blue
        Color(0xFFFFFFFF), // Pearl White
        Color(0xFFF5F5DC), // Cream
        Color(0xFF212121), // Jet Black
        Color(0xFF607D8B)  // Slate Blue Gray
    )

    init {
        viewModelScope.launch {
            settingsRepository.selectedStyleFlow.collect { savedStyleName ->
                selectedStyle = styles.find { style -> style.styleName == savedStyleName }  ?: vwClassic40sStyle
            }
        }
        viewModelScope.launch {
            settingsRepository.backgroundColorFlow.collect { savedBackgroundColor ->
                backgroundColor = Color(savedBackgroundColor)
            }
        }
        viewModelScope.launch {
            settingsRepository.useMphFlow.collect { useMph = it }
        }
    }

    fun changeStyle(style: SpeedometerStyle) {
        selectedStyle = style
        viewModelScope.launch {
            settingsRepository.setSelectedStyle(style.styleName)
        }
    }

    fun changeBackgroundColor(color: Color) {
        backgroundColor = color
        viewModelScope.launch {
            settingsRepository.setBackgroundColor(color.toArgb())
        }
    }

    fun toggleUnit() {
        useMph = !useMph
        viewModelScope.launch {
            settingsRepository.setUseMph(useMph)
        }
    }

    fun start() {
        viewModelScope.launch {
            val granted = permissionsManager.requestLocationPermission()
            if (granted) {
                locationClient.startLocationUpdates { location ->
                    location.speed?.let {
                        speed = if (useMph) {
                            it * 2.23694f
                        } else {
                            it * 3.6f
                        }
                    }
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