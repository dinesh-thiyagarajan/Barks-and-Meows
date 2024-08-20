package com.dineshworkspace.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import barksandmeows.pet.generated.resources.Res
import barksandmeows.pet.generated.resources.ic_add
import barksandmeows.pet.generated.resources.ic_app_logo
import com.dineshworkspace.dataModels.Pet
import org.jetbrains.compose.resources.painterResource

@Composable
fun PetDetailsComposable(pet: Pet, onAddVaccineNoteClicked: (String) -> Unit) {
    Column(modifier = Modifier.padding(20.dp)) {
        Image(
            painterResource(Res.drawable.ic_app_logo),
            "pet",
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.padding(top = 10.dp))

        Text(
            pet.name, style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Vaccine Notes")
            Image(
                painterResource(Res.drawable.ic_add),
                "add",
                modifier = Modifier.size(32.dp).clickable {
                    onAddVaccineNoteClicked.invoke(pet.id)
                }
            )
        }

        LazyColumn {

        }
    }
}