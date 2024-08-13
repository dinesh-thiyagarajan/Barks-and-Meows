package com.dineshworkspace.uicomponents.composables.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PetCardComposable(
    petId: String,
    name: String,
    petCategory: String,
    age: Int,
    onPetCardClicked: (String) -> Unit
) {
    Card(
        modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth().clickable {
            onPetCardClicked.invoke(petId)
        }
    ) {
        Text(name)
    }
}