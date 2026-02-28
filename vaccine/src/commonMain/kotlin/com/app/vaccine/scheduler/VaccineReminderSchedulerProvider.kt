package com.app.vaccine.scheduler

expect class VaccineReminderSchedulerProvider {
    fun scheduleVaccineReminder(
        vaccineNoteId: String,
        petName: String,
        vaccineName: String,
        reminderTimestamp: Long
    )

    fun cancelVaccineReminder(vaccineNoteId: String)
}
