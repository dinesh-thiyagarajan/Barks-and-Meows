package com.app.uicomponents.composables.dateTime

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerComposable(
    state: DatePickerState,
    onDoneButtonClicked: () -> Unit
) {
    DatePicker(
        state = state,
        modifier = Modifier,
        title = {
            Text(
                text = "Select date",
                modifier = Modifier
                    .padding(16.dp),
            )
        },
        headline = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box(Modifier.weight(1f)) {
                    val selectedDateText = state.selectedDateMillis?.let {
                        "date ${it}"
                    } ?: "Select Date"
                    Text(text = selectedDateText)
                }
                Box(Modifier.weight(0.2f)) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Ok",
                        modifier = Modifier.clickable {
                            onDoneButtonClicked.invoke()
                        }
                    )
                }
            }
        },
        colors = DatePickerDefaults.colors()
            .copy(
                containerColor = Color.White,
            )
    )
}
