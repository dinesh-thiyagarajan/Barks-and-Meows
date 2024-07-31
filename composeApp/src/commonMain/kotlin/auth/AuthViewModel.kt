package auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dineshworkspace.auth.useCases.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {

    val authUiState: StateFlow<AuthUiState> get() = _authUiState
    private val _authUiState: MutableStateFlow<AuthUiState> = MutableStateFlow(
        AuthUiState.NotLoggedIn
    )

    suspend fun loginWithUserNamePassword(email: String, password: String) {
        _authUiState.value = AuthUiState.LoginInProgress
        viewModelScope.launch(Dispatchers.IO) {
            val response = loginUseCase.invoke(email = email, password = password)
            if (response.userEmail.isNullOrEmpty()) {
                _authUiState.value = AuthUiState.LoggedIn
            } else {
                _authUiState.value = AuthUiState.Error
            }
        }
    }
}

sealed interface AuthUiState {
    data object NotLoggedIn : AuthUiState
    data object LoginInProgress : AuthUiState
    data object LoggedIn : AuthUiState
    data object Error : AuthUiState
}