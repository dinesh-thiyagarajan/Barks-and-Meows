package com.app.auth.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.auth.useCases.GoogleSignInUseCase
import com.app.auth.useCases.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase
) : ViewModel() {

    val authUiState: StateFlow<AuthUiState> get() = _authUiState
    private val _authUiState: MutableStateFlow<AuthUiState> = MutableStateFlow(
        AuthUiState.NotLoggedIn
    )

    suspend fun loginWithUserNamePassword(email: String, password: String) {
        _authUiState.value = AuthUiState.LoginInProgress
        viewModelScope.launch(Dispatchers.IO) {
            val response = loginUseCase.invoke(email = email, password = password)
            if (response.userEmail.isNullOrEmpty()) {
                _authUiState.value = AuthUiState.Error(response.message ?: "Login failed")
            } else {
                _authUiState.value = AuthUiState.LoggedIn
            }
        }
    }

    suspend fun loginWithGoogle(idToken: String?) {
        if (idToken == null) {
            _authUiState.value = AuthUiState.Error("Google Sign-In cancelled")
            return
        }
        _authUiState.value = AuthUiState.LoginInProgress
        viewModelScope.launch(Dispatchers.IO) {
            val response = googleSignInUseCase.invoke(idToken = idToken)
            if (response.userEmail.isNullOrEmpty()) {
                _authUiState.value = AuthUiState.Error(response.message ?: "Google Sign-In failed")
            } else {
                _authUiState.value = AuthUiState.LoggedIn
            }
        }
    }
}

sealed interface AuthUiState {
    data object NotLoggedIn : AuthUiState
    data object LoginInProgress : AuthUiState
    data object LoggedIn : AuthUiState
    data class Error(val message: String) : AuthUiState
}