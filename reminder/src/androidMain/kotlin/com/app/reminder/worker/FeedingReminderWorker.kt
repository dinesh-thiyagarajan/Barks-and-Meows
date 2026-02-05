package com.app.reminder.worker

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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FeedingReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val petName = inputData.getString(KEY_PET_NAME) ?: "Your pet"
        val reminderId = inputData.getString(KEY_REMINDER_ID) ?: ""
        val intervalMinutes = inputData.getInt(KEY_INTERVAL_MINUTES, 60)

        // Show the notification
        showNotification(petName, reminderId)

        // Schedule the next notification (chaining)
        if (reminderId.isNotEmpty()) {
            ReminderScheduler.scheduleNextNotification(
                context = context,
                reminderId = reminderId,
                petName = petName,
                intervalMinutes = intervalMinutes,
                delayMinutes = intervalMinutes.toLong()
            )
        }

        Result.success()
    }

    private fun showNotification(petName: String, reminderId: String) {
        createNotificationChannel()

        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EXTRA_REMINDER_ID, reminderId)
            putExtra(EXTRA_FROM_NOTIFICATION, true)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            reminderId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Format current time for the notification
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val currentTime = timeFormat.format(Date())

        // Use the same notification ID to update existing notification
        val notificationId = reminderId.hashCode()

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle("Time to feed $petName! 🐾")
            .setContentText("Your furry friend $petName is waiting for food")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Your furry friend $petName is waiting for food.\nReminder sent for $currentTime")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(false) // Alert every time
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
            val name = "Feeding Reminders"
            val descriptionText = "Notifications to remind you to feed your pets"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "feeding_reminder_channel"
        const val KEY_PET_NAME = "pet_name"
        const val KEY_REMINDER_ID = "reminder_id"
        const val KEY_INTERVAL_MINUTES = "interval_minutes"
        const val EXTRA_REMINDER_ID = "extra_reminder_id"
        const val EXTRA_FROM_NOTIFICATION = "extra_from_notification"
        const val WORK_NAME_PREFIX = "feeding_reminder_"
    }
}
