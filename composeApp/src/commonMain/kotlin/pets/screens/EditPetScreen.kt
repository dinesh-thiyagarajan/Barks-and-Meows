package pets.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import com.app.uicomponents.composables.buttons.PrimaryFormButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
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

                val initialBirthDateMillis = remember(pet.birthDate, pet.age) {
                    pet.birthDate?.let { dateStr ->
                        val parts = dateStr.split("-")
                        if (parts.size == 3) {
                            val year = parts[0].toIntOrNull() ?: (currentYear - pet.age)
                            val month = parts[1].toIntOrNull() ?: 1
                            val day = parts[2].toIntOrNull() ?: 1
                            LocalDate(year, month, day).atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
                        } else {
                            LocalDate(currentYear - pet.age, 1, 1).atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
                        }
                    } ?: LocalDate(currentYear - pet.age, 1, 1).atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
                }

                var selectedBirthDateMillis by remember(initialBirthDateMillis) {
                    mutableStateOf<Long?>(initialBirthDateMillis)
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
                            selectedBirthDateMillis = selectedBirthDateMillis,
                            onBirthDateSelected = { selectedBirthDateMillis = it },
                            onCategorySelected = { id ->
                                coroutineScope.launch { petViewModel.updateSelectedCategory(id) }
                            }
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        val isLoading = updatePetUiState.value is UpdatePetUiState.Loading

                        PrimaryFormButton(
                            text = if (isLoading) stringResource(Res.string.updating) else stringResource(Res.string.update),
                            onClick = {
                                coroutineScope.launch {
                                    val birthDate = selectedBirthDateMillis?.let { millis ->
                                        val instant = Instant.fromEpochMilliseconds(millis)
                                        val localDate = instant.toLocalDateTime(TimeZone.UTC).date
                                        val month = localDate.monthNumber.toString().padStart(2, '0')
                                        val day = localDate.dayOfMonth.toString().padStart(2, '0')
                                        "${localDate.year}-$month-$day"
                                    }
                                    val age = selectedBirthDateMillis?.let { millis ->
                                        val year = Instant.fromEpochMilliseconds(millis)
                                            .toLocalDateTime(TimeZone.UTC).year
                                        currentYear - year
                                    } ?: pet.age
                                    val updatedPet = Pet(
                                        id = pet.id,
                                        name = petName,
                                        age = age,
                                        petCategory = petCategories.value.first { it.selected },
                                        birthDate = birthDate
                                    )
                                    petViewModel.updatePet(updatedPet)
                                }
                            },
                            enabled = !isLoading
                                    && petName.isNotEmpty()
                                    && petCategories.value.any { it.selected }
                                    && selectedBirthDateMillis != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                        )

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
