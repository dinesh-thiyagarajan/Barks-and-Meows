package splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptions
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.ic_app_logo
import barksandmeows.composeapp.generated.resources.splash_screen_img_description
import common.utils.getAppVersion
import navigation.AppRouteActions
import navigation.NavRouter
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun SplashScreen(splashViewModel: SplashViewModel = koinViewModel()) {
    val splashUiState = splashViewModel.splashUiState.collectAsState()
    var animationStarted by remember { mutableStateOf(false) }

    LaunchedEffect(splashViewModel) {
        animationStarted = true
        splashViewModel.isLoggedIn()
    }

    LaunchedEffect(splashUiState.value) {
        // NavOptions to remove splash screen from backstack
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

    val alphaAnim by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(durationMillis = 800)
    )

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.alpha(alphaAnim),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // --- App Logo ---
                Surface(
                    modifier = Modifier.size(130.dp),
                    shape = RoundedCornerShape(32.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                    tonalElevation = 4.dp,
                    shadowElevation = 12.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(Res.drawable.ic_app_logo),
                            contentDescription = stringResource(Res.string.splash_screen_img_description),
                            modifier = Modifier.size(96.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- App Name ---
                Text(
                    text = "Barks & Meows",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Your pet companion",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(40.dp))

                // --- Loading Indicator ---
                CircularProgressIndicator(
                    modifier = Modifier.size(28.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.5.dp
                )
            }

            // --- Version at bottom ---
            Text(
                text = "v${getAppVersion()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
                    .alpha(alphaAnim)
            )
        }
    }
}
