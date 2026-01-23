package com.app.auth.platform

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual class GoogleSignInHelper(private val context: Context, private val webClientId: String) {

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    actual suspend fun signIn(): String? = suspendCancellableCoroutine { continuation ->
        val signInIntent = googleSignInClient.signInIntent
        // Note: This is a simplified version. In production, you'd use a proper activity result launcher
        continuation.resume(null) // Placeholder - actual implementation needs activity result handling
    }

    fun getSignInIntent(): Intent = googleSignInClient.signInIntent

    fun handleSignInResult(task: Task<GoogleSignInAccount>): String? {
        return try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken
        } catch (e: ApiException) {
            null
        }
    }
}

@Composable
fun rememberGoogleSignInHelper(webClientId: String): GoogleSignInHelper {
    val context = LocalContext.current
    return remember { GoogleSignInHelper(context, webClientId) }
}

@Composable
fun rememberGoogleSignInLauncher(
    googleSignInHelper: GoogleSignInHelper,
    onResult: (String?) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        val idToken = googleSignInHelper.handleSignInResult(task)
        onResult(idToken)
    }
}
