package com.app.reminder.useCases

import com.app.reminder.repository.ReminderRepository

class UpdateLastFedTimeUseCase(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(reminderId: String, lastFedTime: Long) =
        reminderRepository.updateLastFedTime(reminderId, lastFedTime)
}
