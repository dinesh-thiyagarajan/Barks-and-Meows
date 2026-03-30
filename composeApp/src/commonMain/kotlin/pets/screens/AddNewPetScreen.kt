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
import barksandmeows.composeapp.generated.resources.add_pet_subtitle
import barksandmeows.composeapp.generated.resources.submit
import com.app.dataModels.Pet
import com.app.uicomponents.composables.error.ErrorComposable
import com.app.uicomponents.composables.loading.LoadingComposable
import com.app.viewModels.AddPetUiState
import com.app.viewModels.PetViewModel
import common.utils.generateUUID
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
fun AddNewPetScreen(petViewModel: PetViewModel = koinViewModel()) {
    val addPetUiState = petViewModel.addPetUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(petViewModel) {
        petViewModel.getPetCategories()
    }

    LaunchedEffect(addPetUiState.value) {
        if (addPetUiState.value is AddPetUiState.Error) {
            snackbarHostState.showSnackbar((addPetUiState.value as AddPetUiState.Error).message)
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
        when (addPetUiState.value) {
            is AddPetUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    LoadingComposable()
                }
            }

            is AddPetUiState.Success -> {
                NavRouter.popBackStack()
            }

            is AddPetUiState.NotStarted, is AddPetUiState.Error -> {
                val petCategories = petViewModel.petCategories.collectAsState()
                var petName by remember { mutableStateOf("") }
                var selectedBirthDateMillis by remember { mutableStateOf<Long?>(null) }

                val currentYear = remember {
                    Instant.fromEpochMilliseconds(kotlin.time.Clock.System.now().toEpochMilliseconds())
                        .toLocalDateTime(TimeZone.currentSystemDefault()).year
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(scrollState)
                ) {
                    Text(
                        text = stringResource(Res.string.add_pet_subtitle),
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

                    PrimaryFormButton(
                        text = stringResource(Res.string.submit),
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
                                } ?: 0
                                val pet = Pet(
                                    id = generateUUID(),
                                    name = petName,
                                    age = age,
                                    petCategory = petCategories.value.first { it.selected },
                                    birthDate = birthDate
                                )
                                petViewModel.addNewPet(pet)
                            }
                        },
                        enabled = petName.isNotEmpty()
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
    }
}
