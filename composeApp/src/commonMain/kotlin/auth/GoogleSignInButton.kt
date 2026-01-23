package auth

import androidx.compose.runtime.Composable
import com.app.auth.viewModels.AuthViewModel

/**
 * Platform-specific Google Sign-In handler
 * Returns a callback function that triggers Google Sign-In flow
 */
@Composable
expect fun GoogleSignInHandler(authViewModel: AuthViewModel): () -> Unit
