package home

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import common.composables.LoadingComposable
import home.composables.AddPetsComposable
import home.composables.NoPetsFoundComposable
import home.composables.PetsListComposable
import home.viewModels.GetPetsUiState
import home.viewModels.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = koinViewModel(), coroutineScope: CoroutineScope) {
    val getPetsUiState = homeViewModel.getPetsUiState.collectAsState()
    LaunchedEffect(homeViewModel) {
        homeViewModel.getPets()
    }
    when (getPetsUiState.value) {
        is GetPetsUiState.Loading -> {
            LoadingComposable()
        }

        is GetPetsUiState.Success -> {
            Column {
                val pets = (getPetsUiState.value as GetPetsUiState.Success).pets
                if (pets.isEmpty()) {
                    NoPetsFoundComposable()
                } else {
                    PetsListComposable((getPetsUiState.value as GetPetsUiState.Success).pets)
                }
                AddPetsComposable(
                    coroutineScope = coroutineScope,
                    homeViewModel::addNewPet
                )
            }
        }

        is GetPetsUiState.Error -> {}
    }
}

