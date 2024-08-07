import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import auth.LoginScreen
import common.composables.BarksAndMeowsAppBar
import di.appModule
import home.HomeScreen
import navigation.BarksAndMeowsRouter
import navigation.NavRouter
import navigation.doNotShowTopAppBar
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
    NavRouter.setNavController(navController = navController)
    val coroutineScope = rememberCoroutineScope()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = BarksAndMeowsRouter.valueOf(
        backStackEntry?.destination?.route ?: BarksAndMeowsRouter.SplashScreen.name
    )
    BarksAndMeowsTheme {
        Scaffold(
            topBar = {
                if (!doNotShowTopAppBar.contains(currentScreen)) {
                    BarksAndMeowsAppBar(
                        currentScreen = currentScreen,
                        canNavigateBack = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = BarksAndMeowsRouter.SplashScreen.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(500)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(500)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(500)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(500)
                    )
                }
            ) {
                composable(route = BarksAndMeowsRouter.SplashScreen.name) {
                    SplashScreen()
                }

                composable(route = BarksAndMeowsRouter.LoginScreen.name) {
                    LoginScreen()
                }

                composable(route = BarksAndMeowsRouter.HomeScreen.name) {
                    HomeScreen(coroutineScope = coroutineScope)
                }
            }
        }
    }
}