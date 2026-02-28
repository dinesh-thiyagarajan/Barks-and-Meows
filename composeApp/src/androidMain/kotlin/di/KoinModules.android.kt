package di

import com.app.ads.di.adsAndroidModule
import com.app.reminder.di.reminderAndroidModule
import com.app.vaccine.di.vaccineAndroidModule
import org.koin.core.module.Module

actual fun platformModules(): List<Module> = listOf(reminderAndroidModule, adsAndroidModule, vaccineAndroidModule)
