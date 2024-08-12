package com.dineshworkspace.database.pet.repository

import com.dineshworkspace.database.pet.dataModels.Pet
import com.dineshworkspace.database.pet.dataModels.PetCategory
import com.dineshworkspace.database.pet.dataSource.PetDataSource
import kotlinx.coroutines.flow.Flow

class PetRepositoryImpl(private val petDataSource: PetDataSource) : PetRepository {
    override suspend fun addPet(pet: Pet) = petDataSource.addPet(pet = pet)
    override suspend fun getPets() = petDataSource.getPets()
    override suspend fun getPetCategories(): Flow<List<PetCategory>> =
        petDataSource.getPetCategories()
}