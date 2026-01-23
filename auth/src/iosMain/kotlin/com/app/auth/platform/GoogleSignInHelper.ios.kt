package com.app.auth.platform

/**
 * iOS implementation of GoogleSignInHelper
 *
 * Note: For full iOS Google Sign-In support, you'll need to:
 * 1. Add GoogleSignIn pod to your Podfile
 * 2. Configure URL schemes in Info.plist
 * 3. Add REVERSED_CLIENT_ID to Info.plist
 * 4. Implement native sign-in flow
 */
actual class GoogleSignInHelper {
    actual suspend fun signIn(): String? {
        // TODO: Implement iOS Google Sign-In
        // This would typically use GIDSignIn from the Google Sign-In iOS SDK
        return null
    }
}
