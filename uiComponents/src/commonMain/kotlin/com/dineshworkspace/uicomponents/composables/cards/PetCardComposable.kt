package com.dineshworkspace.uicomponents.composables.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dineshworkspace.uicomponents.dataModels.PetData

@Composable
fun PetCardComposable(
    petData: PetData,
    onPetCardClicked: (String) -> Unit
) {
    Card(
        modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth().clickable {
            onPetCardClicked.invoke(petData.id)
        }
    ) {

        Text(petData.name)
        Spacer(modifier = Modifier.padding(10.dp))
        Text(petData.age.toString())
        Spacer(modifier = Modifier.padding(10.dp))
        Text(petData.petCategory.category)
        Spacer(modifier = Modifier.padding(10.dp))

    }
}