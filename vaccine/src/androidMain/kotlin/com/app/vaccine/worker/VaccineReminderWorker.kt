package com.app.vaccine.worker

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

class VaccineReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val petName = inputData.getString(KEY_PET_NAME) ?: "Your pet"
        val vaccineName = inputData.getString(KEY_VACCINE_NAME) ?: "vaccine"
        val vaccineNoteId = inputData.getString(KEY_VACCINE_NOTE_ID) ?: ""

        showNotification(petName, vaccineName, vaccineNoteId)

        Result.success()
    }

    private fun showNotification(petName: String, vaccineName: String, vaccineNoteId: String) {
        createNotificationChannel()

        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            vaccineNoteId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationId = vaccineNoteId.hashCode()

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle("Vaccine Reminder for $petName")
            .setContentText("Time for $petName's $vaccineName appointment")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Don't forget: $petName is due for $vaccineName. Schedule an appointment with your veterinarian.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                NotificationManagerCompat.from(context).notify(notificationId, notification)
            }
        } else {
            NotificationManagerCompat.from(context).notify(notificationId, notification)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Vaccine Reminders"
            val descriptionText = "Notifications to remind you about pet vaccinations"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "vaccine_reminder_channel"
        const val KEY_PET_NAME = "pet_name"
        const val KEY_VACCINE_NAME = "vaccine_name"
        const val KEY_VACCINE_NOTE_ID = "vaccine_note_id"
        const val WORK_NAME_PREFIX = "vaccine_reminder_"
    }
}
