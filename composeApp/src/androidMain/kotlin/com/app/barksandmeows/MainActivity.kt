package com.app.barksandmeows

import BarksAndMeowsApp
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.app.pet.worker.BirthdayReminderWorker
import com.app.reminder.worker.FeedingReminderWorker
import navigation.AppRouteActions
import navigation.NavConstants
import navigation.NavRouter

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Permission granted or denied - app will work either way
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        createNotificationChannels()
        requestNotificationPermission()
        setContent {
            BarksAndMeowsApp()
        }
        handleNotificationIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun handleNotificationIntent(intent: Intent?) {
        val fromBirthday = intent?.getBooleanExtra(BirthdayReminderWorker.EXTRA_FROM_BIRTHDAY, false) ?: false
        val fromFeeding = intent?.getBooleanExtra(FeedingReminderWorker.EXTRA_FROM_NOTIFICATION, false) ?: false

        when {
            fromBirthday -> {
                val petName = intent?.getStringExtra(BirthdayReminderWorker.EXTRA_PET_NAME) ?: ""
                window.decorView.postDelayed({
                    try {
                        NavRouter.navigate("${AppRouteActions.HappyBirthdayScreen.route}$petName")
                    } catch (ignored: Exception) {}
                }, 500)
            }
            fromFeeding -> {
                window.decorView.postDelayed({
                    try {
                        NavRouter.navigate(AppRouteActions.ReminderScreen.route)
                    } catch (ignored: Exception) {}
                }, 500)
            }
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            NotificationChannel(
                FeedingReminderWorker.CHANNEL_ID,
                "Feeding Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications to remind you to feed your pets"
                notificationManager.createNotificationChannel(this)
            }

            NotificationChannel(
                BirthdayReminderWorker.CHANNEL_ID,
                "Birthday Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Birthday notifications for your pets"
                notificationManager.createNotificationChannel(this)
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    BarksAndMeowsApp()
}
