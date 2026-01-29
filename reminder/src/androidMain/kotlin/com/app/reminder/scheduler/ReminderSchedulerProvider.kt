package com.app.reminder.scheduler

import android.content.Context
import com.app.reminder.dataModels.FeedingReminder
import com.app.reminder.worker.ReminderScheduler

actual class ReminderSchedulerProvider(
    private val context: Context
) {
    actual fun scheduleReminder(reminder: FeedingReminder) {
        ReminderScheduler.scheduleReminder(context, reminder)
    }

    actual fun cancelReminder(reminderId: String) {
        ReminderScheduler.cancelReminder(context, reminderId)
    }

    actual fun cancelAllReminders() {
        ReminderScheduler.cancelAllReminders(context)
    }
}
