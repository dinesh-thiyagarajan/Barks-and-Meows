package auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dineshworkspace.auth.useCases.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class AuthViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {

    suspend fun loginWithUserNamePassword(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = loginUseCase.invoke(email = email, password = password)

        }
    }

}