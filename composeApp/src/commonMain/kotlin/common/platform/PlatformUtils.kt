package common.platform

import androidx.compose.runtime.Composable

/**
 * Provides a platform-specific function to exit the application.
 * Returns a lambda that when invoked will exit the app.
 */
@Composable
expect fun rememberExitApp(): () -> Unit
