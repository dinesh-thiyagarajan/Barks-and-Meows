package com.app.vaccine.scheduler

import android.content.Context
import com.app.vaccine.worker.VaccineReminderScheduler

actual class VaccineReminderSchedulerProvider(
    private val context: Context
) {
    actual fun scheduleVaccineReminder(
        vaccineNoteId: String,
        petName: String,
        vaccineName: String,
        reminderTimestamp: Long
    ) {
        VaccineReminderScheduler.scheduleReminder(
            context, vaccineNoteId, petName, vaccineName, reminderTimestamp
        )
    }

    actual fun cancelVaccineReminder(vaccineNoteId: String) {
        VaccineReminderScheduler.cancelReminder(context, vaccineNoteId)
    }
}
