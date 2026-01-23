package splash

import androidx.lifecycle.ViewModel
import com.app.auth.useCases.IsLoggedInUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SplashViewModel(private val isLoggedInUseCase: IsLoggedInUseCase) : ViewModel() {

    val splashUiState: StateFlow<SplashUiState> get() = _splashUiState
    private val _splashUiState: MutableStateFlow<SplashUiState> = MutableStateFlow(
        SplashUiState.FetchingLoginStatus
    )


    suspend fun isLoggedIn() {
        isLoggedInUseCase.invoke().collect { isLoggedIn ->
            // adding a delay of 2 seconds to display the splash screen
            delay(2000)
            if (isLoggedIn) {
                _splashUiState.value = SplashUiState.LoggedIn
            } else {
                _splashUiState.value = SplashUiState.NotLoggedIn
            }
        }
    }
}

sealed interface SplashUiState {
    data object NotLoggedIn : SplashUiState
    data object FetchingLoginStatus : SplashUiState
    data object LoggedIn : SplashUiState
}


