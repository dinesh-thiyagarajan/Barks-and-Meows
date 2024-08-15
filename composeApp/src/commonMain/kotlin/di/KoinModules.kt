package di

import com.dineshworkspace.auth.di.authModule
import com.dineshworkspace.di.petModule
import com.dineshworkspace.env.Config
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import splash.SplashViewModel


val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val envModule = module {
    single<String>(named("base_env")) { Config.BASE_ENV }
    single<String>(named("pets_collection")) { Config.PETS_COLLECTION }
}

fun appModule() = listOf(authModule, envModule, petModule, splashModule)