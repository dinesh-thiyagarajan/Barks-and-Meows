package reminder

import androidx.compose.runtime.Composable
import com.app.reminder.composables.ReminderScreen

@Composable
actual fun ReminderScreenWithScheduling(
    onReminderClick: (String) -> Unit
) {
    // iOS implementation - scheduling would use UserNotifications framework
    // For now, just render the screen without scheduling
    ReminderScreen(
        onReminderClick = onReminderClick,
        onScheduleReminder = { /* iOS scheduling to be implemented */ },
        onCancelReminder = { /* iOS cancel to be implemented */ }
    )
}
