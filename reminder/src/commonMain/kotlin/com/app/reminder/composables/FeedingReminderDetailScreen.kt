package com.app.reminder.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import barksandmeows.reminder.generated.resources.Res
import barksandmeows.reminder.generated.resources.go_back
import barksandmeows.reminder.generated.resources.mark_as_fed
import barksandmeows.reminder.generated.resources.time_to_feed
import com.app.reminder.viewModels.ReminderViewModel
import com.app.reminder.viewModels.SelectedReminderUiState
import com.app.uicomponents.composables.loading.LoadingComposable
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun FeedingReminderDetailScreen(
    reminderId: String,
    reminderViewModel: ReminderViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val selectedReminderUiState by reminderViewModel.selectedReminderUiState.collectAsState()

    LaunchedEffect(reminderId) {
        reminderViewModel.getReminderById(reminderId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val state = selectedReminderUiState) {
            is SelectedReminderUiState.Loading -> LoadingComposable()
            is SelectedReminderUiState.Success -> {
                ReminderDetailContent(
                    reminder = state.reminder,
                    onMarkAsFed = {
                        reminderViewModel.markAsFed(state.reminder.id)
                        onNavigateBack()
                    }
                )
            }
            is SelectedReminderUiState.Error -> {
                ErrorContent(
                    message = state.message,
                    onNavigateBack = onNavigateBack
                )
            }
        }
    }
}

@Composable
private fun ReminderDetailContent(
    reminder: com.app.reminder.dataModels.FeedingReminder,
    onMarkAsFed: () -> Unit
) {
    Spacer(modifier = Modifier.height(32.dp))

    Icon(
        imageVector = Icons.Default.Restaurant,
        contentDescription = null,
        modifier = Modifier.size(80.dp),
        tint = MaterialTheme.colorScheme.primary
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = stringResource(Res.string.time_to_feed),
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.primary
    )

    Spacer(modifier = Modifier.height(32.dp))

    ReminderDetailCard(reminder = reminder)

    Spacer(modifier = Modifier.height(32.dp))

    MarkAsFedButton(onClick = onMarkAsFed)
}

@Composable
private fun MarkAsFedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Restaurant,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = stringResource(Res.string.mark_as_fed),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateBack) {
            Text(stringResource(Res.string.go_back))
        }
    }
}
