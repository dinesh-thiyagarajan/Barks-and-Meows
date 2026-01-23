package com.app.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import barksandmeows.pet.generated.resources.Res
import barksandmeows.pet.generated.resources.add_vaccine_note_description
import barksandmeows.pet.generated.resources.pet_description
import barksandmeows.pet.generated.resources.track_vaccination_history
import barksandmeows.pet.generated.resources.vaccine_records
import barksandmeows.pet.generated.resources.years_old
import com.app.dataModels.Pet
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PetDetailsComposable(
    pet: Pet,
    petIcon: DrawableResource,
    onAddVaccineNoteClicked: (String) -> Unit
) {
    Column(modifier = Modifier.padding(20.dp)) {
        Image(
            painterResource(petIcon),
            stringResource(Res.string.pet_description),
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        // Pet Name as prominent header
        Text(
            text = pet.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        // Pet age and category
        Text(
            text = "${pet.petCategory.category} • ${pet.age} ${stringResource(Res.string.years_old)}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.padding(top = 24.dp))

        // Vaccine Notes Section Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(Res.string.vaccine_records),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = stringResource(Res.string.track_vaccination_history),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                FilledTonalIconButton(
                    onClick = { onAddVaccineNoteClicked.invoke(pet.id) },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(Res.string.add_vaccine_note_description),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}