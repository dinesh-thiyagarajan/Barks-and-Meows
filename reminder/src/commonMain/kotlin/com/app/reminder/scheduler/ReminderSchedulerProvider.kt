package com.app.reminder.scheduler

import com.app.reminder.dataModels.FeedingReminder

expect class ReminderSchedulerProvider {
    fun scheduleReminder(reminder: FeedingReminder)
    fun cancelReminder(reminderId: String)
    fun cancelAllReminders()
}
