package pets.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.pet_age
import barksandmeows.composeapp.generated.resources.pet_name
import com.app.dataModels.Pet
import com.app.uicomponents.composables.buttons.PrimaryActionButtonComposable
import com.app.uicomponents.composables.chips.CategorySelectorChip
import com.app.uicomponents.composables.loading.LoadingComposable
import com.app.uicomponents.composables.textFields.PetInputTextFieldComposable
import com.app.viewModels.AddPetUiState
import com.app.viewModels.PetViewModel
import common.utils.generateUUID
import kotlinx.coroutines.launch
import navigation.NavRouter
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AddNewPetScreen(petViewModel: PetViewModel = koinViewModel()) {

    val addPetUiState = petViewModel.addPetUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    LaunchedEffect(petViewModel) {
        petViewModel.getPetCategories()
    }

    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState)
        ) {
            when (addPetUiState.value) {
                is AddPetUiState.Loading -> {
                    LoadingComposable()
                }

                is AddPetUiState.Success -> {
                    NavRouter.popBackStack()
                }

                is AddPetUiState.NotStarted -> {
                    val petCategories = petViewModel.petCategories.collectAsState()
                    var petName by remember { mutableStateOf("") }
                    var petAge by remember { mutableStateOf("0") }

                    // Header Section
                    Text(
                        text = "Add Your Pet",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Tell us about your furry friend",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // Category Section
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Pet Type",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(petCategories.value.size) { index ->
                                    CategorySelectorChip(
                                        label = petCategories.value[index].category,
                                        categoryId = petCategories.value[index].id,
                                        selected = petCategories.value[index].selected,
                                        drawableResource = petCategories.value[index].drawableResource,
                                        onChipSelected = {
                                            coroutineScope.launch {
                                                petViewModel.updateSelectedCategory(it)
                                            }
                                        })
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Pet Details Section
                    Text(
                        text = "Pet Details",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    PetInputTextFieldComposable(
                        textFieldValue = petName,
                        onValueChange = { petName = it },
                        label = { Text(stringResource(Res.string.pet_name)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PetInputTextFieldComposable(
                        textFieldValue = petAge,
                        onValueChange = { petAge = it },
                        label = { Text(stringResource(Res.string.pet_age)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    PrimaryActionButtonComposable(
                        coroutineScope = coroutineScope,
                        onButtonClick = {
                            coroutineScope.launch {
                                val pet = Pet(
                                    id = generateUUID(),
                                    name = petName,
                                    age = petAge.toInt(),
                                    petCategory = petCategories.value.first { it.selected }
                                )
                                petViewModel.addNewPet(pet)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = petName.isNotEmpty() && petCategories.value.any { it.selected },
                        buttonLabel = {
                            Text("Submit")
                        })
                }

                is AddPetUiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Error",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = (addPetUiState.value as AddPetUiState.Error).message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        PrimaryActionButtonComposable(
                            coroutineScope = coroutineScope,
                            onButtonClick = {
                                NavRouter.popBackStack()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = true,
                            buttonLabel = {
                                Text("Go Back")
                            }
                        )
                    }
                }
            }
        }
    }
}