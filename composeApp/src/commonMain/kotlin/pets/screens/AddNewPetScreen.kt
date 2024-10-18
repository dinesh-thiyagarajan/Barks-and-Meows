package pets.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.pet_age
import barksandmeows.composeapp.generated.resources.pet_name
import com.dineshworkspace.dataModels.Pet
import com.dineshworkspace.uicomponents.composables.buttons.PrimaryActionButtonComposable
import com.dineshworkspace.uicomponents.composables.chips.CategorySelectorChip
import com.dineshworkspace.uicomponents.composables.loading.LoadingComposable
import com.dineshworkspace.uicomponents.composables.textFields.PetInputTextFieldComposable
import com.dineshworkspace.viewModels.AddPetUiState
import com.dineshworkspace.viewModels.PetViewModel
import common.utils.generateUUID
import kotlinx.coroutines.launch
import navigation.NavRouter
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AddNewPetScreen(petViewModel: PetViewModel = koinViewModel()) {

    val addPetUiState = petViewModel.addPetUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    LaunchedEffect(petViewModel) {
        petViewModel.getPetCategories()
    }

    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(scrollState)
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
                    var petAge by remember { mutableStateOf("0") }

                    LazyRow(
                        contentPadding = PaddingValues(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(petCategories.value.size) { index ->
                            CategorySelectorChip(label = petCategories.value[index].category,
                                categoryId = petCategories.value[index].id,
                                selected = petCategories.value[index].selected,
                                drawableResource = petCategories.value[index].drawableResource,
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
                        label = { Text(stringResource(Res.string.pet_name)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    PetInputTextFieldComposable(
                        textFieldValue = petAge,
                        onValueChange = { petAge = it },
                        label = { Text(stringResource(Res.string.pet_age)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    PrimaryActionButtonComposable(coroutineScope = coroutineScope,
                        onButtonClick = {
                            coroutineScope.launch {
                                val pet = Pet(id = generateUUID(),
                                    name = petName,
                                    age = petAge.toInt(),
                                    petCategory = petCategories.value.first { it.selected })
                                petViewModel.addNewPet(pet)
                            }
                        },
                        modifier = Modifier.padding(top = 10.dp).fillMaxWidth(),
                        enabled = petName.isNotEmpty() && petCategories.value.any { it.selected },
                        buttonLabel = {
                            Text("Submit")
                        })
                }
            }
        }
    }
}