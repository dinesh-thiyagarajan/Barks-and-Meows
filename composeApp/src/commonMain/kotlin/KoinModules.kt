import auth.AuthViewModel
import com.dineshworkspace.auth.di.authModule
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module


val provideViewModels = module {
    viewModelOf(::AuthViewModel)
}

fun appModule() = listOf(authModule, provideViewModels)