package com.app.di

import com.app.dataSource.PetDataSource
import com.app.repositories.PetRepository
import com.app.repositories.PetRepositoryImpl
import com.app.useCases.AddPetUseCase
import com.app.useCases.DeletePetUseCase
import com.app.useCases.GetPetCategoriesUseCase
import com.app.useCases.GetPetDetailsUseCase
import com.app.useCases.GetPetsUseCase
import com.app.useCases.UpdatePetUseCase
import com.app.viewModels.PetDetailsViewModel
import com.app.viewModels.PetViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import org.koin.core.module.dsl.viewModel
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
