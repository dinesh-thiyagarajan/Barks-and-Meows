package navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions

object NavRouter {
    // possible memory leak ?? needs refactoring
    private var navController: NavController? = null

    // Set navController before initializing nav graph
    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun navigate(route: String, navOptions: NavOptions? = null) {
        navController?.navigate(route = route, navOptions = navOptions)
    }

    fun popBackStack() {
        navController?.popBackStack()
    }
}
