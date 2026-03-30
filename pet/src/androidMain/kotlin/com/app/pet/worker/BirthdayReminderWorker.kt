package com.app.pet.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BirthdayReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val petName = inputData.getString(KEY_PET_NAME) ?: "your pet"
        val petId = inputData.getString(KEY_PET_ID) ?: return@withContext Result.failure()
        val birthDate = inputData.getString(KEY_BIRTH_DATE) ?: return@withContext Result.failure()

        showNotification(petId, petName)
        BirthdayReminderScheduler.scheduleBirthday(context, petId, petName, birthDate)

        Result.success()
    }

    private fun showNotification(petId: String, petName: String) {
        createNotificationChannel()

        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EXTRA_FROM_BIRTHDAY, true)
            putExtra(EXTRA_PET_NAME, petName)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            petId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle("Happy Birthday!")
            .setContentText("Today is $petName's birthday!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                NotificationManagerCompat.from(context).notify(petId.hashCode(), notification)
            }
        } else {
            NotificationManagerCompat.from(context).notify(petId.hashCode(), notification)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Birthday Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Birthday notifications for your pets"
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "birthday_reminder_channel"
        const val KEY_PET_NAME = "pet_name"
        const val KEY_PET_ID = "pet_id"
        const val KEY_BIRTH_DATE = "birth_date"
        const val WORK_NAME_PREFIX = "birthday_reminder_"
        const val EXTRA_FROM_BIRTHDAY = "from_birthday_notification"
        const val EXTRA_PET_NAME = "birthday_pet_name"
    }
}
