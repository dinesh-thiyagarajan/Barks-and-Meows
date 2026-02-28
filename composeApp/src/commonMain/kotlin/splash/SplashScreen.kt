package splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavOptions
import navigation.AppRouteActions
import navigation.NavRouter
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun SplashScreen(splashViewModel: SplashViewModel = koinViewModel()) {
    val splashUiState = splashViewModel.splashUiState.collectAsState()

    LaunchedEffect(splashViewModel) {
        splashViewModel.isLoggedIn()
    }

    LaunchedEffect(splashUiState.value) {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(AppRouteActions.SplashScreen.route, inclusive = true)
            .build()

        when (splashUiState.value) {
            is SplashUiState.LoggedIn -> {
                NavRouter.navigate(AppRouteActions.HomeScreen.route, navOptions)
            }
            is SplashUiState.NotLoggedIn -> {
                NavRouter.navigate(AppRouteActions.LoginScreen.route, navOptions)
            }
            is SplashUiState.FetchingLoginStatus -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize())
}
