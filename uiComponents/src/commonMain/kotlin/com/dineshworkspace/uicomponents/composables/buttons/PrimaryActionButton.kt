package com.dineshworkspace.uicomponents.composables.buttons

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun PrimaryActionButtonComposable(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onButtonClick: () -> Unit,
    enabled: Boolean,
    buttonLabel: @Composable ((RowScope) -> Unit),
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors()
) {
    Button(
        onClick = {
            coroutineScope.launch {
                onButtonClick.invoke()
            }
        }, enabled = enabled, modifier = modifier, colors = colors, content = buttonLabel
    )
}