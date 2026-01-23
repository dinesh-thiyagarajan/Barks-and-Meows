package auth

import androidx.compose.runtime.Composable
import com.dineshworkspace.auth.viewModels.AuthViewModel

/**
 * iOS-specific Google Sign-In integration
 * TODO: Implement iOS Google Sign-In using GIDSignIn
 */
@Composable
actual fun GoogleSignInHandler(authViewModel: AuthViewModel): () -> Unit {
    return {
        // TODO: Implement iOS Google Sign-In
        println("Google Sign-In not yet implemented for iOS")
    }
}
