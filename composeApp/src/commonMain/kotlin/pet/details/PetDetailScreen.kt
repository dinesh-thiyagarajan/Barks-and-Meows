package pet.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import common.composables.BarksAndMeowsAppBar
import navigation.NavRouter.getNavController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pet.details.viewModels.PetDetailsViewModel

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
            Text("Received Id is $petId")
        }
    }

}