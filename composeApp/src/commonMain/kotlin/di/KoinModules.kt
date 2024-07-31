package di

import auth.AuthViewModel
import com.dineshworkspace.auth.di.authModule
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import splash.SplashViewModel


val provideViewModels = module {
    viewModel { AuthViewModel(get()) }
    viewModel { SplashViewModel() }
}


fun appModule() = listOf(authModule, provideViewModels)