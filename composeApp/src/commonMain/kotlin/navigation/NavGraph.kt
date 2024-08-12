package navigation

enum class AppRouteActions(
    val route: String,
    val navArguments: List<String> = emptyList(),
) {
    SplashScreen(route = "splash/"),
    LoginScreen(route = "login/"),
    HomeScreen(route = "home/"),
    ProfileScreen(route = "profile/"),
    AddNewPetScreen(route = "addNewPet/"),
}

fun AppRouteActions.path(): String {
    if (navArguments.isEmpty()) return route
    var completePath = route
    for (argument in navArguments) {
        completePath += "{${argument}}/"
    }
    return completePath.removeSuffix("/")
}