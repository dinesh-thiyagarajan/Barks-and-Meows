package home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.add_pet_description
import com.dineshworkspace.composables.NoPetsFoundComposable
import com.dineshworkspace.uicomponents.composables.cards.PetCardComposable
import com.dineshworkspace.uicomponents.composables.error.ErrorComposable
import com.dineshworkspace.uicomponents.composables.loading.LoadingComposable
import com.dineshworkspace.viewModels.GetPetsUiState
import com.dineshworkspace.viewModels.PetViewModel
import common.platform.rememberExitApp
import navigation.AppRouteActions
import navigation.NavRouter
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pets.extensions.toPetData

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(petViewModel: PetViewModel = koinViewModel()) {
    val getPetsUiState = petViewModel.getPetsUiState.collectAsState()
    val exitApp = rememberExitApp()

    LaunchedEffect(Unit) {
        petViewModel.getPets()
    }

    // Handle back button press - exit app if at the start of navigation
    BackHandler(enabled = true) {
        val navController = NavRouter.getNavController()
        if (navController?.previousBackStackEntry == null) {
            // At the start destination, exit the app
            exitApp()
        } else {
            // There's something in backstack, navigate back normally
            navController.popBackStack()
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    NavRouter.navigate(AppRouteActions.AddNewPetScreen.route)
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(Res.string.add_pet_description)
                )
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            when (val petState = getPetsUiState.value) {
                is GetPetsUiState.Loading -> LoadingComposable()
                is GetPetsUiState.Success -> {
                    petState.pets.takeIf { it.isNotEmpty() }
                        ?.let { pets ->
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(pets) { pet ->
                                    PetCardComposable(
                                        petData = pet.toPetData(),
                                        onPetCardClicked = { petId ->
                                            NavRouter.navigate("${AppRouteActions.PetDetailScreen.route}$petId")
                                        }
                                    )
                                }
                            }
                        }
                        ?: NoPetsFoundComposable()
                }

                is GetPetsUiState.Error -> ErrorComposable()
            }
        }
    }
}

