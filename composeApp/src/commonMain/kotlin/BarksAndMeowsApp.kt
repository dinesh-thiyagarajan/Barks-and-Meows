import androidx.compose.runtime.Composable
import auth.LoginComposable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import theme.BarksAndMeowsTheme

@Composable
@Preview
fun BarksAndMeowsApp() {
    KoinApplication(application = {
        modules(appModule())
    }) {
        BarksAndMeowsTheme {
            LoginComposable()
        }
    }
}