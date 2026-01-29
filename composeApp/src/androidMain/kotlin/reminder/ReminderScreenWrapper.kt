package reminder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.app.reminder.composables.ReminderScreen
import com.app.reminder.dataSource.ReminderLocalDataSource
import com.app.reminder.worker.ReminderScheduler
import kotlinx.coroutines.launch

@Composable
actual fun ReminderScreenWithScheduling(
    onReminderClick: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val localDataSource = ReminderLocalDataSource(context)

    // Callbacks to be invoked after permission result
    var onPermissionGranted by remember { mutableStateOf<(() -> Unit)?>(null) }
    var onPermissionDenied by remember { mutableStateOf<(() -> Unit)?>(null) }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGranted?.invoke()
        } else {
            onPermissionDenied?.invoke()
        }
        // Reset callbacks
        onPermissionGranted = null
        onPermissionDenied = null
    }

    // Check if notification permission is granted
    val isNotificationPermissionGranted: () -> Boolean = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // For Android 12 and below, no runtime permission needed
            true
        }
    }

    // Request notification permission
    val requestNotificationPermission: (() -> Unit, () -> Unit) -> Unit = { onGranted, onDenied ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onPermissionGranted = onGranted
            onPermissionDenied = onDenied
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            // For Android 12 and below, permission is always granted
            onGranted()
        }
    }

    ReminderScreen(
        onReminderClick = onReminderClick,
        onScheduleReminder = { reminder ->
            ReminderScheduler.scheduleReminder(context, reminder)
            scope.launch {
                localDataSource.addReminder(reminder)
            }
        },
        onCancelReminder = { reminderId ->
            ReminderScheduler.cancelReminder(context, reminderId)
            scope.launch {
                localDataSource.deleteReminder(reminderId)
            }
        },
        onRequestNotificationPermission = requestNotificationPermission,
        isNotificationPermissionGranted = isNotificationPermissionGranted
    )
}
