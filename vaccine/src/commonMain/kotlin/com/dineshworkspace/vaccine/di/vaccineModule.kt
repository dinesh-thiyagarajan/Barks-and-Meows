package com.dineshworkspace.vaccine.di

import com.dineshworkspace.vaccine.dataSource.VaccineDataSource
import com.dineshworkspace.vaccine.repository.VaccineRepository
import com.dineshworkspace.vaccine.repository.VaccineRepositoryImpl
import com.dineshworkspace.vaccine.useCases.AddVaccineNoteUseCase
import com.dineshworkspace.vaccine.useCases.GetVaccineNotesUseCase
import com.dineshworkspace.vaccine.useCases.GetVaccinesUseCase
import com.dineshworkspace.vaccine.viewModels.VaccineNoteViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val vaccineModule = module {
    single<VaccineRepository> { VaccineRepositoryImpl(vaccineDataSource = get()) }
    single<VaccineDataSource> {
        VaccineDataSource(
            firestore = get(), userId = get(named("user_id")),
            baseEnv = get(named("base_env")),
            petCollection = get(
                named("pets_collection"),
            ),
            vaccineNotesCollection = get(
                named("vaccine_notes_collection"),
            )
        )
    }
    single { GetVaccineNotesUseCase(vaccineRepository = get()) }
    single { AddVaccineNoteUseCase(vaccineRepository = get()) }
    single { GetVaccinesUseCase(vaccineRepository = get()) }
    viewModel {
        VaccineNoteViewModel(
            addVaccineNoteUseCase = get(),
            getVaccineNotesUseCase = get(),
            getVaccinesUseCase = get()
        )
    }
}