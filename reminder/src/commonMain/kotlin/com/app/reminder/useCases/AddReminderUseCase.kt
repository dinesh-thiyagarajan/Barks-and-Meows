package com.app.reminder.useCases

import com.app.reminder.dataModels.FeedingReminder
import com.app.reminder.repository.ReminderRepository

class AddReminderUseCase(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(reminder: FeedingReminder) =
        reminderRepository.addReminder(reminder)
}
