package vaccine

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Vaccines
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.unit.dp
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.add_vaccine_note
import barksandmeows.composeapp.generated.resources.add_vaccine_record
import barksandmeows.composeapp.generated.resources.back
import barksandmeows.composeapp.generated.resources.doctor_and_notes
import barksandmeows.composeapp.generated.resources.doctor_name
import barksandmeows.composeapp.generated.resources.notes_optional
import barksandmeows.composeapp.generated.resources.one_month
import barksandmeows.composeapp.generated.resources.one_year
import barksandmeows.composeapp.generated.resources.quick_reminder_label
import barksandmeows.composeapp.generated.resources.reminder
import barksandmeows.composeapp.generated.resources.reminder_date
import barksandmeows.composeapp.generated.resources.select_date_description
import barksandmeows.composeapp.generated.resources.select_reminder_date_description
import barksandmeows.composeapp.generated.resources.select_vaccine
import barksandmeows.composeapp.generated.resources.set_vaccine_reminder
import barksandmeows.composeapp.generated.resources.six_months
import barksandmeows.composeapp.generated.resources.three_months
import barksandmeows.composeapp.generated.resources.vaccination_date
import barksandmeows.composeapp.generated.resources.vaccine_details
import barksandmeows.composeapp.generated.resources.vaccine_reminder_description
import com.app.uicomponents.composables.buttons.PrimaryFormButton
import com.app.uicomponents.composables.cards.FormSectionCard
import com.app.uicomponents.composables.datePicker.DatePickerFieldComposable
import com.app.uicomponents.composables.error.ErrorComposable
import com.app.uicomponents.composables.loading.LoadingComposable
import com.app.uicomponents.composables.section.SectionHeader
import com.app.uicomponents.composables.textFields.GenericInputTextFieldComposable
import com.app.vaccine.dataModels.Vaccine
import com.app.vaccine.dataModels.VaccineNote
import com.app.vaccine.viewModels.AddVaccineNoteUiState
import com.app.vaccine.viewModels.VaccineNoteViewModel
import common.utils.generateUUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
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
                        text = stringResource(Res.string.add_vaccine_record),
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { NavRouter.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.back)
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
                is AddVaccineNoteUiState.FetchingVaccines -> LoadingComposable()
                AddVaccineNoteUiState.AddingVaccineNote -> LoadingComposable()
                is AddVaccineNoteUiState.Error -> ErrorComposable(uiState.errorMessage)
                AddVaccineNoteUiState.VaccineNotesAddedSuccessfully -> NavRouter.popBackStack()
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

    var reminderEnabled by remember { mutableStateOf(false) }
    var reminderDateMillis by remember { mutableStateOf<Long?>(null) }
    var reminderDateDisplay by remember { mutableStateOf("") }

    val isFormValid = selectedVaccine != null && doctorName.isNotEmpty() && dateTimeStamp.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // --- Vaccine Details Section ---
        SectionHeader(icon = Icons.Outlined.Vaccines, title = stringResource(Res.string.vaccine_details))

        FormSectionCard(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            ExposedDropdownMenuBox(
                expanded = dropDownExpanded,
                onExpandedChange = { dropDownExpanded = it }
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedVaccine?.vaccineName ?: stringResource(Res.string.select_vaccine),
                    onValueChange = {},
                    label = { Text(text = stringResource(Res.string.select_vaccine)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropDownExpanded) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Vaccines,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(),
                    modifier = Modifier.menuAnchor().fillMaxWidth()
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

            DatePickerFieldComposable(
                value = dateTimeStamp,
                label = { Text(stringResource(Res.string.vaccination_date)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIconDescription = stringResource(Res.string.select_date_description),
                onDateSelected = { millis ->
                    val instant = Instant.fromEpochMilliseconds(millis)
                    val localDate = instant.toLocalDateTime(TimeZone.UTC).date
                    val month = localDate.monthNumber.toString().padStart(2, '0')
                    val day = localDate.dayOfMonth.toString().padStart(2, '0')
                    dateTimeStamp = "${localDate.year}-$month-$day"
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- Doctor & Notes Section ---
        SectionHeader(icon = Icons.Outlined.LocalHospital, title = stringResource(Res.string.doctor_and_notes))

        FormSectionCard(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            GenericInputTextFieldComposable(
                textFieldValue = doctorName,
                onValueChange = { doctorName = it },
                label = { Text(stringResource(Res.string.doctor_name)) },
                modifier = Modifier.fillMaxWidth()
            )

            GenericInputTextFieldComposable(
                textFieldValue = note,
                onValueChange = { note = it },
                label = { Text(stringResource(Res.string.notes_optional)) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- Reminder Section ---
        SectionHeader(icon = Icons.Outlined.Notifications, title = stringResource(Res.string.reminder))

        FormSectionCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(Res.string.set_vaccine_reminder),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = stringResource(Res.string.vaccine_reminder_description),
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
                    Spacer(modifier = Modifier.height(8.dp))

                    // Quick preset chips
                    val presetLabels = listOf(
                        stringResource(Res.string.one_month),
                        stringResource(Res.string.three_months),
                        stringResource(Res.string.six_months),
                        stringResource(Res.string.one_year)
                    )
                    val presetMonths = listOf(1, 3, 6, 12)

                    Text(
                        text = stringResource(Res.string.quick_reminder_label),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        presetLabels.forEachIndexed { index, label ->
                            FilterChip(
                                selected = false,
                                onClick = {
                                    val todayMillis = kotlin.time.Clock.System.now().toEpochMilliseconds()
                                    val today = Instant.fromEpochMilliseconds(todayMillis)
                                        .toLocalDateTime(TimeZone.currentSystemDefault()).date
                                    val futureDate = today.plus(DatePeriod(months = presetMonths[index]))
                                    val futureMillis = futureDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
                                    reminderDateMillis = futureMillis
                                    val month = futureDate.monthNumber.toString().padStart(2, '0')
                                    val day = futureDate.dayOfMonth.toString().padStart(2, '0')
                                    reminderDateDisplay = "${futureDate.year}-$month-$day"
                                },
                                label = { Text(label, style = MaterialTheme.typography.labelSmall) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    DatePickerFieldComposable(
                        value = reminderDateDisplay,
                        label = { Text(stringResource(Res.string.reminder_date)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        trailingIconDescription = stringResource(Res.string.select_reminder_date_description),
                        futureOnly = true,
                        initialSelectedDateMillis = reminderDateMillis,
                        onDateSelected = { millis ->
                            reminderDateMillis = millis
                            val instant = Instant.fromEpochMilliseconds(millis)
                            val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
                            val month = localDate.monthNumber.toString().padStart(2, '0')
                            val day = localDate.dayOfMonth.toString().padStart(2, '0')
                            reminderDateDisplay = "${localDate.year}-$month-$day"
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryFormButton(
            text = stringResource(Res.string.add_vaccine_note),
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
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}
