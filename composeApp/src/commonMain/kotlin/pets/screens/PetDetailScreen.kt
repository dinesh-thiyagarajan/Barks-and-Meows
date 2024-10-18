package pets.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dineshworkspace.composables.PetDetailsComposable
import com.dineshworkspace.uicomponents.composables.error.ErrorComposable
import com.dineshworkspace.uicomponents.composables.loading.LoadingComposable
import com.dineshworkspace.vaccine.viewModels.GetVaccineNotesUiState
import com.dineshworkspace.vaccine.viewModels.VaccineNoteViewModel
import com.dineshworkspace.viewModels.GetPetDetailsUiState
import com.dineshworkspace.viewModels.PetDetailsViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import navigation.AppRouteActions
import navigation.NavRouter
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
fun PetDetailScreen(
    petId: String,
    petDetailsViewModel: PetDetailsViewModel = koinViewModel(),
    vaccineNoteViewModel: VaccineNoteViewModel = koinViewModel(),
) {

    LaunchedEffect(petId) {
        coroutineScope {
            launch {
                petDetailsViewModel.getPetDetails(petId = petId)
            }

            launch {
                vaccineNoteViewModel.getVaccineNotes(petId = petId)
            }
        }
    }

    val petDetailsUiState = petDetailsViewModel.petDetailsUiState.collectAsState()
    val getVaccineNotesUiState = vaccineNoteViewModel.getVaccineNoteUiState.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.padding(paddingValues = innerPadding).fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                when (val petResponse = petDetailsUiState.value) {
                    is GetPetDetailsUiState.Error -> {
                        ErrorComposable(errorMessage = petResponse.errorMessage)
                    }

                    GetPetDetailsUiState.Loading -> {
                        LoadingComposable()
                    }

                    is GetPetDetailsUiState.Success -> {
                        PetDetailsComposable(
                            pet = petResponse.pet,
                            onAddVaccineNoteClicked = { petId ->
                                NavRouter.navigate("${AppRouteActions.AddVaccineNoteScreen.route}${petId}")
                            })
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                when (val vaccineNotesUiState = getVaccineNotesUiState.value) {
                    is GetVaccineNotesUiState.Error -> {
                        ErrorComposable(errorMessage = vaccineNotesUiState.errorMessage)
                    }

                    GetVaccineNotesUiState.Loading -> {
                        LoadingComposable()
                    }

                    is GetVaccineNotesUiState.Success -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
                        ) {
                            items(vaccineNotesUiState.vaccineNotesList.size) { index ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Text(vaccineNotesUiState.vaccineNotesList[index].vaccine.vaccineName)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



