package home.add

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
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.password
import barksandmeows.composeapp.generated.resources.pet_name
import com.dineshworkspace.uicomponents.composables.chip.CategorySelectorChip
import com.dineshworkspace.uicomponents.composables.textFields.PetInputTextFieldComposable
import common.composables.BarksAndMeowsAppBar
import home.viewModels.PetViewModel
import kotlinx.coroutines.launch
import navigation.NavRouter.getNavController
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AddNewPetScreen(petViewModel: PetViewModel = koinViewModel()) {

    val petCategories = petViewModel.petCategories.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var petName by remember { mutableStateOf("") }

    LaunchedEffect(petViewModel) {
        petViewModel.getPetCategories()
    }

    Scaffold(topBar = {
        BarksAndMeowsAppBar(
            canNavigateBack = getNavController()?.previousBackStackEntry != null,
            navigateUp = { getNavController()?.navigateUp() }
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(petCategories.value.size) { index ->
                    CategorySelectorChip(
                        label = petCategories.value[index].category,
                        categoryId = petCategories.value[index].id,
                        selected = petCategories.value[index].selected,
                        onChipSelected = {
                            coroutineScope.launch {
                                petViewModel.updateSelectedCategory(it)
                            }
                        }
                    )
                }
            }

            PetInputTextFieldComposable(
                textFieldValue = petName,
                onValueChange = { petName = it },
                label = { Text(stringResource(Res.string.pet_name)) },
                modifier = Modifier,
            )
        }
    }
}