package com.app.reminder.dataModels

import kotlinx.serialization.Serializable

@Serializable
enum class ReminderInterval(val displayName: String, val minutes: Int, val hours: Int) {
    // Minutes options (minimum 15 minutes)
    EVERY_15_MINUTES("15 minutes", 15, 0),
    EVERY_30_MINUTES("30 minutes", 30, 0),
    EVERY_45_MINUTES("45 minutes", 45, 0),

    // Hours options
    EVERY_1_HOUR("1 hour", 60, 1),
    EVERY_2_HOURS("2 hours", 120, 2),
    EVERY_3_HOURS("3 hours", 180, 3),
    EVERY_4_HOURS("4 hours", 240, 4),
    EVERY_6_HOURS("6 hours", 360, 6),
    EVERY_8_HOURS("8 hours", 480, 8),
    EVERY_12_HOURS("12 hours", 720, 12),
    EVERY_24_HOURS("24 hours", 1440, 24);

    companion object {
        val minuteOptions = listOf(EVERY_15_MINUTES, EVERY_30_MINUTES, EVERY_45_MINUTES)
        val hourOptions = listOf(
            EVERY_1_HOUR, EVERY_2_HOURS, EVERY_3_HOURS, EVERY_4_HOURS,
            EVERY_6_HOURS, EVERY_8_HOURS, EVERY_12_HOURS, EVERY_24_HOURS
        )
    }
}
