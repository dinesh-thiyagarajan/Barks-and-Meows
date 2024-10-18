package com.dineshworkspace.uicomponents.composables.chips

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import barksandmeows.uicomponents.generated.resources.Res
import barksandmeows.uicomponents.generated.resources.ic_bone
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun CategorySelectorChip(
    label: String,
    categoryId: Int,
    selected: Boolean = true,
    onChipSelected: ((Int) -> Unit)? = null,
    drawableResource: DrawableResource?
) {
    FilterChip(
        selected = selected,
        onClick = { onChipSelected?.invoke(categoryId) },
        label = { Text(label) },
        leadingIcon = {
            Image(
                painter = painterResource(drawableResource ?: Res.drawable.ic_bone),
                contentDescription = categoryId.toString(),
                modifier = Modifier.size(15.dp),
            )
        }
    )
}