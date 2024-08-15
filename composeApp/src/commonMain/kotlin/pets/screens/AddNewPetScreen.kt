package pets.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dineshworkspace.dataModels.Pet
import com.dineshworkspace.uicomponents.composables.appBar.BarksAndMeowsAppBar
import com.dineshworkspace.uicomponents.composables.buttons.PrimaryActionButtonComposable
import com.dineshworkspace.uicomponents.composables.chips.CategorySelectorChip
import com.dineshworkspace.uicomponents.composables.loading.LoadingComposable
import com.dineshworkspace.uicomponents.composables.textFields.PetInputTextFieldComposable
import com.dineshworkspace.viewModels.AddPetUiState
import com.dineshworkspace.viewModels.PetViewModel
import common.utils.generateUUID
import kotlinx.coroutines.launch
import navigation.NavRouter
import navigation.NavRouter.getNavController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AddNewPetScreen(petViewModel: PetViewModel = koinViewModel()) {

    val addPetUiState = petViewModel.addPetUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(petViewModel) {
        petViewModel.getPetCategories()
    }

    Scaffold(topBar = {
        BarksAndMeowsAppBar(canNavigateBack = getNavController()?.previousBackStackEntry != null,
            navigateUp = { getNavController()?.navigateUp() })
    }) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            when (addPetUiState.value) {
                is AddPetUiState.Loading -> {
                    LoadingComposable()
                }

                is AddPetUiState.Success -> {
                    NavRouter.popBackStack()
                }

                is AddPetUiState.NotStarted -> {
                    val petCategories = petViewModel.petCategories.collectAsState()
                    var petName by remember { mutableStateOf("") }

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(petCategories.value.size) { index ->
                            CategorySelectorChip(label = petCategories.value[index].category,
                                categoryId = petCategories.value[index].id,
                                selected = petCategories.value[index].selected,
                                onChipSelected = {
                                    coroutineScope.launch {
                                        petViewModel.updateSelectedCategory(it)
                                    }
                                })
                        }
                    }

                    PetInputTextFieldComposable(
                        textFieldValue = petName,
                        onValueChange = { petName = it },
                        label = { Text("") },
                        modifier = Modifier,
                    )

                    PrimaryActionButtonComposable(coroutineScope = coroutineScope,
                        onButtonClick = {
                            coroutineScope.launch {
                                val pet = Pet(id = generateUUID(),
                                    name = petName,
                                    age = 0,
                                    petCategory = petCategories.value.first { it.selected })
                                petViewModel.addNewPet(pet)
                            }
                        },
                        enabled = petName.isNotEmpty() && petCategories.value.any { it.selected },
                        buttonLabel = {
                            Text("Submit")
                        })
                }
            }
        }
    }
}