package home.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dineshworkspace.database.pet.dataModels.Pet
import com.dineshworkspace.database.pet.dataModels.PetType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun PetsListComposable(pets: List<Pet>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(pets.size) { index ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            ) {
                Text(text = pets[index].name)
            }
        }
    }
}

@Composable
fun AddPetsComposable(
    coroutineScope: CoroutineScope,
    onAddNewPetClicked: suspend (Pet) -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                val pet = Pet(id = "", name = "Snowy", age = 1, image = "", petType = PetType.Dog)
                coroutineScope.launch {
                    onAddNewPetClicked.invoke(pet)
                }
            },
        ) {
            Text("Submit")
        }
    }
}