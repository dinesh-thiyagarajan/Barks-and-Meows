package com.app.pet.scheduler

actual class BirthdayReminderSchedulerProvider {
    actual fun scheduleBirthday(petId: String, petName: String, birthDate: String) {
        // TODO: iOS UNUserNotificationCenter implementation
    }

    actual fun cancelBirthday(petId: String) {
        // TODO: iOS UNUserNotificationCenter implementation
    }
}
