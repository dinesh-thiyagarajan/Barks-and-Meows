package com.dineshworkspace.uicomponents.composables.textFields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun PetInputTextFieldComposable(
    textFieldValue: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next
    ),
    modifier: Modifier,
) {
    InputTextFieldComposable(
        textFieldValue = textFieldValue,
        onValueChange = onValueChange,
        label = label,
        keyboardOptions = keyboardOptions,
        modifier = modifier,
        colors = TextFieldDefaults.colors()
    )
}


@Composable
fun GenericInputTextFieldComposable(
    textFieldValue: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    ),
    modifier: Modifier,
) {
    InputTextFieldComposable(
        textFieldValue = textFieldValue,
        onValueChange = onValueChange,
        label = label,
        keyboardOptions = keyboardOptions,
        modifier = modifier,
        colors = TextFieldDefaults.colors()
    )
}

/**
 * Use this as a base composable for all Text Input fields
 */
@Composable
internal fun InputTextFieldComposable(
    textFieldValue: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    ),
    modifier: Modifier,
    colors: TextFieldColors,
) {
    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { onValueChange.invoke(it) },
        label = label,
        keyboardOptions = keyboardOptions,
        modifier = modifier,
        colors = colors
    )
}