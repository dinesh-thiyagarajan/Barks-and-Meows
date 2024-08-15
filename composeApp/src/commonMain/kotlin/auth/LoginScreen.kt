package auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import com.dineshworkspace.auth.composables.LoginComposable
import com.dineshworkspace.auth.viewModels.AuthUiState
import com.dineshworkspace.auth.viewModels.AuthViewModel
import com.dineshworkspace.uicomponents.composables.loading.LoadingComposable
import navigation.AppRouteActions
import navigation.NavRouter
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun LoginScreen(authViewModel: AuthViewModel = koinViewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val authUiState = authViewModel.authUiState.collectAsState()
    when (authUiState.value) {
        is AuthUiState.LoggedIn -> {
            NavRouter.navigate(AppRouteActions.HomeScreen.route)
        }

        is AuthUiState.NotLoggedIn -> {
            LoginComposable(coroutineScope = coroutineScope, authViewModel = authViewModel)
        }

        is AuthUiState.LoginInProgress -> {
            LoadingComposable()
        }

        is AuthUiState.Error -> {}
    }
}

