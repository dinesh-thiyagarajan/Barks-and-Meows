package com.app.reminder.useCases

import com.app.reminder.repository.ReminderRepository

class DeleteReminderUseCase(
    private val reminderRepository: ReminderRepository
) {
    operator fun invoke(reminderId: String) = reminderRepository.deleteReminder(reminderId)
}
