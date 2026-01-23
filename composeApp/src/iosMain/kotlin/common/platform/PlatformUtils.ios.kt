package common.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSThread
import kotlin.system.exitProcess

@Composable
actual fun rememberExitApp(): () -> Unit {
    return remember {
        {
            // On iOS, Apple guidelines discourage programmatically exiting apps
            // However, if needed, we can use exit() or NSThread.exit()
            // For a more graceful approach, just do nothing and let the system handle it
            // exitProcess(0) - Use with caution as Apple may reject apps that exit programmatically
        }
    }
}
