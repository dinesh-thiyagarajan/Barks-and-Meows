package com.app.uicomponents.composables.buttons

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun PrimaryActionButtonComposable(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onButtonClick: () -> Unit,
    enabled: Boolean,
    buttonLabel: @Composable ((RowScope) -> Unit),
    modifier: Modifier = Modifier,
    buttonShape: Shape = RoundedCornerShape(4.dp),
    colors: ButtonColors = ButtonDefaults.buttonColors()
) {
    Button(
        shape = buttonShape,
        onClick = {
            coroutineScope.launch {
                onButtonClick.invoke()
            }
        },
        enabled = enabled,
        modifier = modifier,
        colors = colors,
        content = buttonLabel
    )
}
