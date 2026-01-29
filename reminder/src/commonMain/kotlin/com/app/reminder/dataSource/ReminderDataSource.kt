package com.app.reminder.dataSource

import com.app.reminder.dataModels.FeedingReminder
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow

class ReminderDataSource(
    private val firestore: FirebaseFirestore,
    private val userId: String,
    private val baseEnv: String,
    private val reminderCollection: String
) {

    suspend fun addReminder(reminder: FeedingReminder) =
        firestore.collection(baseEnv).document(reminderCollection)
            .collection(userId)
            .document(reminder.id)
            .set(data = reminder, buildSettings = {
                encodeDefaults = true
            })

    suspend fun updateReminder(reminder: FeedingReminder) =
        firestore.collection(baseEnv).document(reminderCollection)
            .collection(userId)
            .document(reminder.id)
            .set(data = reminder, buildSettings = {
                encodeDefaults = true
            })

    fun getReminders() = flow {
        try {
            val remindersList = mutableListOf<FeedingReminder>()
            firestore.collection(baseEnv).document(reminderCollection)
                .collection(userId)
                .snapshots.collect { querySnapshot ->
                    remindersList.clear()
                    querySnapshot.documents.forEach { documentSnapshot ->
                        val reminder = documentSnapshot.data<FeedingReminder>()
                        remindersList.add(reminder)
                    }
                    emit(remindersList)
                }
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    fun getReminderById(reminderId: String) = flow {
        try {
            firestore.collection(baseEnv).document(reminderCollection)
                .collection(userId)
                .document(reminderId)
                .snapshots.collect { documentSnapshot ->
                    val reminder = documentSnapshot.data<FeedingReminder>()
                    emit(reminder)
                }
        } catch (e: Exception) {
            // Handle Firestore permission errors gracefully
        }
    }

    fun getReminderByPetId(petId: String) = flow {
        try {
            firestore.collection(baseEnv).document(reminderCollection)
                .collection(userId)
                .snapshots.collect { querySnapshot ->
                    val reminder = querySnapshot.documents
                        .map { it.data<FeedingReminder>() }
                        .find { it.petId == petId }
                    emit(reminder)
                }
        } catch (e: Exception) {
            emit(null)
        }
    }

    fun deleteReminder(reminderId: String) = flow {
        firestore.collection(baseEnv).document(reminderCollection)
            .collection(userId)
            .document(reminderId)
            .delete()
        emit(true)
    }

    suspend fun updateLastFedTime(reminderId: String, lastFedTime: Long) {
        try {
            firestore.collection(baseEnv).document(reminderCollection)
                .collection(userId)
                .document(reminderId)
                .update("lastFedTime" to lastFedTime)
        } catch (e: Exception) {
            // Handle error gracefully
        }
    }
}
