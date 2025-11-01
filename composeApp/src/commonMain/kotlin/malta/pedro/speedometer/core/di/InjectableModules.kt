package malta.pedro.speedometer.core.di

import malta.pedro.speedometer.features.presentation.SpeedometerViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun appModules(): Module

fun commonModules() = module {
    factory {
        SpeedometerViewModel(
            locationClient = get(),
            permissionsManager = get()
        )
    }
}

val koinModules = appModules() + commonModules()

fun initKoin() {
    startKoin {
        modules(koinModules)
    }
}
