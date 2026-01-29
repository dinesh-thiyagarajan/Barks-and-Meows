package com.app.reminder.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.app.reminder.dataModels.FeedingReminder
import java.util.concurrent.TimeUnit

object ReminderScheduler {

    fun scheduleReminder(context: Context, reminder: FeedingReminder) {
        if (!reminder.isEnabled) {
            cancelReminder(context, reminder.id)
            return
        }

        // Schedule the first notification with initial delay
        scheduleNextNotification(
            context = context,
            reminderId = reminder.id,
            petName = reminder.petName,
            intervalMinutes = reminder.intervalMinutes,
            delayMinutes = reminder.intervalMinutes.toLong()
        )
    }

    /**
     * Schedules the next notification using OneTimeWorkRequest.
     * This is called both when setting up a new reminder and after showing a notification.
     */
    fun scheduleNextNotification(
        context: Context,
        reminderId: String,
        petName: String,
        intervalMinutes: Int,
        delayMinutes: Long
    ) {
        val inputData = Data.Builder()
            .putString(FeedingReminderWorker.KEY_PET_NAME, petName)
            .putString(FeedingReminderWorker.KEY_REMINDER_ID, reminderId)
            .putInt(FeedingReminderWorker.KEY_INTERVAL_MINUTES, intervalMinutes)
            .build()

        // Configure constraints to ensure work runs even when app is closed
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(false)  // Run even on low battery
            .setRequiresCharging(false)        // Run even when not charging
            .setRequiresDeviceIdle(false)      // Run even when device is active
            .build()

        val workRequest = OneTimeWorkRequestBuilder<FeedingReminderWorker>()
            .setInputData(inputData)
            .setConstraints(constraints)
            .addTag(reminderId)
            .setInitialDelay(delayMinutes, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "${FeedingReminderWorker.WORK_NAME_PREFIX}${reminderId}",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun cancelReminder(context: Context, reminderId: String) {
        WorkManager.getInstance(context)
            .cancelUniqueWork("${FeedingReminderWorker.WORK_NAME_PREFIX}$reminderId")
    }

    fun cancelAllReminders(context: Context) {
        WorkManager.getInstance(context).cancelAllWork()
    }
}
