package home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.composables.LoadingComposable
import home.composables.AddPetsComposable
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
                AddPetsComposable(
                    coroutineScope = coroutineScope,
                    homeViewModel::addNewPet
                )
                Spacer(modifier = Modifier.padding(top = 20.dp))
                PetsListComposable((getPetsUiState.value as GetPetsUiState.Success).pets)
            }
        }

        is GetPetsUiState.Error -> {}
    }
}

