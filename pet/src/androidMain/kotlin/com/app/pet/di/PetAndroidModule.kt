package com.app.pet.di

import com.app.pet.scheduler.BirthdayReminderSchedulerProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val petAndroidModule = module {
    single { BirthdayReminderSchedulerProvider(androidContext()) }
}
