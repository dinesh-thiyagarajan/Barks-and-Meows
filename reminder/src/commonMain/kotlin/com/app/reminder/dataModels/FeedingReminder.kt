package com.app.reminder.dataModels

import kotlinx.serialization.Serializable

@Serializable
data class FeedingReminder(
    val id: String = "",
    val petId: String = "",
    val petName: String = "",
    val intervalMinutes: Int = 480, // Default 8 hours in minutes
    val isEnabled: Boolean = true,
    val lastFedTime: Long? = null, // Nullable - null means "Never"
    val createdAt: Long = 0L
)

@Serializable
enum class IntervalUnit(val displayName: String) {
    MINUTES("Minutes"),
    HOURS("Hours")
}

fun formatInterval(intervalMinutes: Int): String {
    return when {
        intervalMinutes >= 60 && intervalMinutes % 60 == 0 -> {
            val hours = intervalMinutes / 60
            if (hours == 1) "1 hour" else "$hours hours"
        }
        intervalMinutes >= 60 -> {
            val hours = intervalMinutes / 60
            val mins = intervalMinutes % 60
            "${hours}h ${mins}m"
        }
        else -> {
            if (intervalMinutes == 1) "1 minute" else "$intervalMinutes minutes"
        }
    }
}
