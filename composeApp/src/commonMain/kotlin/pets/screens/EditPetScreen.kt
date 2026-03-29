package pets.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.edit_pet_subtitle
import barksandmeows.composeapp.generated.resources.error_loading_pet
import barksandmeows.composeapp.generated.resources.update
import barksandmeows.composeapp.generated.resources.updating
import com.app.dataModels.Pet
import com.app.uicomponents.composables.error.ErrorComposable
import com.app.uicomponents.composables.loading.LoadingComposable
import com.app.viewModels.GetPetDetailsUiState
import com.app.viewModels.PetDetailsViewModel
import com.app.viewModels.PetViewModel
import com.app.viewModels.UpdatePetUiState
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import navigation.NavRouter
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3Api::class)
@Composable
fun EditPetScreen(
    petId: String,
    petViewModel: PetViewModel = koinViewModel(),
    petDetailsViewModel: PetDetailsViewModel = koinViewModel()
) {
    val updatePetUiState = petViewModel.updatePetUiState.collectAsState()
    val petDetailsUiState = petDetailsViewModel.petDetailsUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(petId) {
        launch {
            petDetailsViewModel.getPetDetails(petId = petId)
        }
        petViewModel.getPetCategories()
    }

    LaunchedEffect(updatePetUiState.value) {
        when (val state = updatePetUiState.value) {
            is UpdatePetUiState.Success -> {
                petViewModel.resetUpdatePetUiState()
                NavRouter.popBackStack()
            }

            is UpdatePetUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                petViewModel.resetUpdatePetUiState()
            }

            else -> {}
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    ) { paddingValues ->
        when (val petState = petDetailsUiState.value) {
            is GetPetDetailsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    LoadingComposable()
                }
            }

            is GetPetDetailsUiState.Success -> {
                val pet = petState.pet
                val petCategories = petViewModel.petCategories.collectAsState()
                var petName by remember(pet.name) { mutableStateOf(pet.name) }
                var hasInitializedCategory by remember { mutableStateOf(false) }

                val currentYear = remember {
                    Instant.fromEpochMilliseconds(kotlin.time.Clock.System.now().toEpochMilliseconds())
                        .toLocalDateTime(TimeZone.currentSystemDefault()).year
                }
                var selectedBirthYear by remember(pet.age) {
                    mutableStateOf<Int?>(currentYear - pet.age)
                }

                LaunchedEffect(petCategories.value.size, hasInitializedCategory) {
                    if (petCategories.value.isNotEmpty() && !hasInitializedCategory) {
                        petViewModel.updateSelectedCategory(pet.petCategory.id)
                        hasInitializedCategory = true
                    }
                }

                if (petCategories.value.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        LoadingComposable()
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .verticalScroll(scrollState)
                    ) {
                        Text(
                            text = stringResource(Res.string.edit_pet_subtitle),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(
                                start = 20.dp,
                                end = 20.dp,
                                bottom = 20.dp
                            )
                        )

                        PetFormContent(
                            petCategories = petCategories.value,
                            petName = petName,
                            onPetNameChange = { petName = it },
                            selectedBirthYear = selectedBirthYear,
                            onBirthYearSelected = { selectedBirthYear = it },
                            onCategorySelected = { id ->
                                coroutineScope.launch { petViewModel.updateSelectedCategory(id) }
                            }
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        val isLoading = updatePetUiState.value is UpdatePetUiState.Loading

                        Button(
                            shape = RoundedCornerShape(14.dp),
                            onClick = {
                                coroutineScope.launch {
                                    val updatedPet = Pet(
                                        id = pet.id,
                                        name = petName,
                                        age = selectedBirthYear?.let { currentYear - it } ?: pet.age,
                                        petCategory = petCategories.value.first { it.selected }
                                    )
                                    petViewModel.updatePet(updatedPet)
                                }
                            },
                            enabled = !isLoading
                                    && petName.isNotEmpty()
                                    && petCategories.value.any { it.selected }
                                    && selectedBirthYear != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .height(52.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isLoading) {
                                    stringResource(Res.string.updating)
                                } else {
                                    stringResource(Res.string.update)
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }

            is GetPetDetailsUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    ErrorComposable(errorMessage = stringResource(Res.string.error_loading_pet))
                }
            }
        }
    }
}
