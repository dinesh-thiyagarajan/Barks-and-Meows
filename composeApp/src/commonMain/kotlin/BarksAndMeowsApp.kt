import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import auth.LoginScreen
import di.appModule
import home.HomeScreen
import navigation.AppRouteActions
import navigation.BottomNavItem
import navigation.NavRouter
import navigation.showBottomNavBar
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import profile.ProfileScreen
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
    val currentDestination = backStackEntry?.destination

    val screensList = listOf(
        BottomNavItem.Home,
        BottomNavItem.Profile
    )

    BarksAndMeowsTheme {
        Scaffold(
            bottomBar =
            {
                AnimatedVisibility(showBottomNavBar(currentDestination?.route ?: "")) {
                    NavigationBar {
                        screensList.forEach { item ->
                            NavigationBarItem(
                                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                                label = {},
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.description
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color.Red,
                                    unselectedIconColor = Color.Gray
                                ),
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(
                                            navController.graph.findStartDestination().route
                                                ?: ""
                                        ) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }

                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = AppRouteActions.SplashScreen.route,
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
                composable(route = AppRouteActions.SplashScreen.route) {
                    SplashScreen()
                }

                composable(route = AppRouteActions.LoginScreen.route) {
                    LoginScreen()
                }

                composable(route = AppRouteActions.HomeScreen.route) {
                    HomeScreen()
                }

                composable(route = AppRouteActions.ProfileScreen.route) {
                    ProfileScreen()
                }
            }
        }
    }
}