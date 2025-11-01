package malta.pedro.speedometer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import malta.pedro.speedometer.features.presentation.SpeedometerViewModel
import malta.pedro.speedometer.features.presentation.VintageSpeedometer
import malta.pedro.speedometer.features.presentation.styles.SpeedometerStyle
import malta.pedro.speedometer.features.presentation.styles.styles
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeedometerScreen(
    modifier: Modifier = Modifier,
    viewModel: SpeedometerViewModel = koinViewModel()
) {
    // Drawer state
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val (topAppBarColor, contentColor) = remember(viewModel.backgroundColor) {
        topAppBarColorsFor(viewModel.backgroundColor)
    }

    MaterialTheme {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(
                    modifier = Modifier.background(Color.White),
                    useMph = viewModel.useMph,
                    colors = viewModel.colors,
                    selectedStyle = viewModel.selectedStyle,
                    onStyleSelected = {
                        viewModel.selectedStyle = it
                        coroutineScope.launch { drawerState.close() }
                    },
                    onColorSelected = {
                        viewModel.backgroundColor = it
                        coroutineScope.launch { drawerState.close() }
                    },
                    onUnitToggled = {
                        viewModel.useMph = it
                        coroutineScope.launch { drawerState.close() }
                    }
                )
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { },
                        navigationIcon = {
                            IconButton(onClick = {
                                coroutineScope.launch { drawerState.open() }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = topAppBarColor,
                            titleContentColor = contentColor,
                            navigationIconContentColor = contentColor,
                            actionIconContentColor = contentColor
                        )
                    )
                }
            ) { padding ->
                LaunchedEffect(viewModel) {
                    viewModel.start()
                }

                VintageSpeedometer(
                    modifier = modifier
                        .background(viewModel.backgroundColor)
                        .fillMaxSize()
                        .padding(padding)
                        .padding(8.dp),
                    currentSpeed = viewModel.speed,
                    useMph = viewModel.useMph,
                    style = viewModel.selectedStyle,
                )
            }
        }
    }
}


@Composable
fun DrawerContent(
    modifier: Modifier,
    useMph: Boolean,
    selectedStyle: SpeedometerStyle ,
    colors: List<Color>,
    onStyleSelected: (SpeedometerStyle) -> Unit,
    onColorSelected: (Color) -> Unit,
    onUnitToggled: (Boolean) -> Unit,
) {

    Column(
        modifier = modifier
            .systemBarsPadding()
            .fillMaxHeight()
            .fillMaxWidth(0.75f)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Opções", style = MaterialTheme.typography.titleMedium)

        // Styles section
        Text("Estilos")
        styles.forEach { style ->
            TextButton(onClick = { onStyleSelected(style) }) {
                if (selectedStyle == style) {
                    Text("✓ ", color = MaterialTheme.colorScheme.primary)
                }
                Text(style.styleName)
            }
        }

        HorizontalDivider()

        // Background colors
        Text("Cor de fundo")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Cor de fundo")

                LazyVerticalGrid(
                    columns = GridCells.Fixed(6), // 6 columns for nice spacing
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp) // prevent overscroll if many colors
                ) {
                    items(colors) { color ->
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(color, CircleShape)
                                .border(1.dp, Color.DarkGray, CircleShape)
                                .clickable { onColorSelected(color) }
                        )
                    }
                }
            }
        }

        HorizontalDivider()

        // Units toggle
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Usar MPH")
            Switch(
                checked = useMph,
                onCheckedChange = onUnitToggled
            )
        }
    }
}

private fun topAppBarColorsFor(backgroundColor: Color): Pair<Color, Color> {
    return if (backgroundColor.luminance() < 0.5f) {
        Color(backgroundColor.value) to Color.White
    } else {
        Color(backgroundColor.value) to Color.Black
    }
}