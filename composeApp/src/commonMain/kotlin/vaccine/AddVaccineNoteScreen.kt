package vaccine

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.dineshworkspace.uicomponents.composables.appBar.BarksAndMeowsAppBar

import navigation.NavRouter.getNavController

@Composable
fun AddVaccineNoteScreen(petId: String) {
    Scaffold(topBar = {
        BarksAndMeowsAppBar(
            canNavigateBack = getNavController()?.previousBackStackEntry != null,
            navigateUp = { getNavController()?.navigateUp() }
        )
    }) { innerPadding ->

    }
}