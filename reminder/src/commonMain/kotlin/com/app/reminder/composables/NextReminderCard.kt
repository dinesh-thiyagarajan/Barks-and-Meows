package com.app.reminder.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun NextReminderCard(
    lastFedTime: Long?,
    intervalMinutes: Int,
    currentTimeMillis: Long,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val nextReminderInfo = calculateNextReminder(
        lastFedTime = lastFedTime,
        intervalMinutes = intervalMinutes,
        currentTimeMillis = currentTimeMillis,
        isEnabled = isEnabled
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                !isEnabled -> MaterialTheme.colorScheme.surfaceVariant
                nextReminderInfo.isOverdue -> MaterialTheme.colorScheme.errorContainer
                else -> MaterialTheme.colorScheme.tertiaryContainer
            }
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = when {
                    !isEnabled -> MaterialTheme.colorScheme.onSurfaceVariant
                    nextReminderInfo.isOverdue -> MaterialTheme.colorScheme.onErrorContainer
                    else -> MaterialTheme.colorScheme.onTertiaryContainer
                },
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Next Reminder",
                    style = MaterialTheme.typography.labelSmall,
                    color = when {
                        !isEnabled -> MaterialTheme.colorScheme.onSurfaceVariant
                        nextReminderInfo.isOverdue -> MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)
                        else -> MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                    }
                )
                Text(
                    text = nextReminderInfo.displayText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = when {
                        !isEnabled -> MaterialTheme.colorScheme.onSurfaceVariant
                        nextReminderInfo.isOverdue -> MaterialTheme.colorScheme.onErrorContainer
                        else -> MaterialTheme.colorScheme.onTertiaryContainer
                    }
                )
            }
        }
    }
}

private data class NextReminderInfo(
    val displayText: String,
    val isOverdue: Boolean
)

private fun calculateNextReminder(
    lastFedTime: Long?,
    intervalMinutes: Int,
    currentTimeMillis: Long,
    isEnabled: Boolean
): NextReminderInfo {
    if (!isEnabled) {
        return NextReminderInfo("Paused", false)
    }

    if (lastFedTime == null) {
        return NextReminderInfo("Feed now to start", false)
    }

    val intervalMillis = intervalMinutes * 60 * 1000L
    val nextReminderTime = lastFedTime + intervalMillis
    val remainingMillis = nextReminderTime - currentTimeMillis

    return if (remainingMillis <= 0) {
        val overdueMillis = -remainingMillis
        val overdueText = formatDuration(overdueMillis)
        NextReminderInfo("Overdue by $overdueText", true)
    } else {
        val remainingText = formatDuration(remainingMillis)
        NextReminderInfo("in $remainingText", false)
    }
}

private fun formatDuration(millis: Long): String {
    val totalSeconds = millis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return when {
        hours > 0 -> "${hours}h ${minutes}m"
        minutes > 0 -> "${minutes}m ${seconds}s"
        else -> "${seconds}s"
    }
}
