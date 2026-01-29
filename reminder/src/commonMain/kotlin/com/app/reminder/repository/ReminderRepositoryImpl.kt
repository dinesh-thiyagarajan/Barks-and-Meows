package com.app.reminder.repository

import com.app.reminder.dataModels.FeedingReminder
import com.app.reminder.dataSource.ReminderDataSource
import kotlinx.coroutines.flow.Flow

class ReminderRepositoryImpl(
    private val reminderDataSource: ReminderDataSource
) : ReminderRepository {

    override suspend fun addReminder(reminder: FeedingReminder) =
        reminderDataSource.addReminder(reminder)

    override suspend fun updateReminder(reminder: FeedingReminder) =
        reminderDataSource.updateReminder(reminder)

    override fun getReminders(): Flow<List<FeedingReminder>> =
        reminderDataSource.getReminders()

    override fun getReminderById(reminderId: String): Flow<FeedingReminder?> =
        reminderDataSource.getReminderById(reminderId)

    override fun getReminderByPetId(petId: String): Flow<FeedingReminder?> =
        reminderDataSource.getReminderByPetId(petId)

    override fun deleteReminder(reminderId: String): Flow<Boolean> =
        reminderDataSource.deleteReminder(reminderId)

    override suspend fun updateLastFedTime(reminderId: String, lastFedTime: Long) =
        reminderDataSource.updateLastFedTime(reminderId, lastFedTime)
}
