package com.app.repositories

import com.app.dataModels.Pet
import com.app.dataModels.PetCategory
import com.app.dataSource.PetDataSource
import kotlinx.coroutines.flow.Flow

class PetRepositoryImpl(private val petDataSource: PetDataSource) : PetRepository {
    override suspend fun addPet(pet: Pet) = petDataSource.addPet(pet = pet)

    override suspend fun updatePet(pet: Pet) = petDataSource.updatePet(pet = pet)

    override suspend fun getPets() = petDataSource.getPets()

    override suspend fun getPetCategories(): Flow<List<PetCategory>> =
        petDataSource.getPetCategories()

    override suspend fun getPetDetails(petId: String): Flow<Pet> =
        petDataSource.getPetDetails(petId = petId)

    override suspend fun deletePet(petId: String): Flow<Boolean> =
        petDataSource.deletePet(petId = petId)
}
