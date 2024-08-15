package com.dineshworkspace.uicomponents.composables.cards

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dineshworkspace.uicomponents.dataModels.PetData

@Composable
fun HelloPetCardComposable(modifier: Modifier = Modifier, petData: PetData) {
    Card(modifier = modifier.fillMaxWidth()) {
        Text("Hello ${petData.name}")
    }
}