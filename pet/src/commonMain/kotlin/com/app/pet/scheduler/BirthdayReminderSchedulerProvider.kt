package com.app.pet.scheduler

expect class BirthdayReminderSchedulerProvider {
    fun scheduleBirthday(petId: String, petName: String, birthDate: String)
    fun cancelBirthday(petId: String)
}
