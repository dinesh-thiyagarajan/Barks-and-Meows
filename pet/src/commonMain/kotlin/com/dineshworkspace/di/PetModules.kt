package com.dineshworkspace.di

import com.dineshworkspace.dataSource.PetDataSource
import com.dineshworkspace.repositories.PetRepository
import com.dineshworkspace.repositories.PetRepositoryImpl
import com.dineshworkspace.useCases.AddPetUseCase
import com.dineshworkspace.useCases.DeletePetUseCase
import com.dineshworkspace.useCases.GetPetCategoriesUseCase
import com.dineshworkspace.useCases.GetPetDetailsUseCase
import com.dineshworkspace.useCases.GetPetsUseCase
import com.dineshworkspace.useCases.UpdatePetUseCase
import com.dineshworkspace.viewModels.PetDetailsViewModel
import com.dineshworkspace.viewModels.PetViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val petModule = module {
    single<FirebaseFirestore> { Firebase.firestore }
    single<AddPetUseCase> { AddPetUseCase(petRepository = get()) }
    single<UpdatePetUseCase> { UpdatePetUseCase(petRepository = get()) }
    single<GetPetsUseCase> { GetPetsUseCase(petRepository = get()) }
    single<DeletePetUseCase> { DeletePetUseCase(petRepository = get()) }
    single<PetDataSource> {
        PetDataSource(
            firestore = get(),
            userId = get(named("user_id")),
            baseEnv = get(named("base_env")),
            petCollection = get(named("pets_collection"))
        )
    }
    single<PetRepository> { PetRepositoryImpl(petDataSource = get()) }
    single<GetPetCategoriesUseCase> { GetPetCategoriesUseCase(petRepository = get()) }
    single<GetPetDetailsUseCase> { GetPetDetailsUseCase(petRepository = get()) }
    viewModel {
        PetViewModel(
            addPetUseCase = get(),
            updatePetUseCase = get(),
            getPetsUseCase = get(),
            getPetCategoriesUseCase = get()
        )
    }
    viewModel { PetDetailsViewModel(getPetDetailsUseCase = get(), deletePetUseCase = get()) }
}