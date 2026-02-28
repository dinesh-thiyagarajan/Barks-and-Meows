package com.app.vaccine.scheduler

actual class VaccineReminderSchedulerProvider {
    actual fun scheduleVaccineReminder(
        vaccineNoteId: String,
        petName: String,
        vaccineName: String,
        reminderTimestamp: Long
    ) {
        // iOS: Use UNUserNotificationCenter to schedule a one-time notification
        // Placeholder for future implementation
    }

    actual fun cancelVaccineReminder(vaccineNoteId: String) {
        // iOS: Use UNUserNotificationCenter to remove pending notification
        // Placeholder for future implementation
    }
}
