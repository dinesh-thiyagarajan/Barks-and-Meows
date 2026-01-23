package com.app.uicomponents.composables.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import barksandmeows.uicomponents.generated.resources.Res
import barksandmeows.uicomponents.generated.resources.default_error_msg
import org.jetbrains.compose.resources.stringResource

@Composable
fun ErrorComposable(errorMessage: String? = null) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(errorMessage ?: stringResource(Res.string.default_error_msg))
    }
}