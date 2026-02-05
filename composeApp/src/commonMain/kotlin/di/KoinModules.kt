package di

import com.app.auth.di.authModule
import com.app.di.petModule
import com.app.reminder.di.reminderModule
import com.app.vaccine.di.vaccineModule
import env.Config
import org.koin.core.module.dsl.viewModel
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

expect fun platformModules(): List<org.koin.core.module.Module>

fun appModule() = listOf(authModule, envModule, petModule, splashModule, vaccineModule, reminderModule) + platformModules()
