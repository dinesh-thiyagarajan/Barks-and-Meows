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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.back
import com.dineshworkspace.database.pet.dataModels.PetCategory
import com.dineshworkspace.uicomponents.composables.CategorySelectorChip
import common.composables.BarksAndMeowsAppBar
import home.viewModels.PetViewModel
import navigation.NavRouter.getNavController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AddNewPetScreen(petViewModel: PetViewModel = koinViewModel()) {

    val petCategories = petViewModel.petCategories.collectAsState()

    LaunchedEffect(petViewModel) {
        petViewModel.getPetCategories()
    }

    Scaffold(topBar = {
        BarksAndMeowsAppBar(
            titleKey = Res.string.back,
            canNavigateBack = getNavController()?.previousBackStackEntry != null,
            navigateUp = { getNavController()?.navigateUp() }
        )
    }) { innerPadding ->

        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            PetCategoryComposable(petCategories)
        }
    }
}

@Composable
fun PetCategoryComposable(petCategories: State<List<PetCategory>>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Spacing between chips
    ) {
        items(petCategories.value.size) { index ->
            CategorySelectorChip(
                petCategories.value[index].category,
                categoryId = petCategories.value[index].id
            )
        }
    }
}