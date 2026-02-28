package navigation

enum class AppRouteActions(
    val route: String,
    val navArguments: List<String> = emptyList(),
) {
    SplashScreen(route = "splash/"),
    LoginScreen(route = "login/"),
    SignUpScreen(route = "signup/"),
    ForgotPasswordScreen(route = "forgot-password/"),
    HomeScreen(route = "home/"),
    ProfileScreen(route = "profile/"),
    AddNewPetScreen(route = "add-new-pet/"),
    EditPetScreen(route = "edit-pet/", listOf(NavConstants.PET_ID)),
    PetDetailScreen(route = "pet-details/", listOf(NavConstants.PET_ID)),
    AddVaccineNoteScreen(route = "add-vaccine-note/", listOf(NavConstants.PET_ID, NavConstants.PET_NAME)),
    ReminderScreen(route = "reminders/"),
    FeedingReminderDetailScreen(route = "feeding-reminder-detail/", listOf(NavConstants.REMINDER_ID)),
}

fun AppRouteActions.path(): String {
    if (navArguments.isEmpty()) return route
    var completePath = route
    for (argument in navArguments) {
        completePath += "{$argument}/"
    }
    return completePath.removeSuffix("/")
}

object NavConstants {
    const val PET_ID = "pet_id"
    const val PET_NAME = "pet_name"
    const val REMINDER_ID = "reminder_id"
}
