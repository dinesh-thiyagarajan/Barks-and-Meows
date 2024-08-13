package com.dineshworkspace.uicomponents.composables.cards

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PetCardComposable(name: String, petCategory: String, age: Int) {
    Card(
        modifier = Modifier.padding(20.dp)
    ) {
        Text(name)
    }
}