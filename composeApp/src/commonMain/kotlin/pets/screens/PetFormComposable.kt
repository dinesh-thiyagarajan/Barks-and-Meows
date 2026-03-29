package pets.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.pet_birth_year
import barksandmeows.composeapp.generated.resources.pet_details
import barksandmeows.composeapp.generated.resources.pet_name
import barksandmeows.composeapp.generated.resources.pet_type
import com.app.dataModels.PetCategory
import com.app.uicomponents.composables.chips.CategorySelectorChip
import com.app.uicomponents.composables.section.SectionHeader
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetFormContent(
    petCategories: List<PetCategory>,
    petName: String,
    onPetNameChange: (String) -> Unit,
    selectedBirthYear: Int?,
    onBirthYearSelected: (Int) -> Unit,
    onCategorySelected: (Int) -> Unit
) {
    val currentYear = remember {
        Instant.fromEpochMilliseconds(kotlin.time.Clock.System.now().toEpochMilliseconds())
            .toLocalDateTime(TimeZone.currentSystemDefault()).year
    }

    val initialDateMillis: Long? = remember {
        selectedBirthYear?.let { year ->
            LocalDate(year, 1, 1).atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
        }
    }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDateMillis)
    var showYearPicker by remember { mutableStateOf(false) }

    val ageDisplayText = selectedBirthYear?.let { year ->
        val age = currentYear - year
        "$year  (${if (age == 1) "1 year" else "$age years"} old)"
    } ?: ""

    if (showYearPicker) {
        DatePickerDialog(
            onDismissRequest = { showYearPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val instant = Instant.fromEpochMilliseconds(millis)
                            val year = instant.toLocalDateTime(TimeZone.UTC).year
                            onBirthYearSelected(year)
                        }
                        showYearPicker = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showYearPicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // --- Pet Type Section ---
    SectionHeader(icon = Icons.Outlined.Category, title = stringResource(Res.string.pet_type))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(petCategories.size) { index ->
                    CategorySelectorChip(
                        label = petCategories[index].category,
                        categoryId = petCategories[index].id,
                        selected = petCategories[index].selected,
                        drawableResource = petCategories[index].drawableResource,
                        onChipSelected = onCategorySelected
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // --- Pet Details Section ---
    SectionHeader(icon = Icons.Outlined.Pets, title = stringResource(Res.string.pet_details))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            OutlinedTextField(
                value = petName,
                onValueChange = onPetNameChange,
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showYearPicker = true }
            ) {
                OutlinedTextField(
                    value = ageDisplayText,
                    onValueChange = {},
                    label = { Text(stringResource(Res.string.pet_birth_year)) },
                    readOnly = true,
                    enabled = false,
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Cake,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.CalendarMonth,
                            contentDescription = "Select birth year",
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
