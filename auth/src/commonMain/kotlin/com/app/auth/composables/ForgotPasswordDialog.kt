package com.app.auth.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import barksandmeows.auth.generated.resources.Res
import barksandmeows.auth.generated.resources.cancel
import barksandmeows.auth.generated.resources.email
import barksandmeows.auth.generated.resources.forgot_password
import barksandmeows.auth.generated.resources.forgot_password_description
import barksandmeows.auth.generated.resources.forgot_password_success
import barksandmeows.auth.generated.resources.ok
import barksandmeows.auth.generated.resources.send_reset_email
import com.app.auth.viewModels.ForgotPasswordState
import org.jetbrains.compose.resources.stringResource

@Composable
fun ForgotPasswordDialog(
    email: String,
    onEmailChange: (String) -> Unit,
    forgotPasswordState: ForgotPasswordState,
    onSendResetEmail: () -> Unit,
    onDismiss: () -> Unit,
    onReset: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            if (forgotPasswordState !is ForgotPasswordState.Sending) {
                onDismiss()
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
                            value = email,
                            onValueChange = onEmailChange,
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
                            text = forgotPasswordState.message,
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
                        onClick = onSendResetEmail,
                        enabled = email.isNotEmpty()
                    ) {
                        Text(stringResource(Res.string.send_reset_email))
                    }
                }

                is ForgotPasswordState.Sent, is ForgotPasswordState.Error -> {
                    TextButton(onClick = onReset) {
                        Text(stringResource(Res.string.ok))
                    }
                }

                else -> {}
            }
        },
        dismissButton = {
            if (forgotPasswordState is ForgotPasswordState.Idle) {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(Res.string.cancel))
                }
            }
        }
    )
}
