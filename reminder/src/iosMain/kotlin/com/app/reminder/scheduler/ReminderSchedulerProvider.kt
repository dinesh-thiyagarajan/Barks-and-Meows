package com.app.reminder.scheduler

import com.app.reminder.dataModels.FeedingReminder

actual class ReminderSchedulerProvider {
    actual fun scheduleReminder(reminder: FeedingReminder) {
        // iOS implementation would use UserNotifications framework
        // This is a placeholder for future iOS implementation
    }

    actual fun cancelReminder(reminderId: String) {
        // iOS implementation would cancel scheduled notifications
        // This is a placeholder for future iOS implementation
    }
}
