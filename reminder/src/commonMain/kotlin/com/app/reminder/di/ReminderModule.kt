package com.app.reminder.di

import com.app.reminder.dataSource.ReminderDataSource
import com.app.reminder.repository.ReminderRepository
import com.app.reminder.repository.ReminderRepositoryImpl
import com.app.reminder.useCases.AddReminderUseCase
import com.app.reminder.useCases.DeleteReminderUseCase
import com.app.reminder.useCases.GetReminderByIdUseCase
import com.app.reminder.useCases.GetRemindersUseCase
import com.app.reminder.useCases.UpdateLastFedTimeUseCase
import com.app.reminder.useCases.UpdateReminderUseCase
import com.app.reminder.viewModels.ReminderViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val reminderModule = module {
    single<String>(named("reminder_collection")) { "Reminders" }
    
    single<ReminderDataSource> {
        ReminderDataSource(
            firestore = get(),
            userId = get(named("user_id")),
            baseEnv = get(named("base_env")),
            reminderCollection = get(named("reminder_collection"))
        )
    }
    
    single<ReminderRepository> {
        ReminderRepositoryImpl(reminderDataSource = get())
    }
    
    single { GetRemindersUseCase(reminderRepository = get()) }
    single { AddReminderUseCase(reminderRepository = get()) }
    single { UpdateReminderUseCase(reminderRepository = get()) }
    single { DeleteReminderUseCase(reminderRepository = get()) }
    single { GetReminderByIdUseCase(reminderRepository = get()) }
    single { UpdateLastFedTimeUseCase(reminderRepository = get()) }
    
    viewModel {
        ReminderViewModel(
            getRemindersUseCase = get(),
            addReminderUseCase = get(),
            updateReminderUseCase = get(),
            deleteReminderUseCase = get(),
            getReminderByIdUseCase = get(),
            updateLastFedTimeUseCase = get(),
            getPetsUseCase = get()
        )
    }
}
