package com.dineshworkspace.uicomponents.composables.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dineshworkspace.uicomponents.dataModels.PetData
import org.jetbrains.compose.resources.painterResource

@Composable
fun PetCardComposable(
    petData: PetData,
    onPetCardClicked: (String) -> Unit
) {
    val horizontalGradientBrush = Brush.horizontalGradient(
        colors = listOf(
            Color.Yellow,
            Color.Green
        )
    )
    Card(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth().clickable {
                onPetCardClicked.invoke(petData.id)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        BoxWithConstraints(
            modifier = Modifier.background(brush = horizontalGradientBrush).padding(10.dp)
                .fillMaxSize()
        ) {
            Row {
                Image(
                    painterResource(petData.image),
                    "pet",
                    modifier = Modifier.size(32.dp)
                )
                Column(modifier = Modifier.padding(start = 10.dp, top = 5.dp)) {
                    Text(
                        petData.name, style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily.SansSerif
                        )
                    )
                    Text(
                        petData.age.toString(), style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily.SansSerif
                        )
                    )
                }
            }
        }
    }
}