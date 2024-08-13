package pet.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dineshworkspace.database.pet.dataModels.Pet
import com.dineshworkspace.uicomponents.composables.cards.PetCardComposable
import navigation.AppRouteActions
import navigation.NavRouter

@Composable
fun PetsListComposable(pets: List<Pet>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(pets.size) { index ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            ) {
                PetCardComposable(
                    petId = pets[index].id,
                    name = pets[index].name,
                    petCategory = pets[index].petCategory.category,
                    age = pets[index].age,
                    onPetCardClicked = { petId ->
                        NavRouter.navigate("${AppRouteActions.PetDetailScreen.route}$petId")
                    }
                )
            }
        }
    }
}