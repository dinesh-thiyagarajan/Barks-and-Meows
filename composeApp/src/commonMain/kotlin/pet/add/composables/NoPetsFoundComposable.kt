package pet.add.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.ic_app_logo
import barksandmeows.composeapp.generated.resources.no_pets_msg
import barksandmeows.composeapp.generated.resources.splash_screen_img_description
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun NoPetsFoundComposable() {
    Column(modifier = Modifier.padding(20.dp)) {
        Image(
            painter = painterResource(Res.drawable.ic_app_logo),
            contentDescription = stringResource(Res.string.splash_screen_img_description),
            modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            stringResource(Res.string.no_pets_msg),
            textAlign = TextAlign.Center
        )
    }
}