package com.app.vaccine.worker

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object VaccineReminderScheduler {

    fun scheduleReminder(
        context: Context,
        vaccineNoteId: String,
        petName: String,
        vaccineName: String,
        reminderTimestamp: Long
    ) {
        val delayMillis = reminderTimestamp - System.currentTimeMillis()
        if (delayMillis <= 0) return

        val inputData = Data.Builder()
            .putString(VaccineReminderWorker.KEY_VACCINE_NOTE_ID, vaccineNoteId)
            .putString(VaccineReminderWorker.KEY_PET_NAME, petName)
            .putString(VaccineReminderWorker.KEY_VACCINE_NAME, vaccineName)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<VaccineReminderWorker>()
            .setInputData(inputData)
            .addTag(vaccineNoteId)
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "${VaccineReminderWorker.WORK_NAME_PREFIX}$vaccineNoteId",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun cancelReminder(context: Context, vaccineNoteId: String) {
        WorkManager.getInstance(context)
            .cancelUniqueWork("${VaccineReminderWorker.WORK_NAME_PREFIX}$vaccineNoteId")
    }
}
