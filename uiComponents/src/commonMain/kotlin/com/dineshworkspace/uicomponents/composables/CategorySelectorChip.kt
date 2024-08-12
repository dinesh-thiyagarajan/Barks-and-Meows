package com.dineshworkspace.uicomponents.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CategorySelectorChip(
    label: String,
    categoryId: Int,
    selected: Boolean = true,
    onChipSelected: ((Int) -> Unit)? = null
) {
    FilterChip(
        selected = true,
        onClick = { onChipSelected?.invoke(categoryId) },
        label = { Text(label) },
        leadingIcon = {
            if (selected) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = null
                )
                Icon(
                    Icons.Default.Add,
                    contentDescription = null
                )
            } else {
                Icon(
                    Icons.Filled.Create,
                    contentDescription = null
                )
            }
        }
    )
}