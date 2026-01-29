package com.app.reminder.di

import com.app.reminder.dataSource.ReminderLocalDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val reminderAndroidModule = module {
    single { ReminderLocalDataSource(androidContext()) }
}
