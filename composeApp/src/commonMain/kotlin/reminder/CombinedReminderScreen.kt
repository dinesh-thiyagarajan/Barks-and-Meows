package reminder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.reminder.composables.ReminderScreen
import com.app.reminder.dataModels.FeedingReminder
import com.app.reminder.viewModels.PetsUiState
import com.app.reminder.viewModels.ReminderViewModel
import com.app.uicomponents.composables.loading.LoadingComposable
import com.app.vaccine.dataModels.VaccineNote
import com.app.vaccine.viewModels.VaccineNoteViewModel
import com.app.vaccine.viewModels.VaccineRemindersUiState
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CombinedReminderScreen(
    reminderViewModel: ReminderViewModel = koinViewModel(),
    vaccineNoteViewModel: VaccineNoteViewModel = koinViewModel(),
    onReminderClick: (String) -> Unit = {},
    onScheduleReminder: (FeedingReminder) -> Unit = {},
    onCancelReminder: (String) -> Unit = {},
    onRequestNotificationPermission: (() -> Unit, () -> Unit) -> Unit = { onGranted, _ -> onGranted() },
    isNotificationPermissionGranted: () -> Boolean = { true }
) {
    val tabs = listOf("Feeding", "Vaccine")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    val petsUiState by reminderViewModel.petsUiState.collectAsState()

    // Fetch pets so we can get petIds for vaccine reminders
    LaunchedEffect(Unit) {
        reminderViewModel.getPets()
    }

    // Once pets are loaded, fetch vaccine reminders
    LaunchedEffect(petsUiState) {
        val state = petsUiState
        if (state is PetsUiState.Success && state.pets.isNotEmpty()) {
            val petIds = state.pets.map { it.id }
            vaccineNoteViewModel.getVaccineReminders(petIds)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (pagerState.currentPage == index)
                                FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> {
                    ReminderScreen(
                        reminderViewModel = reminderViewModel,
                        onReminderClick = onReminderClick,
                        onScheduleReminder = onScheduleReminder,
                        onCancelReminder = onCancelReminder,
                        onRequestNotificationPermission = onRequestNotificationPermission,
                        isNotificationPermissionGranted = isNotificationPermissionGranted
                    )
                }
                1 -> {
                    VaccineRemindersTab(vaccineNoteViewModel = vaccineNoteViewModel)
                }
            }
        }
    }
}

@Composable
private fun VaccineRemindersTab(
    vaccineNoteViewModel: VaccineNoteViewModel
) {
    val vaccineRemindersUiState by vaccineNoteViewModel.vaccineRemindersUiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var noteToRemoveReminder by remember { mutableStateOf<VaccineNote?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Vaccine Reminders",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = vaccineRemindersUiState) {
            is VaccineRemindersUiState.Loading -> LoadingComposable()
            is VaccineRemindersUiState.Success -> {
                if (state.vaccineNotesList.isEmpty()) {
                    NoVaccineRemindersComposable()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        items(state.vaccineNotesList) { vaccineNote ->
                            VaccineReminderCardComposable(
                                petName = vaccineNote.petName ?: "Unknown Pet",
                                vaccineName = vaccineNote.vaccine.vaccineName,
                                reminderTimestamp = vaccineNote.reminderTimestamp ?: 0L,
                                doctorName = vaccineNote.doctorName,
                                vaccineDate = vaccineNote.timestamp,
                                onDeleteClick = {
                                    noteToRemoveReminder = vaccineNote
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }
            is VaccineRemindersUiState.Error -> {
                Text(
                    text = "Failed to load vaccine reminders",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    if (showDeleteDialog && noteToRemoveReminder != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                noteToRemoveReminder = null
            },
            title = { Text("Remove Reminder") },
            text = {
                Text(
                    "Remove the vaccine reminder for ${noteToRemoveReminder?.vaccine?.vaccineName}? " +
                            "The vaccine record will be kept."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        noteToRemoveReminder?.let {
                            vaccineNoteViewModel.removeVaccineReminder(it)
                        }
                        showDeleteDialog = false
                        noteToRemoveReminder = null
                    }
                ) {
                    Text("Remove", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        noteToRemoveReminder = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun NoVaccineRemindersComposable() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No vaccine reminders yet",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Set a reminder when adding a vaccine note",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
