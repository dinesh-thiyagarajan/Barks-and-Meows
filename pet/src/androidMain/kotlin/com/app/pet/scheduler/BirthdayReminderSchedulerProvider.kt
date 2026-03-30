package com.app.pet.scheduler

import android.content.Context
import com.app.pet.worker.BirthdayReminderScheduler

actual class BirthdayReminderSchedulerProvider(
    private val context: Context
) {
    actual fun scheduleBirthday(petId: String, petName: String, birthDate: String) {
        BirthdayReminderScheduler.scheduleBirthday(context, petId, petName, birthDate)
    }

    actual fun cancelBirthday(petId: String) {
        BirthdayReminderScheduler.cancelBirthday(context, petId)
    }
}
