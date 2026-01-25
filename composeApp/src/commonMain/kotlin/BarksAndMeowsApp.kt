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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import auth.LoginScreen
import com.app.uicomponents.composables.appBar.BarksAndMeowsAppBar
import di.appModule
import home.HomeScreen
import navigation.AppRouteActions
import navigation.NavConstants
import navigation.NavRouter
import navigation.NavRouter.getNavController
import navigation.bottomNavItems
import navigation.path
import navigation.showBottomNavBar
import navigation.showTopAppBar
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.error_pet_id_not_passed
import org.koin.compose.KoinApplication
import pets.screens.AddNewPetScreen
import pets.screens.EditPetScreen
import pets.screens.PetDetailScreen
import profile.ProfileScreen
import signup.SignUpScreen
import splash.SplashScreen
import theme.BarksAndMeowsTheme
import vaccine.AddVaccineNoteScreen

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
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    BarksAndMeowsTheme {
        Scaffold(
            topBar = {
                currentDestination?.route?.let {
                    AnimatedVisibility(showTopAppBar(it.substringBefore("{"))) {
                        BarksAndMeowsAppBar(
                            canNavigateBack = getNavController()?.previousBackStackEntry != null,
                            navigateUp = { getNavController()?.navigateUp() }
                        )
                    }
                }
            },
            bottomBar = {
                AnimatedVisibility(showBottomNavBar(currentDestination?.route ?: "")) {
                    NavigationBar {
                        bottomNavItems.forEach { item ->
                            NavigationBarItem(
                                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                                label = {},
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = stringResource(item.descriptionRes)
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
                composable(route = AppRouteActions.SplashScreen.path()) {
                    SplashScreen()
                }

                composable(route = AppRouteActions.LoginScreen.path()) {
                    LoginScreen(onSignUpClicked = { NavRouter.navigate(AppRouteActions.SignUpScreen.route) })
                }

                composable(route = AppRouteActions.SignUpScreen.path()) {
                    SignUpScreen()
                }

                composable(route = AppRouteActions.HomeScreen.path()) {
                    HomeScreen()
                }

                composable(route = AppRouteActions.ProfileScreen.path()) {
                    ProfileScreen()
                }

                composable(route = AppRouteActions.AddNewPetScreen.path()) {
                    AddNewPetScreen()
                }

                composable(
                    route = AppRouteActions.EditPetScreen.path(),
                    arguments = AppRouteActions.EditPetScreen.navArguments.map {
                        navArgument(it) {
                            type = NavType.StringType
                            defaultValue = ""
                        }
                    }) {
                    val petId = it.arguments?.getString(NavConstants.PET_ID)
                    val defaultPetId = stringResource(Res.string.error_pet_id_not_passed)
                    EditPetScreen(petId = petId ?: defaultPetId)
                }

                composable(
                    route = AppRouteActions.PetDetailScreen.path(),
                    arguments = AppRouteActions.PetDetailScreen.navArguments.map {
                        navArgument(it) {
                            type = NavType.StringType
                            defaultValue = ""
                        }
                    }) {
                    val petId = it.arguments?.getString(NavConstants.PET_ID)
                    val defaultPetId = stringResource(Res.string.error_pet_id_not_passed)
                    PetDetailScreen(petId = petId ?: defaultPetId)
                }

                composable(
                    route = AppRouteActions.AddVaccineNoteScreen.path(),
                    arguments = AppRouteActions.AddVaccineNoteScreen.navArguments.map {
                        navArgument(it) {
                            type = NavType.StringType
                            defaultValue = ""
                        }
                    }) {
                    val petId = it.arguments?.getString(NavConstants.PET_ID)
                    val defaultPetId = stringResource(Res.string.error_pet_id_not_passed)
                    AddVaccineNoteScreen(petId = petId ?: defaultPetId)
                }
            }
        }
    }
}