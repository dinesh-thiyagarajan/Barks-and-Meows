package com.dineshworkspace.di

import com.dineshworkspace.dataSource.PetDataSource
import com.dineshworkspace.repositories.PetRepository
import com.dineshworkspace.repositories.PetRepositoryImpl
import com.dineshworkspace.useCases.AddPetUseCase
import com.dineshworkspace.useCases.GetPetCategoriesUseCase
import com.dineshworkspace.useCases.GetPetDetailsUseCase
import com.dineshworkspace.useCases.GetPetsUseCase
import com.dineshworkspace.viewModels.PetDetailsViewModel
import com.dineshworkspace.viewModels.PetViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module

val petModule = module {
    single<FirebaseFirestore> { Firebase.firestore }
    single<AddPetUseCase> { AddPetUseCase(get()) }
    single<GetPetsUseCase> { GetPetsUseCase(get()) }
    single<PetDataSource> { PetDataSource(get(), get()) }
    single<PetRepository> { PetRepositoryImpl(get()) }
    single<GetPetCategoriesUseCase> { GetPetCategoriesUseCase(get()) }
    single<GetPetDetailsUseCase> { GetPetDetailsUseCase(get()) }
    viewModel { PetViewModel(get(), get(), get()) }
    viewModel { PetDetailsViewModel(get()) }
}