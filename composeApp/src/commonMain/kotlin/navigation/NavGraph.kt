package navigation

import androidx.navigation.NamedNavArgument

private object Route {
    const val SPLASH_SCREEN = "splash"
    const val LOGIN_SCREEN = "login"
}

sealed class Router(val route: String, val navArguments: List<NamedNavArgument> = emptyList()) {
    data object SplashRouter : Router(route = Route.SPLASH_SCREEN)
    data object LoginRouter : Router(route = Route.LOGIN_SCREEN)
}