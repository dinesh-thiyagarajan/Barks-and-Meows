package reminder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.doctor_prefix
import barksandmeows.composeapp.generated.resources.remove_reminder_description
import barksandmeows.composeapp.generated.resources.reminder_prefix
import barksandmeows.composeapp.generated.resources.unknown
import barksandmeows.composeapp.generated.resources.vaccinated_prefix
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource

@Composable
fun VaccineReminderCardComposable(
    petName: String,
    vaccineName: String,
    reminderTimestamp: Long,
    doctorName: String,
    vaccineDate: String,
    onDeleteClick: () -> Unit
) {
    val reminderDateFormatted = formatTimestamp(reminderTimestamp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Pet Name
                Text(
                    text = petName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Vaccine Name
                Text(
                    text = vaccineName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Reminder Date
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(Res.string.reminder_prefix) + reminderDateFormatted,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Doctor Name
                if (doctorName.isNotEmpty()) {
                    InfoRow(label = stringResource(Res.string.doctor_prefix), value = doctorName)
                    Spacer(modifier = Modifier.height(4.dp))
                }

                if (vaccineDate.isNotEmpty()) {
                    InfoRow(label = stringResource(Res.string.vaccinated_prefix), value = vaccineDate)
                }
            }

            // Delete Button
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(Res.string.remove_reminder_description),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

private fun formatTimestamp(timestamp: Long): String {
    return try {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val month = localDateTime.month.name.lowercase()
            .replaceFirstChar { it.uppercase() }
            .take(3)
        "$month ${localDateTime.dayOfMonth}, ${localDateTime.year}"
    } catch (_: Exception) {
        ""
    }
}
