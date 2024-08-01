package navigation

import androidx.navigation.NamedNavArgument
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.login_screen
import barksandmeows.composeapp.generated.resources.splash_screen
import barksandmeows.composeapp.generated.resources.splash_screen_img_description
import org.jetbrains.compose.resources.StringResource

enum class BarksAndMeowsRouter(val title: StringResource) {
    SplashScreen(title = Res.string.splash_screen),
    LoginScreen(title = Res.string.login_screen),
    HomeScreen(title = Res.string.splash_screen_img_description),
}

sealed class Router(val route: String, val navArguments: List<NamedNavArgument> = emptyList()) {
    data object SplashRouter : Router(route = BarksAndMeowsRouter.SplashScreen.name)
    data object LoginRouter : Router(route = BarksAndMeowsRouter.LoginScreen.name)
    data object HomeRouter : Router(route = BarksAndMeowsRouter.HomeScreen.name)
}

val doNotShowTopAppBar = listOf(
    BarksAndMeowsRouter.SplashScreen,
    BarksAndMeowsRouter.LoginScreen,
    BarksAndMeowsRouter.HomeScreen
)