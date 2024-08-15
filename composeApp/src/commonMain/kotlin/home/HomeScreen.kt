package home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.ic_add
import com.dineshworkspace.composables.NoPetsFoundComposable
import com.dineshworkspace.dataModels.Pet
import com.dineshworkspace.uicomponents.composables.cards.PetCardComposable
import com.dineshworkspace.viewModels.GetPetsUiState
import com.dineshworkspace.viewModels.PetViewModel
import common.composables.ErrorComposable
import common.composables.LoadingComposable
import navigation.AppRouteActions
import navigation.NavRouter
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pets.extensions.toPetData

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(petViewModel: PetViewModel = koinViewModel()) {
    val getPetsUiState = petViewModel.getPetsUiState.collectAsState()

    LaunchedEffect(Unit) {
        petViewModel.getPets()
    }

    Scaffold {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.padding(end = 10.dp).fillMaxWidth()
            ) {
                Image(
                    painterResource(Res.drawable.ic_add),
                    "stringResource(Res.string.add_pet_msg)",
                    modifier = Modifier.size(32.dp).clickable {
                        NavRouter.navigate(AppRouteActions.AddNewPetScreen.route)
                    }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            when (val petState = getPetsUiState.value) {
                is GetPetsUiState.Loading -> LoadingComposable()
                is GetPetsUiState.Success -> {
                    petState.pets.takeIf { it.isNotEmpty() }
                        ?.let { PetsListComposable(it) }
                        ?: NoPetsFoundComposable()
                }

                is GetPetsUiState.Error -> ErrorComposable()
            }
        }
    }
}

@Composable
fun PetsListComposable(pets: List<Pet>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
    ) {
        items(pets.size) { index ->
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                PetCardComposable(
                    petData = pets[index].toPetData(),
                    onPetCardClicked = { petId ->
                        NavRouter.navigate("${AppRouteActions.PetDetailScreen.route}$petId")
                    }
                )
            }
        }
    }
}

