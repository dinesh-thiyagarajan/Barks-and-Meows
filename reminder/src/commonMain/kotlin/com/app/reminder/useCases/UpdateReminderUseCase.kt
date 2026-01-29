package com.app.reminder.useCases

import com.app.reminder.dataModels.FeedingReminder
import com.app.reminder.repository.ReminderRepository

class UpdateReminderUseCase(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(reminder: FeedingReminder) =
        reminderRepository.updateReminder(reminder)
}
