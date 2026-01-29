package profile

import androidx.compose.runtime.Composable

@Composable
actual fun performLogoutCleanup() {
    // iOS implementation - cancel local notifications if needed
    // Currently no-op as iOS reminder scheduling not yet implemented
}
