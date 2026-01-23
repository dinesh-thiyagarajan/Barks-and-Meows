package vaccine

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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
import navigation.NavRouter
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AddVaccineNoteScreen(
    petId: String,
    vaccineNoteViewModel: VaccineNoteViewModel = koinViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(vaccineNoteViewModel) {
        vaccineNoteViewModel.getVaccinesList()
    }

    Scaffold {
        val getVaccineNoteUiState = vaccineNoteViewModel.addVaccineNoteUiState.collectAsState()

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
                    vaccineNoteViewModel = vaccineNoteViewModel,
                    coroutineScope = coroutineScope,
                    vaccines = uiState.vaccineList
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddNewVaccineNoteComposable(
    petId: String,
    vaccineNoteViewModel: VaccineNoteViewModel,
    coroutineScope: CoroutineScope,
    vaccines: List<Vaccine>
) {
    var dateTimeStamp by remember { mutableStateOf("") }
    var doctorName by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var dropDownExpanded by remember { mutableStateOf(false) }
    var selectedVaccine: Vaccine? by remember { mutableStateOf(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

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
                            dateTimeStamp = "${localDate.year}-${localDate.monthNumber.toString().padStart(2, '0')}-${localDate.dayOfMonth.toString().padStart(2, '0')}"
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

    Column(modifier = Modifier.padding(all = 10.dp)) {
        GenericInputTextFieldComposable(
            textFieldValue = doctorName,
            onValueChange = { doctorName = it },
            label = { Text(stringResource(Res.string.doctor_name)) },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true }
        ) {
            OutlinedTextField(
                value = dateTimeStamp,
                onValueChange = {},
                label = { Text("Date") },
                readOnly = true,
                enabled = false,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = dropDownExpanded,
            onExpandedChange = {
                dropDownExpanded = it
            },
            content = {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedVaccine?.vaccineName
                        ?: stringResource(Res.string.select_vaccine),
                    onValueChange = {},
                    label = { Text(text = stringResource(Res.string.select_vaccine)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropDownExpanded)
                    },
                    colors = OutlinedTextFieldDefaults.colors(),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = dropDownExpanded,
                    onDismissRequest = { dropDownExpanded = false }) {
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
        )

        Spacer(modifier = Modifier.height(16.dp))

        GenericInputTextFieldComposable(
            textFieldValue = note,
            onValueChange = { note = it },
            label = { Text("Notes") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            shape = RoundedCornerShape(4.dp),
            onClick = {
                coroutineScope.launch {
                    vaccineNoteViewModel.addVaccineNote(
                        vaccineNote = VaccineNote(
                            id = generateUUID(),
                            petId = petId,
                            vaccine = selectedVaccine
                                ?: return@launch, // Ensure vaccine is selected
                            note = note,
                            timestamp = dateTimeStamp,
                            doctorName = doctorName
                        )
                    )
                }
            },
            enabled = selectedVaccine != null && doctorName.isNotEmpty() && dateTimeStamp.isNotEmpty(),
            modifier = Modifier.wrapContentSize().align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(Res.string.add_vaccine_note))
        }
    }
}