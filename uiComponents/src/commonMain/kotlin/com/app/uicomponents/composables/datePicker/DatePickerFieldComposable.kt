package com.app.uicomponents.composables.datePicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerFieldComposable(
    value: String,
    label: @Composable () -> Unit,
    leadingIcon: @Composable () -> Unit,
    trailingIconDescription: String,
    onDateSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
    initialSelectedDateMillis: Long? = null,
    futureOnly: Boolean = false
) {
    val selectableDates = if (futureOnly) {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis > System.currentTimeMillis()
            }
        }
    } else {
        object : SelectableDates {}
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDateMillis,
        selectableDates = selectableDates
    )
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                        showDialog = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { showDialog = true }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            label = label,
            readOnly = true,
            enabled = false,
            singleLine = true,
            leadingIcon = leadingIcon,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = trailingIconDescription,
                    modifier = Modifier.size(20.dp),
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
