package di

import com.dineshworkspace.auth.di.authModule
import com.dineshworkspace.di.petModule
import env.Config
import com.dineshworkspace.vaccine.di.vaccineModule
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import splash.SplashViewModel


val splashModule = module {
    viewModel { SplashViewModel(isLoggedInUseCase = get()) }
}

val envModule = module {
    single<String>(named("base_env")) { Config.BASE_ENV }
    single<String>(named("pets_collection")) { Config.PETS_COLLECTION }
    single<String>(named("vaccine_notes_collection")) { Config.VACCINE_NOTES_COLLECTION }
}

fun appModule() = listOf(authModule, envModule, petModule, splashModule, vaccineModule)