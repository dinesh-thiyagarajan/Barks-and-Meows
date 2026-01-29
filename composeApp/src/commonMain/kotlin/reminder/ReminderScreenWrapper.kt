package reminder

import androidx.compose.runtime.Composable

@Composable
expect fun ReminderScreenWithScheduling(
    onReminderClick: (String) -> Unit = {}
)
