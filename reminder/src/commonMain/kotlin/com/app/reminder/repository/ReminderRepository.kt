package com.app.reminder.repository

import com.app.reminder.dataModels.FeedingReminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    suspend fun addReminder(reminder: FeedingReminder)
    suspend fun updateReminder(reminder: FeedingReminder)
    fun getReminders(): Flow<List<FeedingReminder>>
    fun getReminderById(reminderId: String): Flow<FeedingReminder?>
    fun getReminderByPetId(petId: String): Flow<FeedingReminder?>
    fun deleteReminder(reminderId: String): Flow<Boolean>
    suspend fun updateLastFedTime(reminderId: String, lastFedTime: Long)
}
