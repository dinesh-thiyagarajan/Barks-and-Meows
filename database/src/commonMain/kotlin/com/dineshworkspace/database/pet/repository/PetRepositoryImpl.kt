package com.dineshworkspace.database.pet.repository

import com.dineshworkspace.database.pet.dataModels.Pet
import com.dineshworkspace.database.pet.dataSource.PetDataSource

class PetRepositoryImpl(private val petDataSource: PetDataSource) : PetRepository {
    override suspend fun addPet(pet: Pet) = petDataSource.addPet(pet = pet)
    override suspend fun getPets() = petDataSource.getPets()
}