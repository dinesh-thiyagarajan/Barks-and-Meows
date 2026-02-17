package com.app.reminder.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.reminder.dataModels.FeedingReminder
import com.app.reminder.viewModels.AddReminderUiState
import com.app.reminder.viewModels.PetsUiState
import com.app.reminder.viewModels.ReminderViewModel
import com.app.reminder.viewModels.RemindersUiState
import com.app.uicomponents.composables.loading.LoadingComposable
import barksandmeows.reminder.generated.resources.Res
import barksandmeows.reminder.generated.resources.reminder_disclaimer
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ReminderScreen(
    reminderViewModel: ReminderViewModel = koinViewModel(),
    onReminderClick: (String) -> Unit = {},
    onScheduleReminder: (FeedingReminder) -> Unit = {},
    onCancelReminder: (String) -> Unit = {},
    onRequestNotificationPermission: (() -> Unit, () -> Unit) -> Unit = { onGranted, _ -> onGranted() },
    isNotificationPermissionGranted: () -> Boolean = { true }
) {
    val remindersUiState by reminderViewModel.remindersUiState.collectAsState()
    val petsUiState by reminderViewModel.petsUiState.collectAsState()
    val addReminderUiState by reminderViewModel.addReminderUiState.collectAsState()

    var showAddReminderDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var reminderToDelete by remember { mutableStateOf<FeedingReminder?>(null) }
    var showPermissionDeniedDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        reminderViewModel.getReminders()
        reminderViewModel.getPets()
    }

    LaunchedEffect(addReminderUiState) {
        when (val state = addReminderUiState) {
            is AddReminderUiState.Success -> {
                showAddReminderDialog = false
                onScheduleReminder(state.reminder)
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (isNotificationPermissionGranted()) {
                        showAddReminderDialog = true
                    } else {
                        onRequestNotificationPermission(
                            { showAddReminderDialog = true },
                            { showPermissionDeniedDialog = true }
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Reminder"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Feeding Reminders",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(Res.string.reminder_disclaimer),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (val state = remindersUiState) {
                is RemindersUiState.Loading -> LoadingComposable()
                is RemindersUiState.Success -> {
                    if (state.reminders.isEmpty()) {
                        NoRemindersComposable()
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            items(state.reminders) { reminder ->
                                ReminderCardComposable(
                                    reminder = reminder,
                                    onReminderClick = { onReminderClick(reminder.id) },
                                    onToggleEnabled = { enabled ->
                                        val updatedReminder = reminder.copy(isEnabled = enabled)
                                        reminderViewModel.updateReminder(updatedReminder)
                                        onScheduleReminder(updatedReminder)
                                    },
                                    onMarkAsFed = {
                                        reminderViewModel.markAsFed(reminder.id)
                                    },
                                    onDelete = {
                                        reminderToDelete = reminder
                                        showDeleteConfirmDialog = true
                                    }
                                )
                            }
                        }
                    }
                }
                is RemindersUiState.Error -> {
                    Text(
                        text = "Failed to load reminders",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    if (showAddReminderDialog) {
        val pets = (petsUiState as? PetsUiState.Success)?.pets ?: emptyList()
        AddReminderDialog(
            pets = pets,
            isLoading = addReminderUiState is AddReminderUiState.Loading,
            onDismiss = { showAddReminderDialog = false },
            onAddReminder = { pet, intervalHours ->
                reminderViewModel.addReminder(pet, intervalHours)
            }
        )
    }

    if (showDeleteConfirmDialog) {
        DeleteReminderConfirmDialog(
            reminder = reminderToDelete,
            onConfirm = { reminder ->
                onCancelReminder(reminder.id)
                reminderViewModel.deleteReminder(reminder.id)
                showDeleteConfirmDialog = false
                reminderToDelete = null
            },
            onDismiss = {
                showDeleteConfirmDialog = false
                reminderToDelete = null
            }
        )
    }

    if (showPermissionDeniedDialog) {
        PermissionDeniedDialog(
            onDismiss = { showPermissionDeniedDialog = false }
        )
    }
}

@Composable
fun NoRemindersComposable() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No feeding reminders yet",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tap + to add a reminder for your pet",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
