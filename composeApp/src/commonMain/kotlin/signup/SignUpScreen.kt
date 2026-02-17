package signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavOptions
import auth.GoogleSignInHandler
import com.app.auth.composables.SignUpComposable
import com.app.auth.viewModels.AuthUiState
import com.app.auth.viewModels.AuthViewModel
import com.app.auth.viewModels.SignUpUiState
import com.app.auth.viewModels.SignUpViewModel
import com.app.uicomponents.composables.loading.LoadingComposable
import navigation.AppRouteActions
import navigation.NavRouter
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val signUpUiState = signUpViewModel.signUpUiState.collectAsState()
    val authUiState = authViewModel.authUiState.collectAsState()
    val googleSignInHandler = GoogleSignInHandler(authViewModel)

    // Handle Google Sign-In success
    when (authUiState.value) {
        is AuthUiState.LoggedIn -> {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(AppRouteActions.LoginScreen.route, inclusive = true)
                .build()
            NavRouter.navigate(AppRouteActions.HomeScreen.route, navOptions)
        }
        is AuthUiState.LoginInProgress -> {
            LoadingComposable()
            return
        }
        else -> {}
    }

    when (val state = signUpUiState.value) {
        is SignUpUiState.Initial -> {
            SignUpComposable(
                coroutineScope = coroutineScope,
                signUpViewModel = signUpViewModel,
                onGoogleSignInClick = googleSignInHandler
            )
        }

        is SignUpUiState.SignUpInProgress -> {
            LoadingComposable()
        }

        is SignUpUiState.SignUpSuccess -> {
            // Remove signup and login screens from backstack when navigating to home
            val navOptions = NavOptions.Builder()
                .setPopUpTo(AppRouteActions.LoginScreen.route, inclusive = true)
                .build()
            NavRouter.navigate(AppRouteActions.HomeScreen.route, navOptions)
        }

        is SignUpUiState.Error -> {
            SignUpComposable(
                coroutineScope = coroutineScope,
                signUpViewModel = signUpViewModel,
                errorMessage = state.message,
                onGoogleSignInClick = googleSignInHandler
            )
        }
    }
}
