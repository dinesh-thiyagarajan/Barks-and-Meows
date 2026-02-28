package reminder

import androidx.compose.runtime.Composable

@Composable
actual fun ReminderScreenWithScheduling(
    onReminderClick: (String) -> Unit
) {
    // iOS implementation - scheduling would use UserNotifications framework
    // For now, just render the screen without scheduling
    CombinedReminderScreen(
        onReminderClick = onReminderClick,
        onScheduleReminder = { /* iOS scheduling to be implemented */ },
        onCancelReminder = { /* iOS cancel to be implemented */ }
    )
}
