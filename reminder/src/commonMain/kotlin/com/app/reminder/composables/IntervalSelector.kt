package com.app.reminder.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import barksandmeows.reminder.generated.resources.Res
import barksandmeows.reminder.generated.resources.every
import barksandmeows.reminder.generated.resources.hours
import barksandmeows.reminder.generated.resources.minutes
import com.app.reminder.dataModels.IntervalUnit
import com.app.reminder.dataModels.ReminderInterval
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntervalSelector(
    selectedUnit: IntervalUnit,
    selectedInterval: ReminderInterval,
    intervalExpanded: Boolean,
    onUnitChange: (IntervalUnit) -> Unit,
    onIntervalChange: (ReminderInterval) -> Unit,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
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

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedUnit == IntervalUnit.MINUTES,
                onClick = { onUnitChange(IntervalUnit.MINUTES) },
                label = { Text(stringResource(Res.string.minutes)) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = selectedUnit == IntervalUnit.HOURS,
                onClick = { onUnitChange(IntervalUnit.HOURS) },
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
            onExpandedChange = onExpandedChange
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
                onDismissRequest = { onExpandedChange(false) }
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
                            onIntervalChange(interval)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}
