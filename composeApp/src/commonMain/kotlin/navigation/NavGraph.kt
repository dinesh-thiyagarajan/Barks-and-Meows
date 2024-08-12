package navigation

val doNotShowTopAppBar = listOf(
    AppRouteActions.SplashScreen,
    AppRouteActions.LoginScreen,
    AppRouteActions.HomeScreen
)

enum class AppRouteActions(
    val route: String,
    val navArguments: List<String> = emptyList(),
) {
    SplashScreen(route = "splash/"),
    LoginScreen(route = "login/"),
    HomeScreen(route = "home/"),
    ProfileScreen(route = "profile/"),
}

fun AppRouteActions.path(): String {
    if (navArguments.isEmpty()) return route
    var completePath = route
    for (argument in navArguments) {
        completePath += "{${argument}}/"
    }
    return completePath.removeSuffix("/")
}