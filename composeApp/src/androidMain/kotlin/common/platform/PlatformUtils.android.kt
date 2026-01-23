package common.platform

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberExitApp(): () -> Unit {
    val context = LocalContext.current
    return remember {
        {
            (context as? Activity)?.finish()
        }
    }
}
