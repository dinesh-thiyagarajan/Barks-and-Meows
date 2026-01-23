package com.dineshworkspace.repositories

import com.dineshworkspace.dataSource.PetDataSource
import com.dineshworkspace.dataModels.Pet
import com.dineshworkspace.dataModels.PetCategory
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