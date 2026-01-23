package com.app.auth.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.auth.useCases.DeleteAccountUseCase
import com.app.auth.useCases.LogoutUseCase
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    val profileUiState: StateFlow<ProfileUiState> get() = _profileUiState
    private val _profileUiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(
        ProfileUiState.Loading
    )

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                _profileUiState.value = ProfileUiState.Loaded(
                    email = currentUser.email ?: "No email"
                )
            } else {
                _profileUiState.value = ProfileUiState.NotLoggedIn
            }
        }
    }

    suspend fun logout() {
        val currentEmail = (profileUiState.value as? ProfileUiState.Loaded)?.email
        _profileUiState.value = ProfileUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                logoutUseCase.invoke().collect { success ->
                    withContext(Dispatchers.Main) {
                        if (success) {
                            _profileUiState.value = ProfileUiState.LoggedOut
                        } else {
                            _profileUiState.value = ProfileUiState.Error(
                                message = "Failed to logout. Please try again.",
                                email = currentEmail
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _profileUiState.value = ProfileUiState.Error(
                        message = e.message ?: "An unexpected error occurred during logout",
                        email = currentEmail
                    )
                }
            }
        }
    }

    suspend fun deleteAccount() {
        val currentEmail = (profileUiState.value as? ProfileUiState.Loaded)?.email
        _profileUiState.value = ProfileUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deleteAccountUseCase.invoke().collect { success ->
                    withContext(Dispatchers.Main) {
                        if (success) {
                            _profileUiState.value = ProfileUiState.LoggedOut
                        } else {
                            _profileUiState.value = ProfileUiState.Error(
                                message = "Failed to delete account. Please try again or contact support.",
                                email = currentEmail
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _profileUiState.value = ProfileUiState.Error(
                        message = e.message ?: "An unexpected error occurred while deleting account",
                        email = currentEmail
                    )
                }
            }
        }
    }

    fun resetErrorState(email: String) {
        _profileUiState.value = ProfileUiState.Loaded(email)
    }
}

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Loaded(val email: String) : ProfileUiState
    data object NotLoggedIn : ProfileUiState
    data object LoggedOut : ProfileUiState
    data class Error(val message: String, val email: String? = null) : ProfileUiState
}
