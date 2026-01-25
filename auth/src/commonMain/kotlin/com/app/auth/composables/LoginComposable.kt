package com.app.auth.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import barksandmeows.auth.generated.resources.Res
import barksandmeows.auth.generated.resources.cancel
import barksandmeows.auth.generated.resources.email
import barksandmeows.auth.generated.resources.forgot_password
import barksandmeows.auth.generated.resources.forgot_password_description
import barksandmeows.auth.generated.resources.forgot_password_success
import barksandmeows.auth.generated.resources.ic_app_logo
import barksandmeows.auth.generated.resources.login
import barksandmeows.auth.generated.resources.ok
import barksandmeows.auth.generated.resources.password
import barksandmeows.auth.generated.resources.send_reset_email
import com.app.auth.viewModels.AuthViewModel
import com.app.auth.viewModels.ForgotPasswordState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginComposable(
    coroutineScope: CoroutineScope,
    authViewModel: AuthViewModel,
    errorMessage: String? = null,
    onGoogleSignInClick: () -> Unit = {},
    onSignUpClicked: () -> Unit = {},
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var forgotPasswordEmail by remember { mutableStateOf("") }
    val forgotPasswordState by authViewModel.forgotPasswordState.collectAsState()

    // Forgot Password Dialog
    if (showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = {
                if (forgotPasswordState !is ForgotPasswordState.Sending) {
                    showForgotPasswordDialog = false
                    authViewModel.resetForgotPasswordState()
                    forgotPasswordEmail = ""
                }
            },
            title = { Text(stringResource(Res.string.forgot_password)) },
            text = {
                Column {
                    when (forgotPasswordState) {
                        is ForgotPasswordState.Idle -> {
                            Text(stringResource(Res.string.forgot_password_description))
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = forgotPasswordEmail,
                                onValueChange = { forgotPasswordEmail = it },
                                label = { Text(stringResource(Res.string.email)) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Done
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        is ForgotPasswordState.Sending -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is ForgotPasswordState.Sent -> {
                            Text(stringResource(Res.string.forgot_password_success))
                        }

                        is ForgotPasswordState.Error -> {
                            Text(
                                text = (forgotPasswordState as ForgotPasswordState.Error).message,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            },
            confirmButton = {
                when (forgotPasswordState) {
                    is ForgotPasswordState.Idle -> {
                        TextButton(
                            onClick = {
                                if (forgotPasswordEmail.isNotEmpty()) {
                                    authViewModel.sendPasswordResetEmail(forgotPasswordEmail)
                                }
                            },
                            enabled = forgotPasswordEmail.isNotEmpty()
                        ) {
                            Text(stringResource(Res.string.send_reset_email))
                        }
                    }

                    is ForgotPasswordState.Sent, is ForgotPasswordState.Error -> {
                        TextButton(
                            onClick = {
                                showForgotPasswordDialog = false
                                authViewModel.resetForgotPasswordState()
                                forgotPasswordEmail = ""
                            }
                        ) {
                            Text(stringResource(Res.string.ok))
                        }
                    }

                    else -> {}
                }
            },
            dismissButton = {
                if (forgotPasswordState is ForgotPasswordState.Idle) {
                    TextButton(
                        onClick = {
                            showForgotPasswordDialog = false
                            forgotPasswordEmail = ""
                        }
                    ) {
                        Text(stringResource(Res.string.cancel))
                    }
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painterResource(Res.drawable.ic_app_logo),
            null,
            modifier = Modifier.size(100.dp)
        )

        OutlinedTextField(
            value = email,
            singleLine = true,
            onValueChange = { email = it },
            label = { Text(stringResource(Res.string.email)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            singleLine = true,
            onValueChange = { password = it },
            label = { Text(stringResource(Res.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        TextButton(
            onClick = { showForgotPasswordDialog = true },
            modifier = Modifier.align(Alignment.End).padding(end = 32.dp)
        ) {
            Text(
                stringResource(Res.string.forgot_password),
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    authViewModel.loginWithUserNamePassword(email = email, password = password)
                }
            },
            enabled = email.isNotEmpty() && password.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(stringResource(Res.string.login))
        }

        // Disabling Google sign in for now
        //Spacer(modifier = Modifier.height(16.dp))

//        Row(
//            modifier = Modifier.fillMaxWidth(0.8f),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            HorizontalDivider(modifier = Modifier.weight(1f))
//            Text(
//                text = "  OR  ",
//                style = MaterialTheme.typography.bodySmall,
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            )
//            HorizontalDivider(modifier = Modifier.weight(1f))
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        OutlinedButton(
//            onClick = onGoogleSignInClick,
//            modifier = Modifier.fillMaxWidth(0.8f),
//            colors = ButtonDefaults.outlinedButtonColors()
//        ) {
//            Icon(
//                imageVector = Icons.Default.Email,
//                contentDescription = "Google",
//                modifier = Modifier.size(20.dp)
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text("Sign in with Google")
//        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(
            onClick = { onSignUpClicked.invoke() },
            modifier = Modifier.wrapContentSize()
        ) {
            Text("Don't have an account? Sign Up", style = MaterialTheme.typography.bodyMedium)
        }
    }
}