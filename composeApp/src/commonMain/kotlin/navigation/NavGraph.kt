package navigation

enum class AppRouteActions(
    val route: String,
    val navArguments: List<String> = emptyList(),
) {
    SplashScreen(route = "splash/"),
    LoginScreen(route = "login/"),
    HomeScreen(route = "home/"),
    ProfileScreen(route = "profile/"),
    AddNewPetScreen(route = "add-new-pet/"),
    PetDetailScreen(route = "pet-details/", listOf(NavConstants.PET_ID)),
}

fun AppRouteActions.path(): String {
    if (navArguments.isEmpty()) return route
    var completePath = route
    for (argument in navArguments) {
        completePath += "{${argument}}/"
    }
    return completePath.removeSuffix("/")
}

object NavConstants {
    const val PET_ID = "pet_id"
}