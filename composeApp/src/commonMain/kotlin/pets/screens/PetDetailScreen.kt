package pets.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.dineshworkspace.composables.PetDetailsComposable
import com.dineshworkspace.uicomponents.composables.appBar.BarksAndMeowsAppBar
import com.dineshworkspace.uicomponents.composables.error.ErrorComposable
import com.dineshworkspace.uicomponents.composables.loading.LoadingComposable
import com.dineshworkspace.viewModels.GetPetDetailsUiState
import com.dineshworkspace.viewModels.PetDetailsViewModel
import navigation.AppRouteActions
import navigation.NavRouter
import navigation.NavRouter.getNavController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
fun PetDetailScreen(petId: String, petDetailsViewModel: PetDetailsViewModel = koinViewModel()) {
    LaunchedEffect(petId) {
        petDetailsViewModel.getPetDetails(petId = petId)
    }

    val petDetailsUiState = petDetailsViewModel.petDetailsUiState.collectAsState()

    Scaffold(topBar = {
        BarksAndMeowsAppBar(
            canNavigateBack = getNavController()?.previousBackStackEntry != null,
            navigateUp = { getNavController()?.navigateUp() }
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier.padding(paddingValues = innerPadding).fillMaxSize()
        ) {
            when (val petResponse = petDetailsUiState.value) {
                is GetPetDetailsUiState.Error -> {
                    ErrorComposable(errorMessage = petResponse.errorMessage)
                }

                GetPetDetailsUiState.Loading -> {
                    LoadingComposable()
                }

                is GetPetDetailsUiState.Success -> {
                    PetDetailsComposable(pet = petResponse.pet, onAddVaccineNoteClicked = { petId ->
                        NavRouter.navigate("${AppRouteActions.AddVaccineNoteScreen.route}${petId}")
                    })
                }
            }
        }
    }
}



