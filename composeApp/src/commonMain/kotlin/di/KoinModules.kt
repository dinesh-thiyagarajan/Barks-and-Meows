package di

import com.dineshworkspace.auth.di.authModule
import com.dineshworkspace.di.petModule
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import splash.SplashViewModel


val splashModule = module {
    viewModel { SplashViewModel(get()) }
}


fun appModule() = listOf(authModule, petModule, splashModule)