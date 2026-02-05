package com.app.reminder.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.app.reminder.dataModels.FeedingReminder

@Composable
fun DeleteReminderConfirmDialog(
    reminder: FeedingReminder?,
    onConfirm: (FeedingReminder) -> Unit,
    onDismiss: () -> Unit
) {
    if (reminder != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Delete Reminder") },
            text = {
                Text(
                    "Are you sure you want to delete the feeding reminder for ${reminder.petName}? This action cannot be undone."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { onConfirm(reminder) }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}
