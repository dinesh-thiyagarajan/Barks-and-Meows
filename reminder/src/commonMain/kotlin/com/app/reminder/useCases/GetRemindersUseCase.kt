package com.app.reminder.useCases

import com.app.reminder.repository.ReminderRepository

class GetRemindersUseCase(
    private val reminderRepository: ReminderRepository
) {
    operator fun invoke() = reminderRepository.getReminders()
}
