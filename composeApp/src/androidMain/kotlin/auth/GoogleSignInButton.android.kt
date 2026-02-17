package auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.app.auth.platform.rememberGoogleSignInHelper
import com.app.auth.platform.rememberGoogleSignInLauncher
import com.app.auth.viewModels.AuthViewModel
import com.dineshworkspace.auth.BuildConfig
import kotlinx.coroutines.launch

@Composable
actual fun GoogleSignInHandler(authViewModel: AuthViewModel): () -> Unit {
    val coroutineScope = rememberCoroutineScope()
    val googleSignInHelper = rememberGoogleSignInHelper(webClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID)

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
