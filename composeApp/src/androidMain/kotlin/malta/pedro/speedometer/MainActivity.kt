package malta.pedro.speedometer

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.compose.ui.keepScreenOn
import malta.pedro.speedometer.core.di.koinModules
import malta.pedro.speedometer.features.presentation.SpeedometerViewModel
import malta.pedro.speedometer.presentation.PermissionHandler
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module
import org.koin.mp.KoinPlatformTools


class MainActivity : ComponentActivity() {

    private val viewModel: SpeedometerViewModel by viewModel()
    private val activityModule by lazy {
        module {
            single<ComponentActivity> { this@MainActivity }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT,
            )
        )
        super.onCreate(savedInstanceState)

        loadKoinModules(activityModule)

        setContent {
            PermissionHandler { viewModel.start() }
            SpeedometerScreen(
                modifier = Modifier
                    .keepScreenOn(),
                viewModel = viewModel,
            )
        }
    }

    override fun onDestroy() {
        unloadKoinModules(activityModule)
        super.onDestroy()

    }
}
