package vaccine

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.Notes
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Vaccines
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.unit.dp
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.add_vaccine_note
import barksandmeows.composeapp.generated.resources.doctor_name
import barksandmeows.composeapp.generated.resources.select_vaccine
import com.app.uicomponents.composables.error.ErrorComposable
import com.app.uicomponents.composables.loading.LoadingComposable
import com.app.uicomponents.composables.textFields.GenericInputTextFieldComposable
import com.app.vaccine.dataModels.Vaccine
import com.app.vaccine.dataModels.VaccineNote
import com.app.vaccine.viewModels.AddVaccineNoteUiState
import com.app.vaccine.viewModels.VaccineNoteViewModel
import common.utils.generateUUID
import kotlinx.coroutines.CoroutineScope
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
fun AddVaccineNoteScreen(
    petId: String,
    petName: String = "",
    vaccineNoteViewModel: VaccineNoteViewModel = koinViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(vaccineNoteViewModel) {
        vaccineNoteViewModel.getVaccinesList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Vaccine Record",
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
        }
    ) { paddingValues ->
        val getVaccineNoteUiState = vaccineNoteViewModel.addVaccineNoteUiState.collectAsState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val uiState = getVaccineNoteUiState.value) {
                is AddVaccineNoteUiState.FetchingVaccines -> {
                    LoadingComposable()
                }

                AddVaccineNoteUiState.AddingVaccineNote -> {
                    LoadingComposable()
                }

                is AddVaccineNoteUiState.Error -> {
                    ErrorComposable(uiState.errorMessage)
                }

                AddVaccineNoteUiState.VaccineNotesAddedSuccessfully -> {
                    NavRouter.popBackStack()
                }

                is AddVaccineNoteUiState.VaccinesFetchedSuccessfully -> {
                    AddNewVaccineNoteComposable(
                        petId = petId,
                        petName = petName,
                        vaccineNoteViewModel = vaccineNoteViewModel,
                        coroutineScope = coroutineScope,
                        vaccines = uiState.vaccineList
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddNewVaccineNoteComposable(
    petId: String,
    petName: String,
    vaccineNoteViewModel: VaccineNoteViewModel,
    coroutineScope: CoroutineScope,
    vaccines: List<Vaccine>
) {
    val scrollState = rememberScrollState()
    var dateTimeStamp by remember { mutableStateOf("") }
    var doctorName by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var dropDownExpanded by remember { mutableStateOf(false) }
    var selectedVaccine: Vaccine? by remember { mutableStateOf(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // Reminder state
    var reminderEnabled by remember { mutableStateOf(false) }
    var reminderDateMillis by remember { mutableStateOf<Long?>(null) }
    var reminderDateDisplay by remember { mutableStateOf("") }
    var showReminderDatePicker by remember { mutableStateOf(false) }
    val reminderDatePickerState = rememberDatePickerState()

    val isFormValid = selectedVaccine != null && doctorName.isNotEmpty() && dateTimeStamp.isNotEmpty()

    // Reminder Date Picker Dialog
    if (showReminderDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showReminderDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        reminderDatePickerState.selectedDateMillis?.let { millis ->
                            reminderDateMillis = millis
                            val instant = Instant.fromEpochMilliseconds(millis)
                            val localDate =
                                instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
                            val month = localDate.monthNumber.toString().padStart(2, '0')
                            val day = localDate.dayOfMonth.toString().padStart(2, '0')
                            reminderDateDisplay = "${localDate.year}-$month-$day"
                        }
                        showReminderDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReminderDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = reminderDatePickerState)
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val instant = Instant.fromEpochMilliseconds(millis)
                            val localDate = instant.toLocalDateTime(TimeZone.UTC).date
                            val month = localDate.monthNumber.toString().padStart(2, '0')
                            val day = localDate.dayOfMonth.toString().padStart(2, '0')
                            dateTimeStamp = "${localDate.year}-$month-$day"
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // --- Vaccine Details Section ---
        SectionHeader(
            icon = Icons.Outlined.Vaccines,
            title = "Vaccine Details"
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
                // Vaccine dropdown
                ExposedDropdownMenuBox(
                    expanded = dropDownExpanded,
                    onExpandedChange = { dropDownExpanded = it }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = selectedVaccine?.vaccineName
                            ?: stringResource(Res.string.select_vaccine),
                        onValueChange = {},
                        label = { Text(text = stringResource(Res.string.select_vaccine)) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropDownExpanded)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Vaccines,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = dropDownExpanded,
                        onDismissRequest = { dropDownExpanded = false }
                    ) {
                        vaccines.forEach {
                            DropdownMenuItem(
                                text = { Text(it.vaccineName) },
                                onClick = {
                                    selectedVaccine = it
                                    dropDownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Vaccination date
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true }
                ) {
                    OutlinedTextField(
                        value = dateTimeStamp,
                        onValueChange = {},
                        label = { Text("Vaccination Date") },
                        readOnly = true,
                        enabled = false,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.CalendarMonth,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.CalendarMonth,
                                contentDescription = "Select date",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- Doctor & Notes Section ---
        SectionHeader(
            icon = Icons.Outlined.LocalHospital,
            title = "Doctor & Notes"
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
                // Doctor name
                GenericInputTextFieldComposable(
                    textFieldValue = doctorName,
                    onValueChange = { doctorName = it },
                    label = { Text(stringResource(Res.string.doctor_name)) },
                    modifier = Modifier.fillMaxWidth(),
                )

                // Notes
                GenericInputTextFieldComposable(
                    textFieldValue = note,
                    onValueChange = { note = it },
                    label = { Text("Notes (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- Reminder Section ---
        SectionHeader(
            icon = Icons.Outlined.Notifications,
            title = "Reminder"
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
                // Reminder toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Set Vaccine Reminder",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Get notified on a specific date",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = reminderEnabled,
                        onCheckedChange = {
                            reminderEnabled = it
                            if (!it) {
                                reminderDateMillis = null
                                reminderDateDisplay = ""
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                // Animated reminder date picker
                AnimatedVisibility(
                    visible = reminderEnabled,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showReminderDatePicker = true }
                        ) {
                            OutlinedTextField(
                                value = reminderDateDisplay,
                                onValueChange = {},
                                label = { Text("Reminder Date") },
                                readOnly = true,
                                enabled = false,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Notifications,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.CalendarMonth,
                                        contentDescription = "Select reminder date",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    disabledTrailingIconColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- Save Button ---
        Button(
            shape = RoundedCornerShape(14.dp),
            onClick = {
                coroutineScope.launch {
                    vaccineNoteViewModel.addVaccineNote(
                        vaccineNote = VaccineNote(
                            id = generateUUID(),
                            petId = petId,
                            vaccine = selectedVaccine ?: return@launch,
                            note = note,
                            timestamp = dateTimeStamp,
                            doctorName = doctorName,
                            reminderTimestamp = if (reminderEnabled) reminderDateMillis else null,
                            petName = petName
                        )
                    )
                }
            },
            enabled = isFormValid,
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
                text = stringResource(Res.string.add_vaccine_note),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun SectionHeader(
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
