package com.app.pet.di

import com.app.pet.scheduler.BirthdayReminderSchedulerProvider
import org.koin.dsl.module

val petIosModule = module {
    single { BirthdayReminderSchedulerProvider() }
}
