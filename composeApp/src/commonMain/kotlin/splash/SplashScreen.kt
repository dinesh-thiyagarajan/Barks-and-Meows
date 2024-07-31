package splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.ic_app_logo
import barksandmeows.composeapp.generated.resources.splash_screen_img_description
import navigation.NavRouter
import navigation.Router
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun SplashScreen(splashViewModel: SplashViewModel = koinViewModel()) {
    val splashUiState = splashViewModel.splashUiState.collectAsState()
    LaunchedEffect(splashViewModel) {
        splashViewModel.isLoggedIn()
    }
    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize().padding(
                top = it.calculateTopPadding() + 10.dp,
                bottom = it.calculateBottomPadding() + 25.dp,
                start = it.calculateStartPadding(LocalLayoutDirection.current) + 25.dp,
                end = it.calculateEndPadding(LocalLayoutDirection.current) + 25.dp,
            ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_app_logo),
                contentDescription = stringResource(Res.string.splash_screen_img_description),
                modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally),
            )
        }
    }
    when (splashUiState.value) {
        is SplashUiState.LoggedIn -> {

        }

        is SplashUiState.NotLoggedIn -> {
            NavRouter.navigate(Router.LoginRouter.route)
        }

        is SplashUiState.FetchingLoginStatus -> {}
    }
}