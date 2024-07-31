import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import auth.LoginScreen
import common.composables.BarksAndMeowsAppBar
import di.appModule
import navigation.BarksAndMeowsRouter
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import splash.SplashScreen
import theme.BarksAndMeowsTheme

@Composable
@Preview
fun BarksAndMeowsApp() {
    KoinApplication(application = {
        modules(appModule())
    }) {
        BarksAndMeowsApp(navController = rememberNavController())
    }
}

@Composable
private fun BarksAndMeowsApp(navController: NavHostController = rememberNavController()) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = BarksAndMeowsRouter.valueOf(
        backStackEntry?.destination?.route ?: BarksAndMeowsRouter.SplashScreen.name
    )
    BarksAndMeowsTheme {
        Scaffold(
            topBar = {
                BarksAndMeowsAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() }
                )
            }
        ) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = BarksAndMeowsRouter.SplashScreen.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                composable(route = BarksAndMeowsRouter.SplashScreen.name) {
                    SplashScreen()
                }

                composable(route = BarksAndMeowsRouter.LoginScreen.name) {
                    LoginScreen()
                }
            }
        }
    }
}