package pets.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
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
import com.app.composables.PetDetailsComposable
import com.app.extensions.getPetIcon
import com.app.uicomponents.composables.appBar.BarksAndMeowsAppBar
import com.app.uicomponents.composables.cards.VaccineNoteCardComposable
import com.app.uicomponents.composables.dialogs.ConfirmationDialog
import com.app.uicomponents.composables.emptyState.EmptyVaccineNotesComposable
import com.app.uicomponents.composables.error.ErrorComposable
import com.app.uicomponents.composables.loading.LoadingComposable
import com.app.vaccine.viewModels.GetVaccineNotesUiState
import com.app.vaccine.viewModels.VaccineNoteViewModel
import com.app.viewModels.GetPetDetailsUiState
import com.app.viewModels.PetDetailsViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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

    // String resources (resolved once for reuse in items)
    val doctorLabel = stringResource(Res.string.doctor_prefix)
    val dateLabel = stringResource(Res.string.date_prefix)
    val notesLabel = stringResource(Res.string.notes_label)
    val notAvailableText = stringResource(Res.string.not_available)
    val deleteDescription = stringResource(Res.string.delete_vaccine_note_description)

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
                        onDismissRequest = { showMenu = false },
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(Res.string.edit),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            onClick = {
                                showMenu = false
                                NavRouter.navigate("${AppRouteActions.EditPetScreen.route}$petId")
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.EditNote,
                                    contentDescription = null,
                                    modifier = Modifier.size(22.dp),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(Res.string.delete),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.error
                                )
                            },
                            onClick = {
                                showMenu = false
                                showDeletePetDialog = true
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.DeleteOutline,
                                    contentDescription = null,
                                    modifier = Modifier.size(22.dp),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val petResponse = petDetailsUiState.value) {
            is GetPetDetailsUiState.Error -> {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    ErrorComposable(errorMessage = petResponse.errorMessage)
                }
            }

            GetPetDetailsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    LoadingComposable()
                }
            }

            is GetPetDetailsUiState.Success -> {
                val petIcon = getPetIcon(petResponse.pet.petCategory.category)

                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    // Pet profile hero section
                    item {
                        PetDetailsComposable(
                            pet = petResponse.pet,
                            petIcon = petIcon,
                            onAddVaccineNoteClicked = { id, petName ->
                                NavRouter.navigate("${AppRouteActions.AddVaccineNoteScreen.route}$id/$petName")
                            }
                        )
                    }

                    // Vaccine notes section
                    when (val vaccineNotesUiState = getVaccineNotesUiState.value) {
                        is GetVaccineNotesUiState.Error -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                ) {
                                    ErrorComposable(errorMessage = vaccineNotesUiState.errorMessage)
                                }
                            }
                        }

                        GetVaccineNotesUiState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    LoadingComposable()
                                }
                            }
                        }

                        is GetVaccineNotesUiState.Success -> {
                            if (vaccineNotesUiState.vaccineNotesList.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp)
                                    ) {
                                        EmptyVaccineNotesComposable(
                                            title = stringResource(Res.string.no_vaccine_notes),
                                            description = stringResource(Res.string.no_vaccine_notes_description)
                                        )
                                    }
                                }
                            } else {
                                items(vaccineNotesUiState.vaccineNotesList) { vaccineNote ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp)
                                    ) {
                                        VaccineNoteCardComposable(
                                            vaccineName = vaccineNote.vaccine.vaccineName,
                                            doctorName = vaccineNote.doctorName,
                                            date = vaccineNote.timestamp,
                                            notes = vaccineNote.note,
                                            doctorLabel = doctorLabel,
                                            dateLabel = dateLabel,
                                            notesLabel = notesLabel,
                                            notAvailableText = notAvailableText,
                                            deleteDescription = deleteDescription,
                                            reminderDate = vaccineNote.reminderTimestamp?.let { millis ->
                                                val instant = Instant.fromEpochMilliseconds(millis)
                                                val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
                                                val month = localDate.monthNumber.toString().padStart(2, '0')
                                                val day = localDate.dayOfMonth.toString().padStart(2, '0')
                                                "${localDate.year}-$month-$day"
                                            },
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
}
