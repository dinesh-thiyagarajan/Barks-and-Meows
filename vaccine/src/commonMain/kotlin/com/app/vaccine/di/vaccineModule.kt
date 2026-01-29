package com.app.vaccine.di

import com.app.vaccine.dataSource.VaccineDataSource
import com.app.vaccine.repository.VaccineRepository
import com.app.vaccine.repository.VaccineRepositoryImpl
import com.app.vaccine.useCases.AddVaccineNoteUseCase
import com.app.vaccine.useCases.DeleteVaccineNoteUseCase
import com.app.vaccine.useCases.GetVaccineNotesUseCase
import com.app.vaccine.useCases.GetVaccinesUseCase
import com.app.vaccine.viewModels.VaccineNoteViewModel
import org.koin.core.module.dsl.viewModel
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
    single { DeleteVaccineNoteUseCase(vaccineRepository = get()) }
    single { GetVaccinesUseCase(vaccineRepository = get()) }
    viewModel {
        VaccineNoteViewModel(
            addVaccineNoteUseCase = get(),
            getVaccineNotesUseCase = get(),
            getVaccinesUseCase = get(),
            deleteVaccineNoteUseCase = get()
        )
    }
}