package pets.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.ic_add
import barksandmeows.composeapp.generated.resources.ic_app_logo
import com.dineshworkspace.dataModels.Pet
import com.dineshworkspace.uicomponents.composables.appBar.BarksAndMeowsAppBar
import com.dineshworkspace.uicomponents.composables.error.ErrorComposable
import com.dineshworkspace.uicomponents.composables.loading.LoadingComposable
import com.dineshworkspace.viewModels.GetPetDetailsUiState
import com.dineshworkspace.viewModels.PetDetailsViewModel
import navigation.AppRouteActions
import navigation.NavRouter
import navigation.NavRouter.getNavController
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Composable
fun PetDetailScreen(petId: String, petDetailsViewModel: PetDetailsViewModel = koinViewModel()) {
    LaunchedEffect(petId) {
        petDetailsViewModel.getPetDetails(petId = petId)
    }

    val petDetailsUiState = petDetailsViewModel.petDetailsUiState.collectAsState()

    Scaffold(topBar = {
        BarksAndMeowsAppBar(
            canNavigateBack = getNavController()?.previousBackStackEntry != null,
            navigateUp = { getNavController()?.navigateUp() }
        )
    }) { innerPadding ->
        Column(modifier = Modifier.padding(paddingValues = innerPadding).fillMaxSize()) {
            when (val petResponse = petDetailsUiState.value) {
                is GetPetDetailsUiState.Error -> {
                    ErrorComposable(errorMessage = petResponse.errorMessage)
                }

                GetPetDetailsUiState.Loading -> {
                    LoadingComposable()
                }

                is GetPetDetailsUiState.Success -> {
                    PetDetailsComposable(pet = petResponse.pet)
                }
            }
        }
    }
}

@Composable
internal fun PetDetailsComposable(pet: Pet) {
    Column(modifier = Modifier.padding(20.dp)) {

        Image(
            painterResource(Res.drawable.ic_app_logo),
            "pet",
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.padding(top = 10.dp))

        Text(
            pet.name, style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif
            )
        )

        Spacer(modifier = Modifier.padding(top = 10.dp))
        Text("Vaccine Notes")
        Spacer(modifier = Modifier.padding(top = 10.dp))

        Image(
            painterResource(Res.drawable.ic_add),
            "add",
            modifier = Modifier.size(32.dp).clickable {
                NavRouter.navigate("${AppRouteActions.AddVaccineNoteScreen.route}${pet.id}")
            }
        )
    }
}

