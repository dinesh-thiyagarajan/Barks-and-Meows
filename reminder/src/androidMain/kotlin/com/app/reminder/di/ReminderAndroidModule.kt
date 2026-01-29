package com.app.reminder.di

import com.app.reminder.dataSource.ReminderLocalDataSource
import com.app.reminder.scheduler.ReminderSchedulerProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val reminderAndroidModule = module {
    single { ReminderLocalDataSource(androidContext()) }
    single { ReminderSchedulerProvider(androidContext()) }
    single<() -> Unit> {
        val schedulerProvider: ReminderSchedulerProvider = get()
        return@single { schedulerProvider.cancelAllReminders() }
    }
}
