package auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.app.auth.platform.rememberGoogleSignInHelper
import com.app.auth.platform.rememberGoogleSignInLauncher
import com.app.auth.viewModels.AuthViewModel
import kotlinx.coroutines.launch

/**
 * Android-specific Google Sign-In integration
 * TODO: Replace "YOUR_WEB_CLIENT_ID" with your actual Firebase Web Client ID
 * You can find this in your Firebase Console -> Project Settings -> General -> Web API Key
 */
private const val WEB_CLIENT_ID = "YOUR_WEB_CLIENT_ID"

@Composable
actual fun GoogleSignInHandler(authViewModel: AuthViewModel): () -> Unit {
    val coroutineScope = rememberCoroutineScope()
    val googleSignInHelper = rememberGoogleSignInHelper(webClientId = WEB_CLIENT_ID)

    val launcher = rememberGoogleSignInLauncher(
        googleSignInHelper = googleSignInHelper,
        onResult = { idToken ->
            coroutineScope.launch {
                authViewModel.loginWithGoogle(idToken)
            }
        }
    )

    return {
        launcher.launch(googleSignInHelper.getSignInIntent())
    }
}
