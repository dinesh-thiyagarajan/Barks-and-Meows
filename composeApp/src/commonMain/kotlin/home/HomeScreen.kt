package home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import common.composables.ErrorComposable
import common.composables.LoadingComposable
import home.composables.NoPetsFoundComposable
import home.composables.PetsListComposable
import home.viewModels.GetPetsUiState
import home.viewModels.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = koinViewModel()) {
    val getPetsUiState = homeViewModel.getPetsUiState.collectAsState()

    LaunchedEffect(homeViewModel) {
        homeViewModel.getPets()
    }

    when (val petState = getPetsUiState.value) {
        is GetPetsUiState.Loading -> {
            LoadingComposable()
        }

        is GetPetsUiState.Success -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                petState.pets.takeIf { it.isNotEmpty() }
                    ?.let { PetsListComposable(it) }
                    ?: NoPetsFoundComposable()
            }
        }

        is GetPetsUiState.Error -> {
            ErrorComposable()
        }
    }
}

