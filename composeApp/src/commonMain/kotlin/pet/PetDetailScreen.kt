package pet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import common.composables.BarksAndMeowsAppBar
import navigation.NavRouter.getNavController

@Composable
fun PetDetailScreen(petId: String) {

    Scaffold(topBar = {
        BarksAndMeowsAppBar(
            canNavigateBack = getNavController()?.previousBackStackEntry != null,
            navigateUp = { getNavController()?.navigateUp() }
        )
    }) { innerPadding ->
        Column(modifier = Modifier.padding(paddingValues = innerPadding).fillMaxSize()) {
            Text("Received Id is $petId")
        }
    }

}