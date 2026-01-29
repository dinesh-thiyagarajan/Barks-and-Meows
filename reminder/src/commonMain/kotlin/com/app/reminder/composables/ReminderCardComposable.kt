package com.app.reminder.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.app.reminder.dataModels.FeedingReminder
import kotlinx.coroutines.delay

@Composable
fun ReminderCardComposable(
    reminder: FeedingReminder,
    onReminderClick: () -> Unit,
    onToggleEnabled: (Boolean) -> Unit,
    onMarkAsFed: () -> Unit,
    onDelete: () -> Unit
) {
    var isAnimating by remember { mutableStateOf(false) }
    var currentTimeMillis by remember { mutableLongStateOf(kotlin.time.Clock.System.now().toEpochMilliseconds()) }

    // Update time every second for countdown
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTimeMillis = kotlin.time.Clock.System.now().toEpochMilliseconds()
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (isAnimating) 0.95f else 1f,
        animationSpec = tween(150),
        finishedListener = { isAnimating = false }
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable { onReminderClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ReminderCardHeader(
                petName = reminder.petName,
                isEnabled = reminder.isEnabled,
                onToggleEnabled = onToggleEnabled
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(16.dp))

            ReminderCardInfo(
                intervalMinutes = reminder.intervalMinutes,
                lastFedTime = reminder.lastFedTime
            )

            Spacer(modifier = Modifier.height(12.dp))

            NextReminderCard(
                lastFedTime = reminder.lastFedTime,
                intervalMinutes = reminder.intervalMinutes,
                currentTimeMillis = currentTimeMillis,
                isEnabled = reminder.isEnabled
            )

            Spacer(modifier = Modifier.height(16.dp))

            ReminderCardActions(
                onMarkAsFed = {
                    isAnimating = true
                    onMarkAsFed()
                },
                onDelete = onDelete
            )
        }
    }
}
