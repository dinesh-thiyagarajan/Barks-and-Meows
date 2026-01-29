package com.app.reminder.dataSource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.app.reminder.dataModels.FeedingReminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.reminderDataStore: DataStore<Preferences> by preferencesDataStore(name = "reminders")

class ReminderLocalDataSource(private val context: Context) {
    
    private val json = Json { ignoreUnknownKeys = true }
    
    companion object {
        private val REMINDERS_KEY = stringPreferencesKey("reminders_list")
    }
    
    suspend fun saveReminders(reminders: List<FeedingReminder>) {
        context.reminderDataStore.edit { preferences ->
            val remindersJson = json.encodeToString(reminders)
            preferences[REMINDERS_KEY] = remindersJson
        }
    }
    
    suspend fun addReminder(reminder: FeedingReminder) {
        context.reminderDataStore.edit { preferences ->
            val currentReminders = getRemindersFromPreferences(preferences)
            val updatedReminders = currentReminders + reminder
            preferences[REMINDERS_KEY] = json.encodeToString(updatedReminders)
        }
    }
    
    suspend fun updateReminder(reminder: FeedingReminder) {
        context.reminderDataStore.edit { preferences ->
            val currentReminders = getRemindersFromPreferences(preferences)
            val updatedReminders = currentReminders.map {
                if (it.id == reminder.id) reminder else it
            }
            preferences[REMINDERS_KEY] = json.encodeToString(updatedReminders)
        }
    }
    
    suspend fun deleteReminder(reminderId: String) {
        context.reminderDataStore.edit { preferences ->
            val currentReminders = getRemindersFromPreferences(preferences)
            val updatedReminders = currentReminders.filter { it.id != reminderId }
            preferences[REMINDERS_KEY] = json.encodeToString(updatedReminders)
        }
    }
    
    fun getReminders(): Flow<List<FeedingReminder>> {
        return context.reminderDataStore.data.map { preferences ->
            getRemindersFromPreferences(preferences)
        }
    }
    
    suspend fun getReminderById(reminderId: String): Flow<FeedingReminder?> {
        return context.reminderDataStore.data.map { preferences ->
            getRemindersFromPreferences(preferences).find { it.id == reminderId }
        }
    }
    
    suspend fun updateLastFedTime(reminderId: String, lastFedTime: Long) {
        context.reminderDataStore.edit { preferences ->
            val currentReminders = getRemindersFromPreferences(preferences)
            val updatedReminders = currentReminders.map {
                if (it.id == reminderId) it.copy(lastFedTime = lastFedTime) else it
            }
            preferences[REMINDERS_KEY] = json.encodeToString(updatedReminders)
        }
    }
    
    private fun getRemindersFromPreferences(preferences: Preferences): List<FeedingReminder> {
        val remindersJson = preferences[REMINDERS_KEY] ?: return emptyList()
        return try {
            json.decodeFromString<List<FeedingReminder>>(remindersJson)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
