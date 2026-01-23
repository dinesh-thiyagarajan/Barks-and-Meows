package pets.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.cancel
import barksandmeows.composeapp.generated.resources.date_prefix
import barksandmeows.composeapp.generated.resources.delete
import barksandmeows.composeapp.generated.resources.delete_pet_message
import barksandmeows.composeapp.generated.resources.delete_pet_title
import barksandmeows.composeapp.generated.resources.delete_vaccine_note_description
import barksandmeows.composeapp.generated.resources.delete_vaccine_note_message
import barksandmeows.composeapp.generated.resources.delete_vaccine_note_title
import barksandmeows.composeapp.generated.resources.doctor_prefix
import barksandmeows.composeapp.generated.resources.edit
import barksandmeows.composeapp.generated.resources.more_options
import barksandmeows.composeapp.generated.resources.no_vaccine_notes
import barksandmeows.composeapp.generated.resources.no_vaccine_notes_description
import barksandmeows.composeapp.generated.resources.not_available
import barksandmeows.composeapp.generated.resources.notes_label
import com.dineshworkspace.composables.PetDetailsComposable
import com.dineshworkspace.extensions.getPetIcon
import com.dineshworkspace.uicomponents.composables.appBar.BarksAndMeowsAppBar
import com.dineshworkspace.uicomponents.composables.cards.VaccineNoteCardComposable
import com.dineshworkspace.uicomponents.composables.dialogs.ConfirmationDialog
import com.dineshworkspace.uicomponents.composables.emptyState.EmptyVaccineNotesComposable
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
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
fun PetDetailScreen(
    petId: String,
    petDetailsViewModel: PetDetailsViewModel = koinViewModel(),
    vaccineNoteViewModel: VaccineNoteViewModel = koinViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    var showDeletePetDialog by remember { mutableStateOf(false) }
    var vaccineNoteToDelete by remember { mutableStateOf<String?>(null) }
    var showMenu by remember { mutableStateOf(false) }

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

    // Delete Pet Confirmation Dialog
    if (showDeletePetDialog) {
        ConfirmationDialog(
            title = stringResource(Res.string.delete_pet_title),
            message = stringResource(Res.string.delete_pet_message),
            confirmButtonText = stringResource(Res.string.delete),
            dismissButtonText = stringResource(Res.string.cancel),
            onConfirm = {
                showDeletePetDialog = false
                coroutineScope.launch {
                    petDetailsViewModel.deletePet(petId = petId) {
                        NavRouter.popBackStack()
                    }
                }
            },
            onDismiss = {
                showDeletePetDialog = false
            }
        )
    }

    // Delete Vaccine Note Confirmation Dialog
    vaccineNoteToDelete?.let { noteId ->
        ConfirmationDialog(
            title = stringResource(Res.string.delete_vaccine_note_title),
            message = stringResource(Res.string.delete_vaccine_note_message),
            confirmButtonText = stringResource(Res.string.delete),
            dismissButtonText = stringResource(Res.string.cancel),
            onConfirm = {
                coroutineScope.launch {
                    vaccineNoteViewModel.deleteVaccineNote(
                        petId = petId,
                        vaccineNoteId = noteId
                    )
                }
                vaccineNoteToDelete = null
            },
            onDismiss = {
                vaccineNoteToDelete = null
            }
        )
    }

    Scaffold(
        topBar = {
            BarksAndMeowsAppBar(
                titleKey = null,
                canNavigateBack = true,
                navigateUp = { NavRouter.popBackStack() },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(Res.string.more_options)
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.edit)) },
                            onClick = {
                                showMenu = false
                                NavRouter.navigate("${AppRouteActions.EditPetScreen.route}${petId}")
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.delete)) },
                            onClick = {
                                showMenu = false
                                showDeletePetDialog = true
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize()
        ) {
            Box(
                modifier = Modifier
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
                        val petIcon = getPetIcon(petResponse.pet.petCategory.category)
                        PetDetailsComposable(
                            pet = petResponse.pet,
                            petIcon = petIcon,
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
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                when (val vaccineNotesUiState = getVaccineNotesUiState.value) {
                    is GetVaccineNotesUiState.Error -> {
                        ErrorComposable(errorMessage = vaccineNotesUiState.errorMessage)
                    }

                    GetVaccineNotesUiState.Loading -> {
                        LoadingComposable()
                    }

                    is GetVaccineNotesUiState.Success -> {
                        if (vaccineNotesUiState.vaccineNotesList.isEmpty()) {
                            EmptyVaccineNotesComposable(
                                title = stringResource(Res.string.no_vaccine_notes),
                                description = stringResource(Res.string.no_vaccine_notes_description)
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
                            ) {
                                items(vaccineNotesUiState.vaccineNotesList) { vaccineNote ->
                                    VaccineNoteCardComposable(
                                        vaccineName = vaccineNote.vaccine.vaccineName,
                                        doctorName = vaccineNote.doctorName,
                                        date = vaccineNote.timestamp,
                                        notes = vaccineNote.note,
                                        doctorLabel = stringResource(Res.string.doctor_prefix),
                                        dateLabel = stringResource(Res.string.date_prefix),
                                        notesLabel = stringResource(Res.string.notes_label),
                                        notAvailableText = stringResource(Res.string.not_available),
                                        deleteDescription = stringResource(Res.string.delete_vaccine_note_description),
                                        onDeleteClick = {
                                            vaccineNoteToDelete = vaccineNote.id
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



