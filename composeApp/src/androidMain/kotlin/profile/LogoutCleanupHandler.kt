package profile

import android.content.Context
import com.app.reminder.worker.ReminderScheduler

/**
 * Handles cleanup tasks when user logs out on Android
 */
object LogoutCleanupHandler {

    /**
     * Cancel all scheduled reminders when user logs out
     * This prevents notifications from appearing after logout
     */
    fun cleanup(context: Context) {
        // Cancel all pending WorkManager reminder tasks
        ReminderScheduler.cancelAllReminders(context)
    }
}
