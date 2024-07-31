package navigation

import androidx.navigation.NamedNavArgument
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.login_screen
import barksandmeows.composeapp.generated.resources.splash_screen
import org.jetbrains.compose.resources.StringResource

enum class BarksAndMeowsRouter(val title: StringResource) {
    SplashScreen(title = Res.string.splash_screen),
    LoginScreen(title = Res.string.login_screen),
}

sealed class Router(val route: String, val navArguments: List<NamedNavArgument> = emptyList()) {
    data object SplashRouter : Router(route = BarksAndMeowsRouter.SplashScreen.name)
    data object LoginRouter : Router(route = BarksAndMeowsRouter.LoginScreen.name)
}