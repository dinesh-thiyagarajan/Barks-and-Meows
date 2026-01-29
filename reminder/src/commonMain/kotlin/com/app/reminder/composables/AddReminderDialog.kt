package com.app.reminder.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import barksandmeows.reminder.generated.resources.Res
import barksandmeows.reminder.generated.resources.add_feeding_reminder
import barksandmeows.reminder.generated.resources.add_reminder
import barksandmeows.reminder.generated.resources.cancel
import barksandmeows.reminder.generated.resources.every
import barksandmeows.reminder.generated.resources.hours
import barksandmeows.reminder.generated.resources.minutes
import barksandmeows.reminder.generated.resources.no_pets_found
import barksandmeows.reminder.generated.resources.reminder_interval
import barksandmeows.reminder.generated.resources.reminder_preview
import barksandmeows.reminder.generated.resources.select_pet
import com.app.dataModels.Pet
import com.app.reminder.dataModels.IntervalUnit
import com.app.reminder.dataModels.ReminderInterval
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderDialog(
    pets: List<Pet>,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onAddReminder: (Pet, Int) -> Unit
) {
    var selectedPet by remember { mutableStateOf<Pet?>(null) }
    var petExpanded by remember { mutableStateOf(false) }
    var selectedUnit by remember { mutableStateOf(IntervalUnit.HOURS) }
    var selectedInterval by remember { mutableStateOf(ReminderInterval.EVERY_8_HOURS) }
    var intervalExpanded by remember { mutableStateOf(false) }

    val intervalOptions = if (selectedUnit == IntervalUnit.MINUTES) {
        ReminderInterval.minuteOptions
    } else {
        ReminderInterval.hourOptions
    }

    val validInterval = if (intervalOptions.contains(selectedInterval)) {
        selectedInterval
    } else {
        intervalOptions.first()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(Res.string.add_feeding_reminder),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(Res.string.select_pet),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (pets.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.no_pets_found),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    ExposedDropdownMenuBox(
                        expanded = petExpanded,
                        onExpandedChange = { petExpanded = !petExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedPet?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            placeholder = { Text(stringResource(Res.string.select_pet)) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Pets,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = petExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = petExpanded,
                            onDismissRequest = { petExpanded = false }
                        ) {
                            pets.forEach { pet ->
                                DropdownMenuItem(
                                    text = {
                                        Row {
                                            Icon(
                                                imageVector = Icons.Default.Pets,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = pet.name,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedPet = pet
                                        petExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.reminder_interval),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedUnit == IntervalUnit.MINUTES,
                        onClick = {
                            selectedUnit = IntervalUnit.MINUTES
                            selectedInterval = ReminderInterval.EVERY_15_MINUTES
                        },
                        label = { Text(stringResource(Res.string.minutes)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = selectedUnit == IntervalUnit.HOURS,
                        onClick = {
                            selectedUnit = IntervalUnit.HOURS
                            selectedInterval = ReminderInterval.EVERY_1_HOUR
                        },
                        label = { Text(stringResource(Res.string.hours)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = intervalExpanded,
                    onExpandedChange = { intervalExpanded = !intervalExpanded }
                ) {
                    OutlinedTextField(
                        value = validInterval.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(Res.string.every)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = intervalExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = intervalExpanded,
                        onDismissRequest = { intervalExpanded = false }
                    ) {
                        intervalOptions.forEach { interval ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = interval.displayName,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                },
                                onClick = {
                                    selectedInterval = interval
                                    intervalExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(
                        Res.string.reminder_preview,
                        selectedPet?.name ?: "your pet",
                        validInterval.displayName
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    selectedPet?.let { pet ->
                        onAddReminder(pet, validInterval.minutes)
                    }
                },
                enabled = selectedPet != null && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(24.dp).height(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(stringResource(Res.string.add_reminder))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.cancel))
            }
        }
    )
}
