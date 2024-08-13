package di

import auth.AuthViewModel
import com.dineshworkspace.auth.di.authModule
import com.dineshworkspace.database.di.databaseModule
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pet.add.viewModels.PetViewModel
import pet.details.viewModels.PetDetailsViewModel
import splash.SplashViewModel


val provideViewModels = module {
    viewModel { AuthViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { PetViewModel(get(), get(), get()) }
    viewModel { PetDetailsViewModel(get()) }
}


fun appModule() = listOf(authModule, databaseModule, provideViewModels)