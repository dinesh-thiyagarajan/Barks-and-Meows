package pets.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import barksandmeows.composeapp.generated.resources.Res
import barksandmeows.composeapp.generated.resources.pet_birth_date
import barksandmeows.composeapp.generated.resources.pet_details
import barksandmeows.composeapp.generated.resources.pet_name
import barksandmeows.composeapp.generated.resources.pet_type
import barksandmeows.composeapp.generated.resources.select_birth_date_description
import com.app.dataModels.PetCategory
import com.app.uicomponents.composables.cards.FormSectionCard
import com.app.uicomponents.composables.chips.CategorySelectorChip
import com.app.uicomponents.composables.datePicker.DatePickerFieldComposable
import com.app.uicomponents.composables.section.SectionHeader
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetFormContent(
    petCategories: List<PetCategory>,
    petName: String,
    onPetNameChange: (String) -> Unit,
    selectedBirthDateMillis: Long?,
    onBirthDateSelected: (Long) -> Unit,
    onCategorySelected: (Int) -> Unit
) {
    val currentYear = remember {
        Instant.fromEpochMilliseconds(kotlin.time.Clock.System.now().toEpochMilliseconds())
            .toLocalDateTime(TimeZone.currentSystemDefault()).year
    }

    val birthDateDisplayText = selectedBirthDateMillis?.let { millis ->
        val localDate = Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.UTC).date
        val age = currentYear - localDate.year
        val month = localDate.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
        "$month ${localDate.dayOfMonth}, ${localDate.year}  (${if (age == 1) "1 year" else "$age years"} old)"
    } ?: ""

    // --- Pet Type Section ---
    SectionHeader(icon = Icons.Outlined.Category, title = stringResource(Res.string.pet_type))

    FormSectionCard {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(petCategories.size) { index ->
                CategorySelectorChip(
                    label = petCategories[index].category,
                    categoryId = petCategories[index].id,
                    selected = petCategories[index].selected,
                    drawableResource = petCategories[index].drawableResource,
                    onChipSelected = onCategorySelected
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // --- Pet Details Section ---
    SectionHeader(icon = Icons.Outlined.Pets, title = stringResource(Res.string.pet_details))

    FormSectionCard(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        OutlinedTextField(
            value = petName,
            onValueChange = onPetNameChange,
            label = { Text(stringResource(Res.string.pet_name)) },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Badge,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        DatePickerFieldComposable(
            value = birthDateDisplayText,
            label = { Text(stringResource(Res.string.pet_birth_date)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Cake,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIconDescription = stringResource(Res.string.select_birth_date_description),
            onDateSelected = onBirthDateSelected,
            initialSelectedDateMillis = selectedBirthDateMillis
        )
    }
}
