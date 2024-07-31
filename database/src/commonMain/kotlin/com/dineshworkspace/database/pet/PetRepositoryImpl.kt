package com.dineshworkspace.database.pet

class PetRepositoryImpl(private val petDataSource: PetDataSource) : PetRepository {
    override suspend fun addPet(pet: Pet) = petDataSource.addPet()
}