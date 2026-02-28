package com.app.vaccine.di

import com.app.vaccine.scheduler.VaccineReminderSchedulerProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val vaccineAndroidModule = module {
    single { VaccineReminderSchedulerProvider(androidContext()) }
}
