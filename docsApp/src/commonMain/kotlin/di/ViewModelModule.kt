package di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import screens.main.MainViewModel

internal val viewModelModule = module {
    viewModelOf(::MainViewModel)
}