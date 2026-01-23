package com.app.auth.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        _profileUiState.value = ProfileUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            logoutUseCase.invoke().collect {
                if (it) {
                    withContext(Dispatchers.Main) {
                        _profileUiState.value = ProfileUiState.LoggedOut
                    }
                }
            }
        }
    }
}

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Loaded(val email: String) : ProfileUiState
    data object NotLoggedIn : ProfileUiState
    data object LoggedOut : ProfileUiState
}
