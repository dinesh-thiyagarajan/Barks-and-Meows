package auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.app_name
import barksandmeows.composeapp.generated.resources.email
import barksandmeows.composeapp.generated.resources.ic_app_logo
import barksandmeows.composeapp.generated.resources.login
import barksandmeows.composeapp.generated.resources.password
import common.composables.LoadingComposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import navigation.NavRouter
import navigation.Router
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun LoginScreen(authViewModel: AuthViewModel = koinViewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val authUiState = authViewModel.authUiState.collectAsState()
    when (authUiState.value) {
        is AuthUiState.LoggedIn -> {
            NavRouter.navigate(Router.HomeRouter.route)
        }

        is AuthUiState.NotLoggedIn -> {
            LoginComposable(coroutineScope = coroutineScope, authViewModel = authViewModel)
        }

        is AuthUiState.LoginInProgress -> {
            LoadingComposable()
        }

        is AuthUiState.Error -> {}
    }
}

@Composable
private fun LoginComposable(coroutineScope: CoroutineScope, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
            stringResource(Res.string.app_name),
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
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    authViewModel.loginWithUserNamePassword(email = email, password = password)
                }
            },
            enabled = email.isNotEmpty() && password.isNotEmpty(),
            modifier = Modifier.wrapContentSize()
        ) {
            Text(stringResource(Res.string.login))
        }
    }
}