package auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavOptions
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
fun LoginScreen(authViewModel: AuthViewModel = koinViewModel(),
                onSignUpClicked: () -> Unit = {}) {
    val coroutineScope = rememberCoroutineScope()
    val authUiState = authViewModel.authUiState.collectAsState()
    val googleSignInHandler = GoogleSignInHandler(authViewModel)

    when (val state = authUiState.value) {
        is AuthUiState.LoggedIn -> {
            // Remove login screen from backstack when navigating to home
            val navOptions = NavOptions.Builder()
                .setPopUpTo(AppRouteActions.LoginScreen.route, inclusive = true)
                .build()
            NavRouter.navigate(AppRouteActions.HomeScreen.route, navOptions)
        }

        is AuthUiState.NotLoggedIn -> {
            LoginComposable(
                coroutineScope = coroutineScope,
                authViewModel = authViewModel,
                onGoogleSignInClick = googleSignInHandler,
                onSignUpClicked = onSignUpClicked
            )
        }

        is AuthUiState.LoginInProgress -> {
            LoadingComposable()
        }

        is AuthUiState.Error -> {
            LoginComposable(
                coroutineScope = coroutineScope,
                authViewModel = authViewModel,
                errorMessage = state.message,
                onGoogleSignInClick = googleSignInHandler,
                onSignUpClicked = onSignUpClicked
            )
        }
    }
}

