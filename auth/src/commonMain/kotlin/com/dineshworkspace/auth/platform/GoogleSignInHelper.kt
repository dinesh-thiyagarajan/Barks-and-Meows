package com.dineshworkspace.auth.platform

/**
 * Platform-specific Google Sign-In helper
 * Returns the ID token on success, or null on failure/cancellation
 */
expect class GoogleSignInHelper {
    suspend fun signIn(): String?
}
