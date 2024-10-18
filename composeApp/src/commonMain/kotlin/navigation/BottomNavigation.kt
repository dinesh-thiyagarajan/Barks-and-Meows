package navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

fun showBottomNavBar(route: String) = listOf(
    AppRouteActions.HomeScreen.route,
    AppRouteActions.ProfileScreen.route,
).contains(route)

fun showTopAppBar(route: String) = listOf(
    AppRouteActions.PetDetailScreen.route,
    AppRouteActions.AddVaccineNoteScreen.route,
    AppRouteActions.AddNewPetScreen.route,
).contains(route)

sealed class BottomNavItem(
    val route: String, val icon: ImageVector, val description: String
) {
    data object Home :
        BottomNavItem(AppRouteActions.HomeScreen.route, Icons.Default.Home, "Home")

    data object Profile :
        BottomNavItem(AppRouteActions.ProfileScreen.route, Icons.Default.Face, "Profile")
}


val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Profile
)