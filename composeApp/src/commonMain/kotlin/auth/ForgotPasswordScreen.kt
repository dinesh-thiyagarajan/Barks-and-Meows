package auth

import androidx.compose.runtime.Composable
import com.app.auth.composables.ForgotPasswordScreen
import com.app.auth.viewModels.AuthViewModel
import navigation.NavRouter
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ForgotPasswordScreen(authViewModel: AuthViewModel = koinViewModel()) {
    ForgotPasswordScreen(
        authViewModel = authViewModel,
        onBackPressed = { NavRouter.popBackStack() }
    )
}
