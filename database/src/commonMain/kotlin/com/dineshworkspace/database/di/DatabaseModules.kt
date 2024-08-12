package com.dineshworkspace.database.di

import com.dineshworkspace.database.pet.dataSource.PetDataSource
import com.dineshworkspace.database.pet.repository.PetRepository
import com.dineshworkspace.database.pet.repository.PetRepositoryImpl
import com.dineshworkspace.database.pet.useCases.AddPetUseCase
import com.dineshworkspace.database.pet.useCases.GetPetCategoriesUseCase
import com.dineshworkspace.database.pet.useCases.GetPetsUseCase
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import org.koin.dsl.module

val databaseModule = module {
    single<FirebaseFirestore> { Firebase.firestore }
    single<AddPetUseCase> { AddPetUseCase(get()) }
    single<GetPetsUseCase> { GetPetsUseCase(get()) }
    single<PetDataSource> { PetDataSource(get(), get()) }
    single<PetRepository> { PetRepositoryImpl(get()) }
    single<GetPetCategoriesUseCase> { GetPetCategoriesUseCase(get()) }
}