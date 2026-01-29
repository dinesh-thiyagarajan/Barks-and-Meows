package com.app.reminder.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import barksandmeows.reminder.generated.resources.reminder_interval
import barksandmeows.reminder.generated.resources.reminder_preview
import barksandmeows.reminder.generated.resources.select_pet
import com.app.dataModels.Pet
import com.app.reminder.dataModels.IntervalUnit
import com.app.reminder.dataModels.ReminderInterval
import org.jetbrains.compose.resources.stringResource

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

                PetSelector(
                    pets = pets,
                    selectedPet = selectedPet,
                    expanded = petExpanded,
                    onExpandedChange = { petExpanded = it },
                    onPetSelected = { selectedPet = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.reminder_interval),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                IntervalSelector(
                    selectedUnit = selectedUnit,
                    selectedInterval = selectedInterval,
                    intervalExpanded = intervalExpanded,
                    onUnitChange = { unit ->
                        selectedUnit = unit
                        selectedInterval = if (unit == IntervalUnit.MINUTES) {
                            ReminderInterval.EVERY_15_MINUTES
                        } else {
                            ReminderInterval.EVERY_1_HOUR
                        }
                    },
                    onIntervalChange = { selectedInterval = it },
                    onExpandedChange = { intervalExpanded = it }
                )

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
