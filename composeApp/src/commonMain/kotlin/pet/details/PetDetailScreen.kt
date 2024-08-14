package pet.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.dineshworkspace.database.pet.dataModels.Pet
import com.dineshworkspace.uicomponents.composables.cards.HelloPetCardComposable
import common.composables.BarksAndMeowsAppBar
import common.composables.ErrorComposable
import common.composables.LoadingComposable
import navigation.NavRouter.getNavController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pet.details.viewModels.GetPetDetailsUiState
import pet.details.viewModels.PetDetailsViewModel
import pet.toPetData

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
        Column(modifier = Modifier.padding(paddingValues = innerPadding).fillMaxSize()) {
            when (val petResponse = petDetailsUiState.value) {
                is GetPetDetailsUiState.Error -> {
                    ErrorComposable(errorMessage = petResponse.errorMessage)
                }

                GetPetDetailsUiState.Loading -> {
                    LoadingComposable()
                }

                is GetPetDetailsUiState.Success -> {
                    PetDetailsComposable(pet = petResponse.pet, this)
                }
            }
        }
    }
}

@Composable
internal fun PetDetailsComposable(pet: Pet, columnScope: ColumnScope) {
    columnScope.apply {
        //HelloPetCardComposable(petData = pet.toPetData())
    }
}

