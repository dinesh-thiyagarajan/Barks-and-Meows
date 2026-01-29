package com.app.reminder.useCases

import com.app.reminder.repository.ReminderRepository

class GetReminderByIdUseCase(
    private val reminderRepository: ReminderRepository
) {
    operator fun invoke(reminderId: String) = reminderRepository.getReminderById(reminderId)
}
