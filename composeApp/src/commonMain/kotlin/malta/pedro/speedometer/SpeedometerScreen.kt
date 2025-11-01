package malta.pedro.speedometer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import malta.pedro.speedometer.features.presentation.SpeedometerViewModel
import malta.pedro.speedometer.features.presentation.VintageSpeedometer
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SpeedometerScreen(
    modifier: Modifier = Modifier,
    viewModel: SpeedometerViewModel = koinViewModel()
) {
    MaterialTheme {
        LaunchedEffect(viewModel) {
            viewModel.start()
        }
        VintageSpeedometer(
            modifier = modifier
                .background(Color(0xFFf3b802))
                .systemBarsPadding()
                .fillMaxSize()
                .padding(8.dp),
            currentSpeed = viewModel.speed
        )
    }
}
