package di

import com.app.reminder.di.reminderAndroidModule
import org.koin.core.module.Module

actual fun platformModules(): List<Module> = listOf(reminderAndroidModule)
