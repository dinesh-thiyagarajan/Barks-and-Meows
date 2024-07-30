import com.dineshworkspace.auth.authModule
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module


val provideViewModels = module {
    viewModelOf(::AuthViewModel)
}

fun appModule() = listOf(authModule, provideViewModels)