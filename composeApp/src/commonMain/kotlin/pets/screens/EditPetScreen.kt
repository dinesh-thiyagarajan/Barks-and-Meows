package pets.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.edit_pet_subtitle
import barksandmeows.composeapp.generated.resources.edit_pet_title
import barksandmeows.composeapp.generated.resources.error_loading_pet
import barksandmeows.composeapp.generated.resources.pet_age
import barksandmeows.composeapp.generated.resources.pet_name
import barksandmeows.composeapp.generated.resources.pet_type
import barksandmeows.composeapp.generated.resources.update
import barksandmeows.composeapp.generated.resources.updating
import com.app.dataModels.Pet
import com.app.uicomponents.composables.chips.CategorySelectorChip
import com.app.uicomponents.composables.error.ErrorComposable
import com.app.uicomponents.composables.loading.LoadingComposable
import com.app.viewModels.GetPetDetailsUiState
import com.app.viewModels.PetDetailsViewModel
import com.app.viewModels.PetViewModel
import com.app.viewModels.UpdatePetUiState
import kotlinx.coroutines.launch
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
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.edit_pet_title),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { NavRouter.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
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
                var petAge by remember(pet.age) { mutableStateOf(pet.age.toString()) }
                var hasInitializedCategory by remember { mutableStateOf(false) }

                // Set the initial selected category when categories are first loaded
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
                        // Subtitle
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

                        // --- Pet Type Section ---
                        EditPetSectionHeader(
                            icon = Icons.Outlined.Category,
                            title = stringResource(Res.string.pet_type)
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
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
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // --- Pet Details Section ---
                        EditPetSectionHeader(
                            icon = Icons.Outlined.Pets,
                            title = "Pet Details"
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                OutlinedTextField(
                                    value = petName,
                                    onValueChange = { petName = it },
                                    label = { Text(stringResource(Res.string.pet_name)) },
                                    singleLine = true,
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Outlined.Badge,
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Next
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = petAge,
                                    onValueChange = { petAge = it },
                                    label = { Text(stringResource(Res.string.pet_age)) },
                                    singleLine = true,
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Outlined.Cake,
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // --- Update Button ---
                        val isLoading = updatePetUiState.value is UpdatePetUiState.Loading

                        Button(
                            shape = RoundedCornerShape(14.dp),
                            onClick = {
                                coroutineScope.launch {
                                    val updatedPet = Pet(
                                        id = pet.id,
                                        name = petName,
                                        age = petAge.toInt(),
                                        petCategory = petCategories.value.first { it.selected }
                                    )
                                    petViewModel.updatePet(updatedPet)
                                }
                            },
                            enabled = !isLoading && petName.isNotEmpty() && petCategories.value.any { it.selected },
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

@Composable
private fun EditPetSectionHeader(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
            modifier = Modifier.size(28.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
