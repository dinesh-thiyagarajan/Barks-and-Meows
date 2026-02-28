package reminder

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
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

private data class TabItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

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
    val tabs = listOf(
        TabItem("Feeding", Icons.Filled.Restaurant, Icons.Outlined.Restaurant),
        TabItem("Vaccine", Icons.Filled.MedicalServices, Icons.Outlined.MedicalServices)
    )
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
        // Header
        Text(
            text = "Reminders",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 12.dp)
        )

        // Custom pill-shaped tab bar
        PillTabBar(
            tabs = tabs,
            selectedIndex = pagerState.currentPage,
            onTabSelected = { index ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

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
private fun PillTabBar(
    tabs: List<TabItem>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            tabs.forEachIndexed { index, tab ->
                val isSelected = selectedIndex == index

                val backgroundColor by animateColorAsState(
                    targetValue = if (isSelected)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0f),
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    label = "tabBgColor"
                )

                val contentColor by animateColorAsState(
                    targetValue = if (isSelected)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    label = "tabContentColor"
                )

                val elevation by animateDpAsState(
                    targetValue = if (isSelected) 4.dp else 0.dp,
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    label = "tabElevation"
                )

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onTabSelected(index) }
                        ),
                    shape = RoundedCornerShape(12.dp),
                    color = backgroundColor,
                    shadowElevation = elevation
                ) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = contentColor
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = tab.title,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = contentColor
                        )
                    }
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
        Icon(
            imageVector = Icons.Outlined.MedicalServices,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "No vaccine reminders yet",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Set a reminder when adding a vaccine note",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}
