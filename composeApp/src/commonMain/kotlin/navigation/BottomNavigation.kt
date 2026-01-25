package navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.home
import barksandmeows.composeapp.generated.resources.nav_profile
import org.jetbrains.compose.resources.StringResource

fun showBottomNavBar(route: String) = listOf(
    AppRouteActions.HomeScreen.route,
    AppRouteActions.ProfileScreen.route,
).contains(route)

fun showTopAppBar(route: String) = listOf(
    AppRouteActions.AddVaccineNoteScreen.route,
    AppRouteActions.AddNewPetScreen.route,
    AppRouteActions.EditPetScreen.route,
).contains(route)

sealed class BottomNavItem(
    val route: String, val icon: ImageVector, val descriptionRes: StringResource
) {
    data object Home :
        BottomNavItem(AppRouteActions.HomeScreen.route, Icons.Default.Home, Res.string.home)

    data object Profile :
        BottomNavItem(AppRouteActions.ProfileScreen.route, Icons.Default.Face, Res.string.nav_profile)
}


val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Profile
)