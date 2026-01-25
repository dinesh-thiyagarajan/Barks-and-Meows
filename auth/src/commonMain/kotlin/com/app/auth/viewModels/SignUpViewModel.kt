package com.app.auth.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.auth.useCases.SignUpUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(private val signUpUseCase: SignUpUseCase) : ViewModel() {

    val signUpUiState: StateFlow<SignUpUiState> get() = _signUpUiState
    private val _signUpUiState: MutableStateFlow<SignUpUiState> = MutableStateFlow(
        SignUpUiState.Initial
    )

    fun signUpWithEmailPassword(email: String, password: String) {
        _signUpUiState.value = SignUpUiState.SignUpInProgress
        viewModelScope.launch(Dispatchers.IO) {
            val response = signUpUseCase.invoke(email = email, password = password)
            if (response.userEmail.isNullOrEmpty()) {
                _signUpUiState.value = SignUpUiState.Error(response.message ?: "Sign up failed")
            } else {
                _signUpUiState.value = SignUpUiState.SignUpSuccess
            }
        }
    }
}

sealed interface SignUpUiState {
    data object Initial : SignUpUiState
    data object SignUpInProgress : SignUpUiState
    data object SignUpSuccess : SignUpUiState
    data class Error(val message: String) : SignUpUiState
}
