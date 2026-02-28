package navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.home
import barksandmeows.composeapp.generated.resources.nav_profile
import barksandmeows.composeapp.generated.resources.nav_reminders
import org.jetbrains.compose.resources.StringResource

fun showBottomNavBar(route: String) = listOf(
    AppRouteActions.HomeScreen.route,
    AppRouteActions.ProfileScreen.route,
    AppRouteActions.ReminderScreen.route,
).contains(route)

fun showTopAppBar(route: String) = listOf(
    AppRouteActions.AddNewPetScreen.route,
    AppRouteActions.EditPetScreen.route,
).contains(route)

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val descriptionRes: StringResource
) {
    data object Home :
        BottomNavItem(AppRouteActions.HomeScreen.route, Icons.Default.Home, Res.string.home)

    data object Reminders :
        BottomNavItem(AppRouteActions.ReminderScreen.route, Icons.Default.Notifications, Res.string.nav_reminders)

    data object Profile :
        BottomNavItem(AppRouteActions.ProfileScreen.route, Icons.Default.Face, Res.string.nav_profile)
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Reminders,
    BottomNavItem.Profile
)
