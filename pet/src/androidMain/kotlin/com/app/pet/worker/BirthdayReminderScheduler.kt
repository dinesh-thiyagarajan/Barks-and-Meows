package com.app.pet.worker

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

object BirthdayReminderScheduler {

    fun scheduleBirthday(context: Context, petId: String, petName: String, birthDate: String) {
        val parts = birthDate.split("-")
        if (parts.size != 3) return
        val month = parts[1].toIntOrNull() ?: return
        val day = parts[2].toIntOrNull() ?: return

        val now = Calendar.getInstance()
        val nextBirthday = Calendar.getInstance().apply {
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (!after(now)) {
                add(Calendar.YEAR, 1)
            }
        }

        val delayMillis = nextBirthday.timeInMillis - System.currentTimeMillis()
        if (delayMillis <= 0) return

        val inputData = Data.Builder()
            .putString(BirthdayReminderWorker.KEY_PET_ID, petId)
            .putString(BirthdayReminderWorker.KEY_PET_NAME, petName)
            .putString(BirthdayReminderWorker.KEY_BIRTH_DATE, birthDate)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<BirthdayReminderWorker>()
            .setInputData(inputData)
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "${BirthdayReminderWorker.WORK_NAME_PREFIX}$petId",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun cancelBirthday(context: Context, petId: String) {
        WorkManager.getInstance(context)
            .cancelUniqueWork("${BirthdayReminderWorker.WORK_NAME_PREFIX}$petId")
    }
}
